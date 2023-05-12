package org.sereinfish.catcat.mirai.catcat.event.handler.filter

import org.sereinfish.catcat.mirai.catcat.event.handler.EventHandleChain
import org.sereinfish.catcat.mirai.catcat.core.handler.HandlerChain
import org.sereinfish.catcat.mirai.catcat.core.handler.HandlerContext

/**
 * 事件过滤处理器
 */
open class EventFilterHandler(
    override var beforeHandlerChain: HandlerChain = EventHandleChain(),
    override var afterHandlerChain: HandlerChain = EventHandleChain(),
    override var catchHandlerChain: HandlerChain = EventHandleChain(),
    override var level: Int? = null,

    private val handler: suspend (context: EventFilterHandlerContext) -> Unit,
) : FilterHandler {
    override suspend fun filter(context: EventFilterHandlerContext): Boolean {
        handler.invoke(context) // 执行函数
        return context.filterResult // 返回结果
    }

    override suspend fun handle(context: HandlerContext) {
        if (context is EventFilterHandlerContext)
            handler.invoke(context)
        else throw TypeCastException("错误的上下文类型，执行失败")
    }
}