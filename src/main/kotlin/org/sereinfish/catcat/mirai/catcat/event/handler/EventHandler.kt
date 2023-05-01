package org.sereinfish.catcat.mirai.catcat.event.handler

import org.sereinfish.catcat.mirai.catcat.event.handler.filter.EventFilterHandlerChain
import org.sereinfish.catcat.mirai.catcat.core.handler.Handler
import org.sereinfish.catcat.mirai.catcat.core.handler.HandlerChain
import org.sereinfish.catcat.mirai.catcat.core.handler.HandlerContext
import org.sereinfish.catcat.mirai.catcat.event.handler.filter.EventFilterHandlerContext
import org.sereinfish.catcat.mirai.catcat.utils.isFalse

/**
 * 事件处理器
 */
class EventHandler(
    val filter: EventFilterHandlerChain, // 过滤器

    override val beforeHandlerChain: HandlerChain = EventHandleChain(),
    override val afterHandlerChain: HandlerChain = EventHandleChain(),
    override val catchHandlerChain: HandlerChain = EventHandleChain(),

    private val handler: (context: HandlerContext) -> Unit
) : Handler<EventHandlerContext> {

    override fun invoke(context: EventHandlerContext) {
        // 过滤器执行
        filter.filterInvoke(context).isFalse {
            // 处理器执行
            super.invoke(context)
        }
    }

    override fun handle(context: EventHandlerContext) {
        // 处理器部分
        handler.invoke(context)
    }
}