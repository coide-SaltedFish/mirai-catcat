package org.sereinfish.catcat.mirai.catcat.event.extend

import net.mamoe.mirai.event.Event
import org.sereinfish.catcat.mirai.catcat.event.handler.filter.EventFilterHandler
import org.sereinfish.catcat.mirai.catcat.event.handler.filter.EventFilterHandlerContext

/**
 * 过滤器扩展
 */

/**
 * 事件过滤器构建器
 */
class EventFilterHandlerBuilder<E: Event>(
    filter: E.(EventFilterHandlerContext) -> Boolean
){
    val filterHandler = EventFilterHandler {
        val result = filter(it.event as E, it)
        it.filterResult = result
    }

    var level: Int?
        get() = filterHandler.level
        set(value) { filterHandler.level = value }

    fun build() = filterHandler
}

/**
 * 构建过滤器
 */
inline fun <E: Event> filter(
    builder: EventFilterHandlerBuilder<E>.() -> Unit,
    noinline filter: E.(EventFilterHandlerContext) -> Boolean
): EventFilterHandler{
    val build = EventFilterHandlerBuilder(filter)
    build.builder()
    return build.build()
}