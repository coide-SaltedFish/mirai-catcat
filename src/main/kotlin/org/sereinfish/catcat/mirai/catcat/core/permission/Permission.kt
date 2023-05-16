package org.sereinfish.catcat.mirai.catcat.core.permission

import org.sereinfish.catcat.mirai.catcat.core.handler.HandlerContext
import org.sereinfish.catcat.mirai.catcat.event.handler.filter.EventFilterHandlerContext

/**
 * 权限匹配器
 *
 * 也支持链式构造
 */
interface Permission {
    var inner: Permission?

    fun then(node: Permission): Permission {
        if ((this === Permission).not())
            node.inner = this
        return node
    }

    suspend fun checkPermission(context: EventFilterHandlerContext): Boolean

    /**
     * 进行执行
     */
    suspend fun toInner(context: EventFilterHandlerContext): Boolean{
        inner?.let {
            if (it.toInner(context)) { // 执行上一个
                return checkPermission(context) // 执行自己
            }
        } ?: return checkPermission(context)

        return false
    }

    companion object: Permission{
        override var inner: Permission? = null

        override suspend fun checkPermission(context: EventFilterHandlerContext): Boolean {
            return false
        }
    }
}