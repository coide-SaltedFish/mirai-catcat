package org.sereinfish.catcat.mirai.catcat.event.handler.filter

import net.mamoe.mirai.event.Event
import org.sereinfish.catcat.mirai.catcat.core.handler.HandlerContext

open class EventFilterHandlerContext(
    val event: Event
): HandlerContext() {
    init {
        context["event"] = event
    }

    var filterResult: Boolean
        get() = context["filterResult"] as? Boolean ?: true
        set(value) { context["filterResult"] = value }
}