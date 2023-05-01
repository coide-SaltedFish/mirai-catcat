package org.sereinfish.catcat.mirai.catcat.eventOld.context

import net.mamoe.mirai.event.events.MessageEvent

/**
 * 路由上下文
 */
class RouterContext<T: MessageEvent>(
    val instance: T
): CatContextAbstract() {

}