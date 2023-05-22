package org.sereinfish.catcat.mirai.catcat.event

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.mamoe.mirai.console.plugin.Plugin
import net.mamoe.mirai.console.plugin.id
import net.mamoe.mirai.console.plugin.name
import net.mamoe.mirai.event.Event
import net.mamoe.mirai.event.GlobalEventChannel
import org.sereinfish.catcat.mirai.catcat.event.untils.pluginErrorInfoLog
import org.sereinfish.catcat.mirai.catcat.packages.manager.CatPackageManager
import org.sereinfish.catcat.mirai.catcat.utils.SortedList
import org.sereinfish.catcat.mirai.catcat.utils.creatContextScope
import org.sereinfish.catcat.mirai.catcat.utils.logger
import java.util.concurrent.LinkedBlockingQueue

object CatEventManager {
    private val contextScope = creatContextScope("CatEventManager") // 作用域
    private const val maxThreadCount = 1 // 最大并发数
    private val eventQueue = LinkedBlockingQueue<Event>() // 事件队列
    // 插件列表
    private val plugins = SortedList<PluginEventInfo>()

    init {
        // 处理协程
        repeat(maxThreadCount){
            contextScope.launch {
                try {
                    while (true){
                        try {
                            // 进行事件处理
                            broadcast(withContext(Dispatchers.IO) {
                                eventQueue.take()
                            })
                        }catch (e: Exception){
                            logger.error("在进行事件处理时出现了未处理异常，此异常可能导致框架无法正常运行", e)
                        }
                    }
                }catch (e: Exception){
                    logger.error("事件处理协程未知异常，此事件处理协程已关闭", e)
                }
            }
        }

        // 全局事件监听器注册
        GlobalEventChannel.subscribeAlways<Event> {
            eventQueue.add(it)
        }
    }

    /**
     * 插件注册
     */
    fun registerListener(pluginInfo: CatPackageManager.PluginInfoData, level: Int){
        try {
            plugins.add(PluginEventInfo(pluginInfo), level)
        }catch (e: Exception){
            logger.error(pluginErrorInfoLog(pluginInfo.plugin, "插件注册失败"), e)
        }
    }

    /**
     * 事件广播
     */
    private suspend fun broadcast(event: Event){
        plugins.forEach {
            try {
                it.broadcast(event)
            }catch (e: Exception){
                logger.error(pluginErrorInfoLog(
                    it.plugin,
                    "在接收事件广播时出现了未处理异常(${e::class.java.name})"
                ), e)
            }
        }
    }
}