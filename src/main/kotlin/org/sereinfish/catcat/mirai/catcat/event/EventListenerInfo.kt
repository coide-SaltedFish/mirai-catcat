package org.sereinfish.catcat.mirai.catcat.event

import kotlinx.coroutines.runBlocking
import net.mamoe.mirai.console.plugin.id
import net.mamoe.mirai.console.plugin.name
import net.mamoe.mirai.event.Event
import org.sereinfish.catcat.mirai.catcat.core.handler.Handler
import org.sereinfish.catcat.mirai.catcat.core.handler.HandlerChain
import org.sereinfish.catcat.mirai.catcat.core.handler.HandlerContext
import org.sereinfish.catcat.mirai.catcat.event.handler.EventHandleChain
import org.sereinfish.catcat.mirai.catcat.event.handler.EventHandler
import org.sereinfish.catcat.mirai.catcat.event.handler.EventHandlerContext
import org.sereinfish.catcat.mirai.catcat.event.untils.pluginErrorInfoLog
import org.sereinfish.catcat.mirai.catcat.packages.manager.CatPackageManager
import org.sereinfish.catcat.mirai.catcat.utils.*
import org.sereinfish.catcat.mirai.catcat.utils.AnnotationUtils.functionExistAnnotationAll
import kotlin.reflect.KFunction
import kotlin.reflect.full.callSuspend
import kotlin.reflect.full.isSupertypeOf
import kotlin.reflect.full.starProjectedType
import kotlin.reflect.full.valueParameters
import kotlin.reflect.jvm.javaType
import kotlin.reflect.jvm.jvmErasure
import kotlin.reflect.jvm.jvmName

/**
 * 事件监听器信息
 */
class EventListenerInfo(
    val classInfo: CatPackageManager.PluginInfoData.ClassInfo,
    val pluginEventInfo: PluginEventInfo, // 所在的插件
) {
    // 实例
    val instance: CatEventListener = classInfo.clazz.newInstance() as CatEventListener

    // 处理器
    val handlerChain = EventHandleChain()

    init {
        runBlocking {
            val beforeHandler = getBeforeHandler()
            val afterHandler = getAfterHandler()
            val catchHandler = getCatchHandler()

            classInfo.getFunctionByAnnotationAll<CatEventListener.Handler>().forEach {
                // 执行函数获取处理器
                getHandler(it.function)?.let { handler ->
                    // 加载各阶段处理器
                    handler.beforeHandlerChain.addAll(beforeHandler)
                    handler.afterHandlerChain.addAll(afterHandler)
                    handler.catchHandlerChain.addAll(catchHandler)

                    // 获取优先级
                    val level = handler.level ?: it.annotation.level
                    // 载入
                    handlerChain.add(handler, level)
                } ?: logger.error("处理器加载失败")
            }
        }
    }

    /**
     * 在监听器内进行事件广播
     */
    suspend fun broadcast(event: Event){
        for (handler in handlerChain.handlerChain){
            // 处理器执行
            val context = EventHandlerContext(event)

            handler.invoke(context)

            // 判断是否停止
            if (context.isStop)
                break
        }
    }

    /**
     * 获取前置处理器实例
     */
    private suspend fun getBeforeHandler(): HandlerChain{
        val list = EventHandleChain()
        classInfo.getFunctionByAnnotationAll<CatEventListener.Before>().forEach {
            // 排除全局处理器
            if (it.function.functionExistAnnotationAll<CatEventListener.Global>().not()){
                // 执行函数获取处理器
                getHandler(it.function)?.let { handler ->
                    // 获取优先级
                    val level = handler.level ?: it.annotation.level
                    // 载入
                    list.add(handler, level)
                } ?: logger.error("处理器加载失败")
            }
        }
        return list
    }

    /**
     * 获取后置处理器实例
     */
    private suspend fun getAfterHandler(): HandlerChain{
        val list = EventHandleChain()
        classInfo.getFunctionByAnnotationAll<CatEventListener.After>().forEach {
            // 排除全局处理器
            if (it.function.functionExistAnnotationAll<CatEventListener.Global>().not()){
                // 执行函数获取处理器
                getHandler(it.function)?.let { handler ->
                    // 获取优先级
                    val level = handler.level ?: it.annotation.level
                    // 载入
                    list.add(handler, level)
                } ?: logger.error("处理器加载失败")
            }
        }
        return list
    }

    /**
     * 获取异常处理器实例
     */
    private suspend fun getCatchHandler(): HandlerChain{
        val list = EventHandleChain()
        classInfo.getFunctionByAnnotationAll<CatEventListener.Catch>().forEach {
            // 排除全局处理器
            if (it.function.functionExistAnnotationAll<CatEventListener.Global>().not()){
                // 执行函数获取处理器
                getHandler(it.function)?.let { handler ->
                    // 获取优先级
                    val level = handler.level ?: it.annotation.level
                    // 载入
                    list.add(handler, level)
                } ?: logger.error("处理器加载失败")
            }
        }
        return list
    }

    private suspend fun getHandler(function: KFunction<*>): Handler<HandlerContext>?{
        // 执行前检查
        if (Handler::class.java.isAssignableFrom(function.returnType.jvmErasure.java).not()) { // 检查返回类型
            logger.error(pluginErrorInfoLog(
                pluginEventInfo.plugin,
                """
                        处理器加载失败
                        异常插件：${pluginEventInfo.plugin.name}(${pluginEventInfo.plugin.id})
                        方法路径：${classInfo.clazz.name}.${function.name}
                        返回类型：${function.returnType.javaClass.typeName}
                        信息：错误的返回类型，返回类型应该为Handler或其子类
                    """.trimIndent()
            ))
        }else if (function.valueParameters.isNotEmpty()){ // 检查参数
            logger.error(pluginErrorInfoLog(
                pluginEventInfo.plugin,
                """
                        处理器加载失败
                        异常插件：${pluginEventInfo.plugin.name}(${pluginEventInfo.plugin.id})
                        方法路径：${classInfo.clazz.name}.${function.name}
                        参数列表：${function.valueParameters.map { it.name }.toTypedArray().contentToString()}
                        信息：错误的参数，此方法不应有参数
                    """.trimIndent()
            ))
        }else if (function.isExtension){ // 检查是否是外部扩展函数
            logger.error(pluginErrorInfoLog(
                pluginEventInfo.plugin,
                """
                        处理器加载失败
                        异常插件：${pluginEventInfo.plugin.name}(${pluginEventInfo.plugin.id})
                        方法路径：${classInfo.clazz.name}.${function.name}
                        所属扩展类：${function.extensionInfo?.jvmName}}
                        信息：错误的参数，此方法不应有参数
                    """.trimIndent()
            ))
        }else {
            return try {
                (if (function.isInstance){
                    function.callSuspend(instance)
                }else function.callSuspend()) as Handler<HandlerContext>
            }catch (e: Exception){
                logger.error(pluginErrorInfoLog(
                    pluginEventInfo.plugin,
                    """
                        处理器加载失败
                        异常插件：${pluginEventInfo.plugin.name}(${pluginEventInfo.plugin.id})
                        方法路径：${classInfo.clazz.name}.${function.name}
                        信息：构建器执行失败
                    """.trimIndent()
                ))
                throw e.cause ?: e
            }
        }
        return null
    }
}