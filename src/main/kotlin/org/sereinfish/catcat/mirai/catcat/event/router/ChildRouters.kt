package org.sereinfish.catcat.mirai.catcat.event.router

import org.sereinfish.catcat.mirai.catcat.core.router.Router
import org.sereinfish.catcat.mirai.catcat.core.router.RouterChain
import org.sereinfish.catcat.mirai.catcat.core.router.RouterContext
import org.sereinfish.catcat.mirai.catcat.event.extend.router.RouterChainBuilder

/**
 * 路由的子路由匹配
 */
class ChildRouters(
    val router: Router
): RouterChain(){
    override fun match(context: RouterContext): Boolean {
        return router.toInner(context)
    }
}
fun RouterChainBuilder.routers(router: Router) = ChildRouters(router)