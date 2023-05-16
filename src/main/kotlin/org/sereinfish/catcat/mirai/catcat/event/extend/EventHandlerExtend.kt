package org.sereinfish.catcat.mirai.catcat.event.extend

import net.mamoe.mirai.event.Event
import org.sereinfish.catcat.mirai.catcat.core.handler.Handler
import org.sereinfish.catcat.mirai.catcat.core.handler.HandlerContext
import org.sereinfish.catcat.mirai.catcat.event.extend.permission.PermissionChainBuilder
import org.sereinfish.catcat.mirai.catcat.event.extend.router.RouterChainBuilder
import org.sereinfish.catcat.mirai.catcat.event.handler.EventHandler
import org.sereinfish.catcat.mirai.catcat.event.handler.EventHandlerContext
import org.sereinfish.catcat.mirai.catcat.event.handler.filter.PermissionFilterHandler
import org.sereinfish.catcat.mirai.catcat.event.handler.filter.RouterFilterHandler
import org.sereinfish.catcat.mirai.catcat.event.untils.HandlerLevel
import kotlin.reflect.KClass

/**
 * 事件处理器扩展
 *
 * 写法扩展
 */

/**
 * 前置处理器构建器
 */
class EventHandlerBuilder<E: Event>(
    val eventType: KClass<out E>, // 事件类型
    handle: suspend Event.(context: EventHandlerContext) -> Unit,
){
    val eventHandler = EventHandler(level = 0, handler = handle)

    var level: Int?
        get() = eventHandler.level
        set(value) { eventHandler.level = value }

    init {
        eventHandler.filter.add(filter<E> {
            // 事件类型筛选
            eventType.java.isAssignableFrom(it.event::class.java)
        }, HandlerLevel.HIGH)
    }

    /**
     * 构建过滤器链
     */
    inline fun filter(crossinline block: E.() -> Boolean){
        val handler = filter<E>({}) {
            block(this)
        }
        eventHandler.filter.add(handler, handler.level ?: HandlerLevel.NORMAL)
    }

    /**
     * 构建前置处理器
     */
    suspend inline fun <reified BE: E> before(
        builder: EventHandlerBuilder<BE>.() -> Unit = {},
        crossinline block: suspend BE.(EventHandlerContext) -> Unit
    ){
        val handler = handler(builder, block)
        eventHandler.beforeHandlerChain.add(handler, handler.level ?: HandlerLevel.NORMAL)
    }

    /**
     * 构建后置处理器
     */
    suspend inline fun <reified AE: E> after(
        builder: EventHandlerBuilder<AE>.() -> Unit = {},
        crossinline block: suspend AE.(EventHandlerContext) -> Unit
    ){
        val handler = handler(builder, block)
        eventHandler.afterHandlerChain.add(handler, handler.level ?: HandlerLevel.NORMAL)
    }

    /**
     * 构建异常处理器
     */
    suspend inline fun <reified E: Throwable> catch(
        builder: CatchHandlerBuilder<E>.() -> Unit = {},
        crossinline block: suspend E.(EventHandlerContext) -> Unit
    ){
        val handler = catchHandler(arrayOf(E::class), builder, block)
        eventHandler.catchHandlerChain.add(handler, handler.level ?: HandlerLevel.NORMAL)
    }

    suspend inline fun <reified E: Throwable> catch(
        exs: Array<KClass<out E>>,
        builder: CatchHandlerBuilder<E>.() -> Unit = {},
        crossinline block: E.(EventHandlerContext) -> Unit
    ){
        val handler = catchHandler(exs, builder, block)
        eventHandler.catchHandlerChain.add(handler, handler.level ?: HandlerLevel.NORMAL)
    }

    /**
     * 构建路由
     */
    fun router(block: RouterChainBuilder.() -> Unit){
        val builder = RouterChainBuilder()
        builder.block()
        eventHandler.filter.add(RouterFilterHandler(builder.build()), HandlerLevel.ROUTER)
    }

    /**
     * 构建权限检查器
     */
    fun permission(block: PermissionChainBuilder.() -> Unit){
        val builder = PermissionChainBuilder()
        builder.block()
        eventHandler.filter.add(PermissionFilterHandler(builder.build()), HandlerLevel.ROUTER)
    }

    fun build() = eventHandler
}

/**
 * 构建处理器
 */
suspend inline fun <reified E: Event> handler(
    builder: EventHandlerBuilder<E>.() -> Unit = {},
    crossinline handler: suspend E.(context: EventHandlerContext) -> Unit
): Handler<HandlerContext> {
    val build = EventHandlerBuilder(eventType = E::class) {
        handler(it.event as E, it)
    }
    build.builder()
    return build.build() as Handler<HandlerContext>
}