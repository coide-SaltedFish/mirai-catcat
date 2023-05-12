package org.sereinfish.catcat.mirai.catcat.core.router

import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.message.data.MessageContent
import org.sereinfish.catcat.mirai.catcat.event.handler.EventHandlerContext

class RouterContext(
    val messageEvent: MessageEvent
) : EventHandlerContext(messageEvent) {

    init {
        context["waitMatchData"] = messageEvent.message.filterIsInstance<MessageContent>().toMutableList()
    }

    var waitMatchData: MutableList<MessageContent>?
        get() = context["waitMatchData"] as? MutableList<MessageContent>
        set(value) { context["waitMatchData"] = value }

    // 匹配后的缓存数据
    var matchDataCache: MutableList<MessageContent>
        get() = context["matchDataCache"] as? MutableList<MessageContent>  ?: kotlin.run {
            val list = ArrayList<MessageContent>()
            context["matchDataCache"] = list
            list
        }
        set(value) { context["matchDataCache"] = value }

}