package org.sereinfish.catcat.mirai.catcat

import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.utils.info
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.core.LoggerContext
import org.apache.logging.log4j.core.config.Configurator

object PluginMain : KotlinPlugin(
    JvmPluginDescription(
        id = "org.sereinfish.catcat.mirai.catcat",
        name = "猫猫插件框架",
        version = "0.1.0"
    ) {
        author("MiaoTiao")
        info(
            """
            猫猫插件框架
        """.trimIndent()
        )
    }
) {
    override fun onEnable() {
        logger.info { "Plugin loaded" }
    }
}
