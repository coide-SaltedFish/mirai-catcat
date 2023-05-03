package org.sereinfish.catcat.mirai.catcat.event

import net.mamoe.mirai.console.plugin.Plugin
import net.mamoe.mirai.event.Event
import org.sereinfish.catcat.mirai.catcat.packages.manager.CatPackageManager

/**
 * 插件事件信息
 */
class PluginEventInfo(
    // 插件信息
    val pluginInfo: CatPackageManager.PluginInfoData,
) {
    val plugin: Plugin
        get() = pluginInfo.plugin

    // 全局处理器


    // 事件处理器

    init {
        // 进行插件各部分的初始化

        // 进行事件处理器分析
        pluginInfo.getClassByInterfaceAll(CatEventListener::class.java).forEach {
            // 进行实例化

            // 获取前置处理器列表
            it.getFunctionByAnnotationAll<CatEventListener.Before>().forEach { beforeFunc ->

            }
        }
    }

    /**
     * 插件事件广播
     */
    suspend fun broadcast(event: Event){
        // 进行执行
        try {

        }catch (e: Exception){

        }
    }
}