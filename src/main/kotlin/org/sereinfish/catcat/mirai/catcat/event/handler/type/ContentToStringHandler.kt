package org.sereinfish.catcat.mirai.catcat.event.handler.type

import net.mamoe.mirai.message.data.MessageContent
import org.sereinfish.catcat.mirai.catcat.core.handler.TypeHandler
import kotlin.reflect.KClass

class ContentToStringHandler: TypeHandler<MessageContent, String> {
    override val inputType: KClass<MessageContent> = MessageContent::class
    override val outputType: KClass<String> = String::class

    override fun cast(value: MessageContent): String {
        return value.contentToString()
    }
}