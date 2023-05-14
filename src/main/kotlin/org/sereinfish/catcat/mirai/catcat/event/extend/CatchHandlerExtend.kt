package org.sereinfish.catcat.mirai.catcat.event.extend

import net.mamoe.mirai.event.Event
import org.sereinfish.catcat.mirai.catcat.core.handler.Handler
import org.sereinfish.catcat.mirai.catcat.core.handler.HandlerContext
import org.sereinfish.catcat.mirai.catcat.event.handler.EventHandler
import org.sereinfish.catcat.mirai.catcat.event.handler.EventHandlerContext
import org.sereinfish.catcat.mirai.catcat.event.handler.filter.EventFilterHandler
import org.sereinfish.catcat.mirai.catcat.event.untils.HandlerLevel
import kotlin.reflect.KClass

/**
 * 异常处理器
 */
class CatchHandlerBuilder<E: Throwable>(
    val exs: Array<KClass<out E>>,
    handle: suspend Event.(context: EventHandlerContext) -> Unit,
){
    val throwableHandler = EventHandler(level = 0, handler = {
        handle(it)
        it.throwable = null
    })

    init {
        // 添加异常类型过滤器
        throwableHandler.filter.add(filter<Event>({}){
            // 验证异常类型
            it.throwable?.let { ex ->
                for (e in exs){
                    // 列表类异常类型为当前异常子类，触发
                    if (ex::class.java.isAssignableFrom(e.java)){
                        return@filter true
                    }
                }
            }
            false
        }, HandlerLevel.LOW)
    }

    var level: Int?
        get() = throwableHandler.level
        set(value) { throwableHandler.level = value }

    /**
     * 构建异常处理器
     */
    suspend inline fun <reified E: Throwable> catch(
        builder: CatchHandlerBuilder<E>.() -> Unit = {},
        crossinline block: suspend E.(EventHandlerContext) -> Unit
    ){
        val handler = catchHandler(arrayOf(E::class), builder, block)
        throwableHandler.catchHandlerChain.add(handler, handler.level ?: HandlerLevel.NORMAL)
    }

    suspend inline fun <reified E: Throwable> catch(
        exs: Array<KClass<out E>>,
        builder: CatchHandlerBuilder<E>.() -> Unit = {},
        crossinline block: E.(EventHandlerContext) -> Unit
    ){
        val handler = catchHandler(exs, builder, block)
        throwableHandler.catchHandlerChain.add(handler, handler.level ?: HandlerLevel.NORMAL)
    }

    suspend inline fun <reified E: Throwable, reified EV: Event> catchEvent(
        builder: CatchHandlerBuilder<E>.() -> Unit = {},
        crossinline block: suspend E.(EventHandlerContext, EV) -> Unit
    ){
        val handler = catchHandlerEvent(arrayOf(E::class), builder, block)
        throwableHandler.catchHandlerChain.add(handler, handler.level ?: HandlerLevel.NORMAL)
    }

    suspend inline fun <reified E: Throwable, reified EV: Event> catchEvent(
        exs: Array<KClass<out E>>,
        builder: CatchHandlerBuilder<E>.() -> Unit = {},
        crossinline block: E.(EventHandlerContext, EV) -> Unit
    ){
        val handler = catchHandlerEvent(exs, builder, block)
        throwableHandler.catchHandlerChain.add(handler, handler.level ?: HandlerLevel.NORMAL)
    }

    fun build() = throwableHandler
}

/**
 * 异常处理器
 */
suspend inline fun <E: Throwable> catchHandler(
    exs: Array<KClass<out E>>,
    builder: CatchHandlerBuilder<E>.() -> Unit = {},
    crossinline block: suspend E.(EventHandlerContext) -> Unit
): Handler<HandlerContext> {
    val build = CatchHandlerBuilder(exs){
        it.throwable?.let {e ->
            block(e as E, it)
        }
    }
    build.builder()
    return build.build() as Handler<HandlerContext>
}

suspend inline fun <E: Throwable, reified EV: Event> catchHandlerEvent(
    exs: Array<KClass<out E>>,
    builder: CatchHandlerBuilder<E>.() -> Unit = {},
    crossinline block: suspend E.(EventHandlerContext,EV) -> Unit
): Handler<HandlerContext> {
    val build = CatchHandlerBuilder(exs){
        it.throwable?.let {e ->
            block(e as E, it, it.event as EV)
        }
    }
    build.builder()
    return build.build().also {
        it.filter.add(EventFilterHandler {
            // 添加事件过滤器
            EV::class.java.isAssignableFrom(it.event::class.java)
        }, HandlerLevel.HIGH)
    } as Handler<HandlerContext>
}

/**
 * 异常处理器
 */
suspend inline fun <reified E: Throwable> catchHandler(
    builder: CatchHandlerBuilder<E>.() -> Unit = {},
    crossinline block: suspend E.(EventHandlerContext) -> Unit
): Handler<HandlerContext> {
    val build = CatchHandlerBuilder(arrayOf(E::class)){
        it.throwable?.let {e ->
            block(e as E, it)
        }
    }
    build.builder()
    return build.build() as Handler<HandlerContext>
}

suspend inline fun <reified E: Throwable, reified EV: Event> catchHandlerEvent(
    builder: CatchHandlerBuilder<E>.() -> Unit = {},
    crossinline block: suspend E.(EventHandlerContext,EV) -> Unit
): Handler<HandlerContext> {
    val build = CatchHandlerBuilder(arrayOf(E::class)){
        it.throwable?.let {e ->
            block(e as E, it, it.event as EV)
        }
    }
    build.builder()
    return build.build().also {
        it.filter.add(EventFilterHandler {
            // 添加事件过滤器
            EV::class.java.isAssignableFrom(it.event::class.java)
        }, HandlerLevel.HIGH)
    } as Handler<HandlerContext>
}
