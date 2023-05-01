package org.sereinfish.catcat.mirai.catcat.core.handler

import org.sereinfish.catcat.mirai.catcat.core.context.Context
import kotlin.reflect.KFunction
import kotlin.reflect.KProperty

/**
 * 处理器上下文
 */
open class HandlerContext(
    final override val context: HashMap<String, Any?> = HashMap(),
) : Context<String> {
    // 处理结果
    var result: Any?
        get() = context["result"]
        set(value) {
            context["result"] = value
        }
    // 源内容
    var source: Any?
        get() = context["source"]
        set(value) {
            context["source"] = value
        }

    // 异常
    var throwable: Throwable?
        get() = context["throwable"] as? Throwable
        set(value) {
            context["throwable"] = value
        }

    /**
     * 获取属性代理对象
     */
    fun value() = ValueProxy(this)

    class ValueProxy(
        private val context: HandlerContext
    ){
        operator fun getValue(thisRef: Any?, property: KProperty<*>): Any?{
            return context[property.name]
        }

        operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
            context[property.name] = value
        }
    }
}