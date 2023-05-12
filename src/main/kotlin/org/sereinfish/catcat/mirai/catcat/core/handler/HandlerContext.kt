package org.sereinfish.catcat.mirai.catcat.core.handler

import org.sereinfish.catcat.mirai.catcat.core.context.Context
import org.sereinfish.catcat.mirai.catcat.utils.forEachCheck
import org.sereinfish.catcat.mirai.catcat.utils.logger
import kotlin.reflect.KClass
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

    // key1为输入类型，key2为输出类型
    var typeHandlers: HashMap<KClass<*> ,HashMap<KClass<*>, TypeHandler<*, *>>>
        get() = context["typeHandlers"] as? HashMap<KClass<*> ,HashMap<KClass<*>, TypeHandler<*, *>>> ?: kotlin.run {
            val list = HashMap<KClass<*> ,HashMap<KClass<*>, TypeHandler<*, *>>>()
            context["typeHandlers"] = list
            list
        }
        set(value) { context["typeHandlers"] = value }

    /**
     * 获取属性代理对象
     */
    inline fun <reified T> value() = ValueProxy<T>(this, T::class)

    class ValueProxy<T>(
        private val context: HandlerContext,
        private val type: KClass<*>,
        private val typeHandler: HashMap<KClass<*> ,HashMap<KClass<*>, TypeHandler<*, *>>> = context.typeHandlers
    ){
        operator fun getValue(thisRef: Any?, property: KProperty<*>): T?{
            return typeHandler(context[property.name])
        }

        operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
            context[property.name] = value
        }

        private fun typeHandler(value: Any?): T? {
            return value?.let {
                val inputType = it::class
                val outputType = type
                // 查找能处理的类型
                typeHandler[inputType]?.get(outputType)?.let {
                    try {
                        it.castSimple<T>(it)
                    }catch (e: Exception){
                        logger.error("""
                            类型处理失败：
                            输入类型：${inputType.java.name}
                            输出类型：${outputType.java.name}
                            处理器：${it::class.java.name}
                            在处理类型时出现异常，将会返回 null
                        """.trimIndent(), e)
                        return null
                    }
                }
                logger.warning("""
                    类型处理警告：未找到合适的类型处理器，进行默认处理
                    输入类型：${inputType.java.name}
                    输出类型：${outputType.java.name}
                """.trimIndent())
                value as? T
            }
        }
    }
}