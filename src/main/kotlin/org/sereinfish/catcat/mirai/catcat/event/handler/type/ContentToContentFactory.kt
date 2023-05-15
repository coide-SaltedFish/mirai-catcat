package org.sereinfish.catcat.mirai.catcat.event.handler.type

import net.mamoe.mirai.message.data.MessageContent
import org.sereinfish.catcat.mirai.catcat.core.handler.HandlerContext
import org.sereinfish.catcat.mirai.catcat.core.handler.TypeFactory
import kotlin.reflect.KClass

class ContentToContentFactory: TypeFactory<MessageContent> {
    override fun isHandlerCompatible(value: Any?, outputType: KClass<*>): Boolean {
        return value is MessageContent
            && MessageContent::class.java.isAssignableFrom(outputType.java)
    }

    override fun cast(value: Any?, context: HandlerContext): MessageContent? {
        return value as? MessageContent
    }
}