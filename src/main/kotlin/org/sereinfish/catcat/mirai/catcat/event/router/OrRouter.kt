package org.sereinfish.catcat.mirai.catcat.event.router

import net.mamoe.mirai.message.data.MessageContent
import org.sereinfish.catcat.mirai.catcat.core.router.Router
import org.sereinfish.catcat.mirai.catcat.core.router.RouterChain
import org.sereinfish.catcat.mirai.catcat.core.router.RouterContext
import org.sereinfish.catcat.mirai.catcat.event.extend.router.RouterChainBuilder
import org.sereinfish.catcat.mirai.catcat.utils.isTrue

/**
 * 或匹配，两个中的一个匹配成功就算成功
 */
class OrRouter(
    val first: Router,
    val second: Router
): RouterChain() {
    override fun match(context: RouterContext): Boolean {
        // 保存现场
        val waitMatchData = ArrayList<MessageContent>()
        context.waitMatchData?.let { waitMatchData.addAll(it) }

        // 第一个匹配
        first.toInner(context).isTrue {
            return true
        }

        // 恢复现场
        context.waitMatchData = waitMatchData

        return second.toInner(context)
    }
}

fun RouterChainBuilder.or(first: Router, second: Router) = OrRouter(first, second)