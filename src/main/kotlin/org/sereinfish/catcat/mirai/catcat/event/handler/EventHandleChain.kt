package org.sereinfish.catcat.mirai.catcat.event.handler

import org.sereinfish.catcat.mirai.catcat.core.handler.Handler
import org.sereinfish.catcat.mirai.catcat.core.handler.HandlerChain
import org.sereinfish.catcat.mirai.catcat.core.handler.HandlerContext
import org.sereinfish.catcat.mirai.catcat.utils.SortedList

/**
 * 事件处理器链
 */
class EventHandleChain(
    override val handlerChain: SortedList<Handler<HandlerContext>> = SortedList()
) : HandlerChain