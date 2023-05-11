package org.sereinfish.catcat.mirai.catcat.core.router

/**
 * 路由链
 */
abstract class RouterChain: Router {
    override var inner: Router? = null
}