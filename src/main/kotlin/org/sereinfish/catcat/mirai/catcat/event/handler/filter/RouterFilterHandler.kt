package org.sereinfish.catcat.mirai.catcat.event.handler.filter

import net.mamoe.mirai.event.events.MessageEvent
import org.sereinfish.catcat.mirai.catcat.core.router.Router
import org.sereinfish.catcat.mirai.catcat.core.router.RouterContext
import org.sereinfish.catcat.mirai.catcat.event.router.TextRouter
import org.sereinfish.catcat.mirai.catcat.utils.logger

/**
 * 路由过滤器
 */
class RouterFilterHandler(
    private val router: Router
): EventFilterHandler(handler = {
    if (it.event is MessageEvent){

        val routerContext = RouterContext(it.event)
        routerContext.addAll(it)
        val matchResult = router.toInner(routerContext) // 匹配结果
        if (matchResult){
            it.addAll(routerContext)
        }
        it.filterResult = matchResult
    }else {
        logger.warning("""
        终止了一次路由筛选器处理，因为传入的事件类型不合法，必须为 MessageEvent 类型，但是传入的事件为 ${it.event::class.java.name}
    """.trimIndent())
        throw TypeCastException("事件类型错误，路由仅能处理 MessageEvent 类型事件，但传入了 ${it.event::class.java.name} 事件")
    }
})