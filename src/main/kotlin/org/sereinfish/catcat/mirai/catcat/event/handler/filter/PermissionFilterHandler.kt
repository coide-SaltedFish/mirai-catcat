package org.sereinfish.catcat.mirai.catcat.event.handler.filter

import org.sereinfish.catcat.mirai.catcat.core.permission.Permission

/**
 * 权限过滤器
 */
class PermissionFilterHandler(
    private val permission: Permission
): EventFilterHandler(handler = {
    val result = permission.toInner(it)
    it.filterResult = result
})