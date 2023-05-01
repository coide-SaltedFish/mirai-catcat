package org.sereinfish.catcat.mirai.catcat.event.handler

import net.mamoe.mirai.event.Event
import org.sereinfish.catcat.mirai.catcat.core.handler.HandlerContext

/**
 * 事件处理器上下文
 */
class EventHandlerContext(
    val event: Event
): HandlerContext() {
    var eventContext: Event?
        get() = context["event"] as? Event
        set(value) {
            context["event"] = value
        }

    init {
        context["event"] = event
    }
}