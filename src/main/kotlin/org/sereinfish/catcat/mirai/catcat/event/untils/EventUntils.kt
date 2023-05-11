package org.sereinfish.catcat.mirai.catcat.event.untils

import net.mamoe.mirai.console.plugin.Plugin
import net.mamoe.mirai.console.plugin.id
import net.mamoe.mirai.console.plugin.name

/**
 * 返回一个格式化后的插件错误信息
 */
fun pluginErrorInfoLog(
    plugin: Plugin,
    message: String,
    exception: Exception? = null
) = """
    插件：${plugin.name}(${plugin.id})
    信息：$message
    ${exception?.let { "异常：${exception.message}" } ?: ""}
""".trimIndent()