package org.sereinfish.catcat.mirai.catcat.event.handler

import net.mamoe.mirai.event.Event
import org.sereinfish.catcat.mirai.catcat.core.handler.HandlerContext

/**
 * 事件处理器上下文
 */
open class EventHandlerContext(
    val event: Event
): HandlerContext() {
    var eventContext: Event?
        get() = context["event"] as? Event
        set(value) {
            context["event"] = value
        }

    // 停止执行
    var isStop: Boolean
        get() = context["isStop"] as? Boolean ?: false
        set(value) {
            context["isStop"] = value
        }

    init {
        context["event"] = event
    }
}