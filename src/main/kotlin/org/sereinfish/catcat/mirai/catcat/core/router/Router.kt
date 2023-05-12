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
        inner?.let {
            if (it.toInner(context)) { // 执行上一个
                return match(context) // 执行自己
            }
        } ?: return match(context)

        return false
    }

    companion object: Router{
        override var inner: Router? = null
        override fun match(context: RouterContext): Boolean {
            return false
        }
    }
}