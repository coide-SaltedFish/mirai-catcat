package org.sereinfish.catcat.mirai.catcat.event.router

import net.mamoe.mirai.message.data.At
import org.sereinfish.catcat.mirai.catcat.core.router.Router
import org.sereinfish.catcat.mirai.catcat.event.extend.router.AbstractRouter
import org.sereinfish.catcat.mirai.catcat.event.extend.router.RouterBuilderTool
import org.sereinfish.catcat.mirai.catcat.event.extend.router.RouterChainBuilder
import org.sereinfish.catcat.mirai.catcat.event.extend.router.buildRouterChain

class AtRouter(
    val id: Long
): AbstractRouter() {
    override fun RouterBuilderTool.match() {
        content<At> {
            it.target == id
        }
    }
}

fun RouterChainBuilder.at(id: Long) = buildRouterChain {
    AtRouter(id) + space()
}

fun RouterChainBuilder.atOnly(id: Long) = AtRouter(id)