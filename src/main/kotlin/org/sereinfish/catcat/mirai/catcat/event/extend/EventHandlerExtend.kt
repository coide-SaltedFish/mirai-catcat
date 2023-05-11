package org.sereinfish.catcat.mirai.catcat.event.extend

import net.mamoe.mirai.event.Event
import net.mamoe.mirai.event.events.MessageEvent
import org.sereinfish.catcat.mirai.catcat.core.handler.Handler
import org.sereinfish.catcat.mirai.catcat.core.handler.HandlerContext
import org.sereinfish.catcat.mirai.catcat.core.router.Router
import org.sereinfish.catcat.mirai.catcat.event.extend.router.RouterChainBuilder
import org.sereinfish.catcat.mirai.catcat.event.handler.EventHandler
import org.sereinfish.catcat.mirai.catcat.event.handler.EventHandlerContext
import org.sereinfish.catcat.mirai.catcat.event.handler.filter.RouterFilterHandler
import org.sereinfish.catcat.mirai.catcat.event.untils.HandlerLevel
import java.security.InvalidAlgorithmParameterException
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
    handle: Event.(context: EventHandlerContext) -> Unit,
){
    val eventHandler = EventHandler(level = 0, handler = handle)

    var level: Int?
        get() = eventHandler.level
        set(value) { eventHandler.level = value }

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
    inline fun before(
        builder: EventHandlerBuilder<E>.() -> Unit = {},
        crossinline block: E.(EventHandlerContext) -> Unit
    ){
        val handler = handler<E>({ builder() }){
            block(it)
        }
        eventHandler.beforeHandlerChain.add(handler, handler.level ?: HandlerLevel.NORMAL)
    }

    /**
     * 构建后置处理器
     */
    inline fun after(
        builder: EventHandlerBuilder<E>.() -> Unit = {},
        crossinline block: E.(EventHandlerContext) -> Unit
    ){
        val handler = handler<E>({ builder() }){
            block(it)
        }
        eventHandler.afterHandlerChain.add(handler, handler.level ?: HandlerLevel.NORMAL)
    }

    /**
     * 构建异常处理器
     */
    inline fun <reified E: Throwable> catch(
        builder: CatchHandlerBuilder<E>.() -> Unit = {},
        crossinline block: E.(EventHandlerContext) -> Unit
    ){
        val handler = catchBuilder(arrayOf(E::class), builder, block)
        eventHandler.catchHandlerChain.add(handler, handler.level ?: HandlerLevel.NORMAL)
    }

    inline fun <reified E: Throwable> catch(
        exs: Array<KClass<out E>>,
        builder: CatchHandlerBuilder<E>.() -> Unit = {},
        crossinline block: E.(EventHandlerContext) -> Unit
    ){
        val handler = catchBuilder(exs, builder, block)
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

    fun build() = eventHandler
}

/**
 * 构建处理器
 */
inline fun <E: Event> handler(
    builder: EventHandlerBuilder<E>.() -> Unit,
    crossinline handler: E.(context: EventHandlerContext) -> Unit
): Handler<HandlerContext> {
    val build = EventHandlerBuilder<E> {
        handler(it.event as E, it)
    }
    build.builder()
    return build.build() as Handler<HandlerContext>
}

val d = handler<MessageEvent>({
    level = 2
    filter {
        true
    }
    after {

    }
    before {

    }
    catch<TypeCastException> {

    }
    catch(arrayOf(
        IllegalMonitorStateException::class,
        IllegalThreadStateException::class
    )) {

    }
}){

}