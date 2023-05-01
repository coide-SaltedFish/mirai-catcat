package org.sereinfish.catcat.mirai.catcat.event

import kotlinx.coroutines.launch
import net.mamoe.mirai.event.Event
import net.mamoe.mirai.event.GlobalEventChannel
import org.sereinfish.catcat.mirai.catcat.utils.SortedList
import org.sereinfish.catcat.mirai.catcat.utils.creatContextScope
import org.sereinfish.catcat.mirai.catcat.utils.logger
import java.util.concurrent.LinkedBlockingQueue

object CatEventManager {
    private val contextScope = creatContextScope("CatEventManager") // 作用域
    private const val maxThreadCount = 2 // 最大并发数
    private val eventQueue = LinkedBlockingQueue<Event>() // 事件队列
    // 插件列表
    private val plugins = SortedList<PluginEventInfo>()

    init {
        // 处理协程
        repeat(maxThreadCount){ i ->
            contextScope.launch {
                try {
                    while (true){
                        // TODO 进行事件处理
                    }
                }catch (e: Exception){
                    logger.error("事件处理协程未知异常", e)
                }
            }
        }

        // 全局事件监听器注册
        GlobalEventChannel.subscribeAlways<Event> {
            eventQueue.add(it)
        }
    }

    /**
     * 事件广播
     */
    private suspend fun broadcast(event: Event){
        plugins.forEach {
            it.broadcast(event)
        }
    }
}