package org.sereinfish.catcat.mirai.catcat.event.handler.filter

import org.sereinfish.catcat.mirai.catcat.core.handler.Handler
import org.sereinfish.catcat.mirai.catcat.core.handler.HandlerContext

/**
 * 过滤器
 */
interface FilterHandler: Handler<HandlerContext> {
    /**
     * 是否过滤
     *
     * 返回 true 拦截， false 不拦截
     */
    fun filter(context: EventFilterHandlerContext): Boolean
}