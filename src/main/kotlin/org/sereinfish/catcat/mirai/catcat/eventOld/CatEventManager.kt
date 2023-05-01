package org.sereinfish.catcat.mirai.catcat.eventOld

import org.sereinfish.catcat.mirai.catcat.utils.*

/**
 * 插件事件管理器
 */
object CatEventManager {
    private val contextScope = creatContextScope("CatEventManager") // 作用域
    private const val maxThreadCount = 2 // 最大并发数
//    private val eventQueue = LinkedBlockingQueue<Event>() // 事件队列
//
//    val listeners = ArrayList<EventListenerInfo>() // 事件监听器列表
//
//    init {
//        // 处理协程
//        repeat(maxThreadCount){ i ->
//            contextScope.launch {
//                try {
//                    while (true){
//                        // 进行事件处理
//                        broadcast(withContext(Dispatchers.IO) {
//                            eventQueue.take()
//                        })
//                    }
//                }catch (e: Exception){
//                    logger.error("事件处理协程未知异常", e)
//                }
//            }
//        }
//
//
//        GlobalEventChannel.subscribeAlways<Event> {
//            eventQueue.add(it)
//        }
//    }
//
//    /**
//     * 注册事件监听器
//     */
//    fun registerListener(listenerClassInfo: CatPackageManager.PluginInfoData.ClassInfo){
//        listeners.add(EventListenerInfo(listenerClassInfo))
//    }
//
//    /**
//     * 事件广播
//     */
//    suspend fun broadcast(event: Event): Boolean{
//        return listeners.forEachCheck { eventListenerInfo ->
//            tryCatch {
//                catch<Exception> {
//                    logger.error("""
//                        意料之外的错误
//                        listener：${eventListenerInfo.listener::class}
//                    """.trimIndent(), this)
//                    false // 停止广播
//                }
//                eventListenerInfo.broadcast(event).not()
//            }
//        }.not()
//    }
//
//    /**
//     * 事件监听器信息
//     */
//    class EventListenerInfo(
//        private val listenerClassInfo: CatPackageManager.PluginInfoData.ClassInfo
//    ){
//        val listener: CatEventListener = getInstance()
//
//        val beforeHandlers = getHandler<CatEventListener.Before, EventHandler<Event, *>>() // 前置处理器
//        val afterHandlers = getHandler<CatEventListener.After, EventHandler<Event, *>>() // 后置处理器
//        val catchHandlers = getHandler<CatEventListener.Catch, CatchHandler<Throwable>>() // 异常处理器
//        val handlers = getHandler<CatEventListener.Handler, EventHandler<Event, *>>() // 处理器
//
//        /**
//         * 事件广播
//         * true 事件已被处理
//         * false
//         */
//        suspend fun broadcast(event: Event): Boolean{
//            // 本次广播上下文
//            val context = EventHandlerContext()
//
//            // 查找能匹配的处理器
//            for (eventHandler in handlers){
//                eventHandler.context = context
//
//                val isMatch = try {
//                    eventHandler.match(event)
//                }catch (e: Exception){
//                    false
//                }
//                if (isMatch){
//                    try {
//                        beforeHandlers.invoke(event, context) // 前置处理器
//                        (eventHandler.update as suspend EventHandler<Event, *>.() -> Unit).invoke(eventHandler)
//                        eventHandler.execute.invoke(eventHandler.creatInstanceExpand(event), context) // 进行处理
//                        afterHandlers.invoke(event, context) // 后置处理器
//                        return true// 不再继续
//                    }catch (e: Exception){
//                        // 异常处理器
//                        try {
//                            catchHandlers.forEachResult { catchHandler ->
//                                catchHandler.match(e).isTrue {
//                                    catchHandler.context = context
//                                    catchHandler.update(catchHandler)
//                                    catchHandler.execute.invoke(catchHandler.creatInstanceExpand(e), context)
//                                }.not()
//                            }.isFalse {
//                                // 已被进行处理
//                                throw CatEventListener.Stop() // 通知外部停止处理
//                            }.isTrue {
//                                // 未找到可用异常处理器
//                                throw e // 抛出
//                            }
//                        }catch (e: Exception){
//                            // 捕捉异常处理器抛出的控制异常
//                            when{
//                                e::class.isSubclassOf(CatEventListener.Skip::class) -> continue // 跳过
//                                e::class.isSubclassOf(CatEventListener.Stop::class) -> return true// 停止
//                                else -> {
//                                    throw e // 抛出
//                                }
//                            }
//                        }
//                        // 一些未知情况处理
//                        when{
//                            e::class.isSubclassOf(CatEventListener.Skip::class) -> continue // 跳过
//                            e::class.isSubclassOf(CatEventListener.Stop::class) -> return true// 停止
//                            else -> {
//                                throw e // 抛出
//                            }
//                        }
//                    }
//                }
//            }
//
//            return false
//        }
//
//        /**
//         * 批量处理器执行
//         */
//        private suspend fun  List<EventHandler<Event, *>>.invoke(event: Event, context: EventHandlerContext){
//            this.forEach {
//                it.context = context
//                (it.update as suspend EventHandler<Event, *>.() -> Unit).invoke(it)
//                it.execute.invoke(it.creatInstanceExpand(event), context)
//            }
//        }
//
//        /**
//         * 得到监听器实例
//         */
//        private fun getInstance() =
//            listenerClassInfo.clazz.newInstance() as CatEventListener // 创建实例
//
//        /**
//         * 获取指定外部扩展方法
//         */
//        private inline fun <reified T: Annotation, reified R> getHandler(): List<R> =
//            listenerClassInfo.getFunctionByAnnotationAll<T>().map { functionInfo ->
//            // 参数类型检查
//            if (functionInfo.function.valueParameters.isNotEmpty())
//                error("""
//                    类：${listener::class.java.name}
//                    处理器类型：${functionInfo.annotation::class.java.name}
//                    方法：${functionInfo.function.name}
//                    错误：参数错误，不应指定参数
//                """.trimIndent())
//            // 返回类型检查
//            if (functionInfo.function.returnType.isSubtypeOf(Handler::class.starProjectedType).not()){
//                error("""
//                    类：${listener::class.java.name}
//                    处理器类型：${functionInfo.annotation::class.java.name}
//                    方法：${functionInfo.function.name}(): ${functionInfo.function.returnType}
//                    错误：返回类型错误，应该为[${Handler::class.starProjectedType}]
//                """.trimIndent())
//            }
//            if (T::class == CatEventListener.Catch::class){
//                if (functionInfo.function.returnType.isSubtypeOf(CatchHandler::class.starProjectedType).not()){
//                    error("""
//                        类：${listener::class.java.name}
//                        处理器类型：${functionInfo.annotation::class.java.name}
//                        方法：${functionInfo.function.name}(): ${functionInfo.function.returnType}
//                        错误：返回类型错误，应该为[${CatchHandler::class.starProjectedType}]
//                    """.trimIndent())
//                }
//            }
//            // 调用方法获得处理器
//            if (functionInfo.function.isSuspend){
//                error("""
//                    类：${listener::class.java.name}
//                    处理器类型：${functionInfo.annotation::class.java.name}
//                    方法：${functionInfo.function.name}
//                    错误：不应该为Suspend方法，去除 Suspend 修饰符
//                """.trimIndent())
//            }
//            val handler = if (functionInfo.function.instanceParameter.isNull())
//                functionInfo.function.call()
//            else functionInfo.function.call(listener)
//
//            handler as R
//        }
//    }
}