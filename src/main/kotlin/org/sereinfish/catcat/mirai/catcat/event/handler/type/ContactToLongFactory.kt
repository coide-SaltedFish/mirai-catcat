package org.sereinfish.catcat.mirai.catcat.event.handler.type

import net.mamoe.mirai.contact.Contact
import org.sereinfish.catcat.mirai.catcat.core.handler.HandlerContext
import org.sereinfish.catcat.mirai.catcat.core.handler.TypeFactory
import kotlin.reflect.KClass

class ContactToLongFactory: TypeFactory<Long> {
    override fun isHandlerCompatible(value: Any?, outputType: KClass<*>): Boolean {
        return value is Contact && Long::class.java.isAssignableFrom(outputType.java)
    }

    override fun cast(value: Any?, context: HandlerContext): Long? {
        if (value is Contact){
            return value.id
        }
        return null
    }
}