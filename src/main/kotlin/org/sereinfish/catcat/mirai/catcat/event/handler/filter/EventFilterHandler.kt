package org.sereinfish.catcat.mirai.catcat.event.handler.filter

import org.sereinfish.catcat.mirai.catcat.event.handler.EventHandleChain
import org.sereinfish.catcat.mirai.catcat.core.handler.HandlerChain
import org.sereinfish.catcat.mirai.catcat.core.handler.HandlerContext

/**
 * 事件过滤处理器
 */
class EventFilterHandler(
    override val beforeHandlerChain: HandlerChain = EventHandleChain(),
    override val afterHandlerChain: HandlerChain = EventHandleChain(),
    override val catchHandlerChain: HandlerChain = EventHandleChain(),
    override var level: Int? = null,

    private val handler: (context: EventFilterHandlerContext) -> Unit,
) : FilterHandler {
    override fun filter(context: EventFilterHandlerContext): Boolean {
        return context.filterResult
    }

    override fun handle(context: HandlerContext) {
        handler.invoke(context as EventFilterHandlerContext)
    }
}