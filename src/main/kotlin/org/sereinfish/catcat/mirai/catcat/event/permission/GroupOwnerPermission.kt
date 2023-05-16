package org.sereinfish.catcat.mirai.catcat.event.permission

import net.mamoe.mirai.contact.MemberPermission
import net.mamoe.mirai.event.events.GroupMessageEvent
import org.sereinfish.catcat.mirai.catcat.core.permission.PermissionChain
import org.sereinfish.catcat.mirai.catcat.event.extend.permission.PermissionChainBuilder
import org.sereinfish.catcat.mirai.catcat.event.handler.filter.EventFilterHandlerContext

/**
 * 群主权限
 */
class GroupOwnerPermission: PermissionChain() {
    override suspend fun checkPermission(context: EventFilterHandlerContext): Boolean {
        return if (context.event is GroupMessageEvent){
            context.event.sender.permission == MemberPermission.OWNER
        }else false
    }
}

fun PermissionChainBuilder.groupOwner(){
    add(GroupOwnerPermission())
}