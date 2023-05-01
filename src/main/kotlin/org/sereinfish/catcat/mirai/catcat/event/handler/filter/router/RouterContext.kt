package org.sereinfish.catcat.mirai.catcat.event.handler.filter.router

import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.message.data.MessageContent
import org.sereinfish.catcat.mirai.catcat.core.handler.HandlerContext

class RouterContext(
    // 源
    var event: MessageEvent,
): HandlerContext() {
    // 等待匹配的消息
    var waitMatchData: List<MessageContent> = event.message.filterIsInstance<MessageContent>()

    // 匹配是否成功
    var isSuccess = false
}