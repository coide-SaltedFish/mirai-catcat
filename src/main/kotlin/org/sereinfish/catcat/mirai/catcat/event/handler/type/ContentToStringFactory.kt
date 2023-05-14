package org.sereinfish.catcat.mirai.catcat.event.handler.type

import net.mamoe.mirai.message.data.MessageContent
import org.sereinfish.catcat.mirai.catcat.core.handler.TypeFactory
import kotlin.reflect.KClass

class ContentToStringFactory: TypeFactory<MessageContent, String> {
    override val inputType: KClass<MessageContent> = MessageContent::class
    override val outputType: KClass<String> = String::class

    override fun cast(value: MessageContent): String {
        return value.contentToString()
    }
}