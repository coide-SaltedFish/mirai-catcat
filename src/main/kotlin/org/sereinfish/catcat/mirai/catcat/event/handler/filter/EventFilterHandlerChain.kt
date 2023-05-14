package org.sereinfish.catcat.mirai.catcat.event.handler.filter

import org.sereinfish.catcat.mirai.catcat.core.handler.Handler
import org.sereinfish.catcat.mirai.catcat.core.handler.HandlerChain
import org.sereinfish.catcat.mirai.catcat.core.handler.HandlerContext
import org.sereinfish.catcat.mirai.catcat.event.handler.EventHandlerContext
import org.sereinfish.catcat.mirai.catcat.utils.*

/**
 * 过滤器链
 */
class EventFilterHandlerChain(
    filterHandlerChain: SortedList<FilterHandler> = SortedList(),
) : HandlerChain {
    override val handlerChain: SortedList<Handler<HandlerContext>> = filterHandlerChain.map { it }

    /**
     * 是否过滤
     *
     * 返回 true 通过， false 拦截
     */
    suspend fun filterInvoke(context: EventHandlerContext): Boolean{
        return handlerChain.forEachCheck {
            if (it is FilterHandler){
                val eventFilterContext = EventFilterHandlerContext(context.event)
                eventFilterContext.addAll(context)

                val result = it.filter(eventFilterContext)
                // 添加上下文
                context.addAll(eventFilterContext)

                result
            }else {
                logger.error("""
                    一个错误的过滤器类型：${it::class.java.name}
                    应该为 ${FilterHandler::class.java.name}
                """.trimIndent())
                false
            }
        }
    }
}