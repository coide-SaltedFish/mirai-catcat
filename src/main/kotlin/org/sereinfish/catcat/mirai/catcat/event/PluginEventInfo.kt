package org.sereinfish.catcat.mirai.catcat.event

import net.mamoe.mirai.console.plugin.Plugin
import net.mamoe.mirai.event.Event
import org.sereinfish.catcat.mirai.catcat.event.untils.pluginErrorInfoLog
import org.sereinfish.catcat.mirai.catcat.packages.manager.CatPackageManager
import org.sereinfish.catcat.mirai.catcat.utils.SortedList
import org.sereinfish.catcat.mirai.catcat.utils.logger

/**
 * 插件事件信息
 */
class PluginEventInfo(
    // 插件信息
    val pluginInfo: CatPackageManager.PluginInfoData,
) {
    // TODO 全局处理器部分


    val plugin: Plugin
        get() = pluginInfo.plugin

    // 事件处理器
    val eventListenerInfos = SortedList<EventListenerInfo>()

    init {
        // 进行事件处理器分析
        pluginInfo.getClassByInterfaceAll(CatEventListener::class.java).forEach {
            try {
                val eventListenerInfo = EventListenerInfo(it, this)
                eventListenerInfos.add(eventListenerInfo, eventListenerInfo.instance.level)
            }catch (e: Exception){
                logger.error(pluginErrorInfoLog(
                    plugin = pluginInfo.plugin,
                    "加载事件监听器失败"
                ), e)
            }
        }
    }

    /**
     * 插件事件广播
     */
    suspend fun broadcast(event: Event){
        // 进行执行
        eventListenerInfos.forEach {
            try {
                it.broadcast(event)
            }catch (e: Exception){
                logger.error(pluginErrorInfoLog(plugin,
                """
                    事件监听器：${it.instance::class.java.name}
                    在事件广播时出现了未处理异常
                """.trimIndent()), e)
            }
        }
    }
}