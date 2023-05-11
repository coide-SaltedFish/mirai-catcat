package org.sereinfish.catcat.mirai.catcat.event.router

import net.mamoe.mirai.message.data.MessageContent
import org.sereinfish.catcat.mirai.catcat.core.router.Router
import org.sereinfish.catcat.mirai.catcat.event.extend.router.AbstractRouter
import org.sereinfish.catcat.mirai.catcat.event.extend.router.RouterBuilderTool
import org.sereinfish.catcat.mirai.catcat.event.extend.router.RouterChainBuilder
import kotlin.reflect.KClass

/**
 * 限定元素类型
 */
class MessageContentTypeRouter(
    val type: KClass<out MessageContent>
): AbstractRouter() {
    override fun RouterBuilderTool.match() {
        contentType(type)
    }
}

inline fun <reified T: MessageContent> RouterChainBuilder.type() =
    MessageContentTypeRouter(T::class)