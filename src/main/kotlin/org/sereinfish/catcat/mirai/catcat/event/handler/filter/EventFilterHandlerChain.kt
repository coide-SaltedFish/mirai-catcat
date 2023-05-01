package org.sereinfish.catcat.mirai.catcat.event.handler.filter

import org.sereinfish.catcat.mirai.catcat.core.handler.Handler
import org.sereinfish.catcat.mirai.catcat.core.handler.HandlerChain
import org.sereinfish.catcat.mirai.catcat.core.handler.HandlerContext
import org.sereinfish.catcat.mirai.catcat.event.handler.EventHandlerContext
import org.sereinfish.catcat.mirai.catcat.utils.SortedList
import org.sereinfish.catcat.mirai.catcat.utils.forEachResult
import org.sereinfish.catcat.mirai.catcat.utils.isFalse
import org.sereinfish.catcat.mirai.catcat.utils.isTrue

/**
 * 过滤器链
 */
class EventFilterHandlerChain(
    val filterHandlerChain: SortedList<FilterHandler>,
) : HandlerChain {
    override val handlerChain: SortedList<Handler<HandlerContext>> = filterHandlerChain.map { it }

    /**
     * 是否过滤
     *
     * 返回 true 拦截， false 不拦截
     */
    fun filterInvoke(context: EventHandlerContext): Boolean{
        return filterHandlerChain.forEachResult {
            val eventFilterContext = EventFilterHandlerContext(context.event)
            eventFilterContext.addAll(context)

            it.filter(eventFilterContext).not().isTrue {
                // 不过滤，添加上下文
                context.addAll(eventFilterContext)
            }
        }
    }
}