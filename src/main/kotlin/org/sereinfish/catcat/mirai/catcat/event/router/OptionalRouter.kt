package org.sereinfish.catcat.mirai.catcat.event.router

import net.mamoe.mirai.message.data.MessageContent
import org.sereinfish.catcat.mirai.catcat.core.router.Router
import org.sereinfish.catcat.mirai.catcat.core.router.RouterChain
import org.sereinfish.catcat.mirai.catcat.core.router.RouterContext
import org.sereinfish.catcat.mirai.catcat.event.extend.router.RouterChainBuilder

/**
 * 可选路由
 */
class OptionalRouter(
    val router: Router
): RouterChain() {

    override fun match(context: RouterContext): Boolean {
        // 保存现场
        val waitMatchData = ArrayList<MessageContent>()
        context.waitMatchData?.let { waitMatchData.addAll(it) }

        if (!router.toInner(context)) {
            // 匹配不成功
            context.waitMatchData = waitMatchData // 恢复现场
        }
        return true
    }
}

fun RouterChainBuilder.optional(router: Router) = OptionalRouter(router)

fun RouterChainBuilder.optional(block: RouterChainBuilder.() -> Unit): OptionalRouter{
    val builder = RouterChainBuilder()
    builder.block()
    return OptionalRouter(builder.build())
}