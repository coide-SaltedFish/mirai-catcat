package org.sereinfish.catcat.mirai.catcat.event.handler.type

import net.mamoe.mirai.message.data.MessageContent
import org.sereinfish.catcat.mirai.catcat.core.handler.HandlerContext
import org.sereinfish.catcat.mirai.catcat.core.handler.TypeFactory
import kotlin.reflect.KClass

class MessageContentListToStringFactory: TypeFactory<String> {

    override fun isHandlerCompatible(value: Any?, outputType: KClass<*>): Boolean {
        return value is List<*>
            && value.firstOrNull() is MessageContent
            && String::class.java.isAssignableFrom(outputType.java)
    }

    override fun cast(value: Any?, context: HandlerContext): String? {
        return buildString {
            if (value is List<*>){
                value.forEach { item ->
                    if (item is MessageContent){
                        append(item.contentToString())
                    }
                }
            }
        }
    }

}