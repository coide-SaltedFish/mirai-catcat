package org.sereinfish.catcat.mirai.catcat.event.handler.type

import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.At
import org.sereinfish.catcat.mirai.catcat.core.handler.HandlerContext
import org.sereinfish.catcat.mirai.catcat.core.handler.TypeFactory
import org.sereinfish.catcat.mirai.catcat.event.handler.EventHandlerContext
import kotlin.reflect.KClass

class AtToMemberFactory: TypeFactory<Member> {
    override fun isHandlerCompatible(value: Any?, outputType: KClass<*>): Boolean {
        return value is At
            && Member::class.java.isAssignableFrom(outputType.java)
    }

    override fun cast(value: Any?, context: HandlerContext): Member? {
        if (value is At && context is EventHandlerContext && context.event is GroupMessageEvent){
            return context.event.group.members[value.target]
        }
        return null
    }
}