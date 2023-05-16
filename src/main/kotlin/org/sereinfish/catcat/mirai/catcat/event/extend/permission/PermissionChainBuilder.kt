package org.sereinfish.catcat.mirai.catcat.event.extend.permission

import org.sereinfish.catcat.mirai.catcat.core.permission.Permission
import org.sereinfish.catcat.mirai.catcat.core.permission.PermissionChain
import org.sereinfish.catcat.mirai.catcat.event.handler.filter.EventFilterHandlerContext
import org.sereinfish.catcat.mirai.catcat.event.permission.or

/**
 * 权限链构建工具
 */
class PermissionChainBuilder {
    private var list = ArrayList<Permission>()

    fun add(permission: Permission){
        list.add(permission)
    }

    /**
     * 权限的 or 判断
     */
    infix fun Permission.or(second: Permission){
        or(this, second)
    }

    /**
     * 添加一个自定义构建的权限检查器
     */
    suspend inline fun permission(crossinline block: suspend () -> Boolean){
        add(buildPermission(block))
    }

    fun build(): Permission {
        var node: Permission = Permission
        list.forEach {
            node = node.then(it)
        }
        return node;
    }
}

/**
 * 简单的构建一个权限检查器
 */
suspend inline fun buildPermission(crossinline block: suspend () -> Boolean): Permission{
    return object : PermissionChain(){
        override suspend fun checkPermission(context: EventFilterHandlerContext): Boolean {
            return block()
        }
    }
}
