package org.sereinfish.catcat.mirai.catcat.event.permission

import net.mamoe.mirai.event.events.MessageEvent
import org.sereinfish.catcat.mirai.catcat.core.permission.PermissionChain
import org.sereinfish.catcat.mirai.catcat.event.extend.permission.PermissionChainBuilder
import org.sereinfish.catcat.mirai.catcat.event.handler.filter.EventFilterHandlerContext

/**
 * 指定qq号拥有权限
 */
class MemberIdPermission(
    val qq: Long
): PermissionChain() {
    override suspend fun checkPermission(context: EventFilterHandlerContext): Boolean {
        return if (context.event is MessageEvent){
            context.event.sender.id == qq
        }else false
    }
}

fun PermissionChainBuilder.qq(qq: Long){
    add(MemberIdPermission(qq))
}