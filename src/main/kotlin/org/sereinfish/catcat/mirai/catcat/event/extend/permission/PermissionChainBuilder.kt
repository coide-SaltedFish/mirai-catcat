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

    operator fun Permission.plus(other: Permission): List<Permission> {
        list.add(this@plus)
        list.add(other)
        return list
    }

    operator fun List<Permission>.plus(other: Permission): List<Permission> {
        list.add(other)
        return list
    }

    operator fun Permission.unaryPlus(): List<Permission>{
        list.add(this)
        return list
    }

    /**
     * 权限的 or 判断
     */
    infix fun Permission.or(second: Permission){
        add(or(this, second))
    }

    /**
     * 添加一个自定义构建的权限检查器
     */
    inline fun permission(crossinline block: suspend (EventFilterHandlerContext) -> Boolean) = buildPermission(block)

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
inline fun buildPermission(crossinline block: suspend (EventFilterHandlerContext) -> Boolean): Permission{
    return object : PermissionChain(){
        override suspend fun checkPermission(context: EventFilterHandlerContext): Boolean {
            return block(context)
        }
    }
}
