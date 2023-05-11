package org.sereinfish.catcat.mirai.catcat.event.router

import org.sereinfish.catcat.mirai.catcat.core.router.Router
import org.sereinfish.catcat.mirai.catcat.event.extend.router.AbstractRouter
import org.sereinfish.catcat.mirai.catcat.event.extend.router.RouterBuilderTool
import org.sereinfish.catcat.mirai.catcat.event.extend.router.RouterChainBuilder

class TextRouter(
    val text: String
): AbstractRouter() {
    override fun RouterBuilderTool.match() {
        text(text)
    }
}

fun RouterChainBuilder.text(text: String) = TextRouter(text)

// 匹配一个空格
fun RouterChainBuilder.space() = text(" ")