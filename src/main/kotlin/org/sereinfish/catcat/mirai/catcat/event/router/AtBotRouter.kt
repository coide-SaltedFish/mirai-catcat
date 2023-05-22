package org.sereinfish.catcat.mirai.catcat.event.router

import net.mamoe.mirai.message.data.At
import org.sereinfish.catcat.mirai.catcat.event.extend.router.AbstractRouter
import org.sereinfish.catcat.mirai.catcat.event.extend.router.RouterBuilderTool
import org.sereinfish.catcat.mirai.catcat.event.extend.router.RouterChainBuilder
import org.sereinfish.catcat.mirai.catcat.event.extend.router.buildRouterChain

class AtBotRouter: AbstractRouter() {
    override fun RouterBuilderTool.match() {

        content<At> {
            it.target == event.bot.id
        }
    }
}

fun RouterChainBuilder.atBot() = buildRouterChain {
    AtBotRouter() + space()
}

fun RouterChainBuilder.atBotOnly() = AtBotRouter()