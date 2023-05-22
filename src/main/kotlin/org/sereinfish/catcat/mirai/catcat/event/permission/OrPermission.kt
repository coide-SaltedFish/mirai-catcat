package org.sereinfish.catcat.mirai.catcat.event.permission

import org.sereinfish.catcat.mirai.catcat.core.permission.Permission
import org.sereinfish.catcat.mirai.catcat.core.permission.PermissionChain
import org.sereinfish.catcat.mirai.catcat.event.extend.permission.PermissionChainBuilder
import org.sereinfish.catcat.mirai.catcat.event.handler.filter.EventFilterHandlerContext

/**
 * 进行权限的 Or 运算
 */
class OrPermission(
    val first: Permission,
    val second: Permission
): PermissionChain() {
    override suspend fun checkPermission(context: EventFilterHandlerContext): Boolean {
        return first.checkPermission(context) || second.checkPermission(context)
    }
}

fun PermissionChainBuilder.or(first: Permission, second: Permission) = OrPermission(first, second)