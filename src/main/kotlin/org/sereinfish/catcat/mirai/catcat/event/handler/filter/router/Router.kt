package org.sereinfish.catcat.mirai.catcat.event.handler.filter.router

import net.mamoe.mirai.event.events.MessageEvent
import org.sereinfish.catcat.mirai.catcat.event.handler.filter.FilterHandler
import org.sereinfish.catcat.mirai.catcat.event.handler.filter.EventFilterHandlerContext

/**
 * 路由作为特殊的过滤器进行处理
 *
 * 为了性能，路由应该是低优先级的
 */
interface Router: FilterHandler {
    /**
     * 匹配实现
     */
    fun match(context: RouterContext)

    override fun filter(context: EventFilterHandlerContext): Boolean {
        val event = context.event
        if (event is MessageEvent){
            val routerContext = RouterContext(event)
            routerContext.addAll(context)
            // 进行匹配
            match(routerContext)

            // 如果匹配成功，注入参数
            context.addAll(routerContext)

            // 返回匹配结果
            return routerContext.isSuccess
        }
        return false
    }
}