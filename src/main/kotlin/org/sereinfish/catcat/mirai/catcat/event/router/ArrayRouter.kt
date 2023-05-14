package org.sereinfish.catcat.mirai.catcat.event.router

import net.mamoe.mirai.message.data.MessageContent
import org.sereinfish.catcat.mirai.catcat.core.router.Router
import org.sereinfish.catcat.mirai.catcat.core.router.RouterChain
import org.sereinfish.catcat.mirai.catcat.core.router.RouterContext
import org.sereinfish.catcat.mirai.catcat.event.extend.router.RouterChainBuilder
import org.sereinfish.catcat.mirai.catcat.utils.isFalse

/**
 * 数组的匹配
 */
class ArrayRouter(
    val router: Router
): RouterChain() {
    override fun match(context: RouterContext): Boolean {
        // 进行第一次匹配
        router.toInner(context).isFalse {
            return false
        }
        // 接下来循环匹配
        while (true){
            // 保存现场
            val waitMatchData = ArrayList<MessageContent>()
            context.waitMatchData?.let { waitMatchData.addAll(it) }

            if (router.toInner(context).not()) {
                // 匹配失败，恢复现场
                context.waitMatchData = waitMatchData
                break
            }
        }

        return true
    }
}

fun RouterChainBuilder.array(router: Router) = ArrayRouter(router)

fun RouterChainBuilder.array(block: RouterChainBuilder.() -> Unit): ArrayRouter{
    val builder = RouterChainBuilder()
    builder.block()
    return ArrayRouter(builder.build())
}