package org.sereinfish.catcat.mirai.catcat.core.router

import org.sereinfish.catcat.mirai.catcat.utils.isTrue

/**
 * 路由用作链式数据匹配
 */
interface Router {

    var inner: Router?

    fun then(router: Router): Router{
        if ((this === Router).not())
            router.inner = this
        return router
    }

    /**
     * 执行匹配
     */
    fun match(context: RouterContext): Boolean

    /**
     * 进行执行
     */
    fun toInner(context: RouterContext): Boolean{
        if (inner?.toInner(context).isTrue()) {
            return match(context) // 执行自己
        }
        return false
    }

    companion object: Router{
        override var inner: Router? = null
        override fun match(context: RouterContext): Boolean {
            return false
        }
    }
}