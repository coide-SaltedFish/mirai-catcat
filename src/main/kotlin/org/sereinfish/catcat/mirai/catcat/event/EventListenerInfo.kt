package org.sereinfish.catcat.mirai.catcat.event

import org.sereinfish.catcat.mirai.catcat.utils.SortedList
import net.mamoe.mirai.event.Event
import org.sereinfish.catcat.mirai.catcat.core.handler.Handler
import org.sereinfish.catcat.mirai.catcat.core.handler.HandlerContext
import org.sereinfish.catcat.mirai.catcat.event.handler.EventHandleChain
import org.sereinfish.catcat.mirai.catcat.packages.manager.CatPackageManager

/**
 * 事件监听器信息
 */
class EventListenerInfo(
    val classInfo: CatPackageManager.PluginInfoData.ClassInfo,
    val pluginEventInfo: PluginEventInfo, // 所在的插件
) {
    // 实例
    val instance = classInfo.clazz.newInstance()

    // 处理器
    val handler = EventHandleChain()

    init {
        val beforeHandler = ;
        val afterHandler = ;
        val catcheHandler = ;
    }

    /**
     * 在监听器内进行事件广播
     */
    suspend fun broadcast(event: Event){

    }

    /**
     * 获取指定注解的处理器实例
     */
    private fun getBeforeHandler(): SortedList<HandlerInfo>{
        val list = SortedList<HandlerInfo>()
        classInfo.getFunctionByAnnotationAll<CatEventListener.Before>().forEach {
            // 执行函数获取处理器
            val handler = it.function.ca

            var level = it.annotation.level

        }
        return list
    }

    /**
     * 处理器信息
     *
     * 用来支持处理器动态更新
     */
    data class HandlerInfo(
        var handler: Handler<HandlerContext>, // 处理器实例
        var function: Function<*>, // 获取处理器的函数
    )
}