package org.sereinfish.catcat.mirai.catcat.event.router

import org.sereinfish.catcat.mirai.catcat.event.extend.router.AbstractRouter
import org.sereinfish.catcat.mirai.catcat.event.extend.router.RouterBuilderTool
import org.sereinfish.catcat.mirai.catcat.event.extend.router.RouterChainBuilder

/**
 * 匹配任何内容
 */
class AnyRouter: AbstractRouter() {
    override fun RouterBuilderTool.match() {
        contentMatchNotNull {
            matchDataTemp.add(it) // 保存匹配的数据
            true
        }
    }
}

fun RouterChainBuilder.any() = AnyRouter()