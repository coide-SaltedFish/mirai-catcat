package org.sereinfish.catcat.mirai.catcat.event.extend

import net.mamoe.mirai.event.Event
import org.sereinfish.catcat.mirai.catcat.core.handler.Handler
import org.sereinfish.catcat.mirai.catcat.core.handler.HandlerContext
import org.sereinfish.catcat.mirai.catcat.event.handler.EventHandler
import org.sereinfish.catcat.mirai.catcat.event.handler.EventHandlerContext
import org.sereinfish.catcat.mirai.catcat.event.untils.HandlerLevel
import kotlin.reflect.KClass
import kotlin.reflect.full.isSuperclassOf

/**
 * 异常处理器
 */
class CatchHandlerBuilder<E: Throwable>(
    val exs: Array<KClass<out E>>,
    handle: Event.(context: EventHandlerContext) -> Unit,
){
    val eventHandler = EventHandler(level = 0, handler = handle)

    init {
        // 添加异常类型过滤器
        eventHandler.filter.add(filter<Event>({}){ it ->
            // 验证异常类型
            it.throwable?.let { ex ->
                exs.forEach { e ->
                    if (e.isSuperclassOf(ex::class)){ // 列表类异常类型为当前异常子类，触发
                        return@filter true
                    }
                }
            }
            false
        }, HandlerLevel.LOW)
    }

    var level: Int?
        get() = eventHandler.level
        set(value) { eventHandler.level = value }

    fun build() = eventHandler
}

/**
 * 异常处理器
 */
inline fun <E: Throwable> catchBuilder(
    exs: Array<KClass<out E>>,
    builder: CatchHandlerBuilder<E>.() -> Unit = {},
    crossinline block: E.(EventHandlerContext) -> Unit
): Handler<HandlerContext> {
    val build = CatchHandlerBuilder(exs){
        it.throwable?.let {e ->
            block(e as E, it)
        }
    }
    build.builder()
    return build.build() as Handler<HandlerContext>
}

