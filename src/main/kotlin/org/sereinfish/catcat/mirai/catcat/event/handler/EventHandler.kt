package org.sereinfish.catcat.mirai.catcat.event.handler

import kotlinx.coroutines.runBlocking
import net.mamoe.mirai.event.Event
import org.sereinfish.catcat.mirai.catcat.event.handler.filter.EventFilterHandlerChain
import org.sereinfish.catcat.mirai.catcat.core.handler.Handler
import org.sereinfish.catcat.mirai.catcat.core.handler.HandlerChain
import org.sereinfish.catcat.mirai.catcat.utils.isFalse
import org.sereinfish.catcat.mirai.catcat.utils.isTrue

/**
 * 事件处理器
 */
class EventHandler(
    val filter: EventFilterHandlerChain = EventFilterHandlerChain(), // 过滤器

    override var beforeHandlerChain: HandlerChain = EventHandleChain(),
    override var afterHandlerChain: HandlerChain = EventHandleChain(),
    override var catchHandlerChain: HandlerChain = EventHandleChain(),
    override var level: Int? = null,

    private val handler: suspend Event.(context: EventHandlerContext) -> Unit,
) : Handler<EventHandlerContext> {

    override suspend fun invoke(context: EventHandlerContext) {
        // 过滤器执行
        filter.filterInvoke(context).isTrue {
            // 处理器执行
            super.invoke(context)
        }
    }

    override suspend fun handle(context: EventHandlerContext) {
        // 处理器部分
        runBlocking {
            handler.invoke(context.event, context)
        }
    }
}