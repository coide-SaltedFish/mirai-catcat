package org.sereinfish.catcat.mirai.catcat.core.handler

import org.sereinfish.catcat.mirai.catcat.core.context.Context
import org.sereinfish.catcat.mirai.catcat.utils.SortedList
import org.sereinfish.catcat.mirai.catcat.utils.logger
import kotlin.reflect.KClass
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
    var typeHandlers: SortedList<TypeFactory<*>>
        get() = context["typeHandlers"] as? SortedList<TypeFactory<*>> ?: kotlin.run {
            val list = SortedList<TypeFactory<*>>()
            context["typeHandlers"] = list
            list
        }
        set(value) { context["typeHandlers"] = value }

    /**
     * 获取属性代理对象
     */
    inline fun <reified T> value(typeFactory: SortedList<TypeFactory<*>> = typeHandlers) =
        ValueProxy<T>(this, T::class, typeFactory)

    inline fun <reified T> valueOrDefault(
        default: T,
        typeFactory: SortedList<TypeFactory<*>> = typeHandlers
    ) = ValueProxyByDefalut<T>(this, T::class, typeFactory, default)

    class ValueProxyByDefalut<T>(
        context: HandlerContext,
        type: KClass<*>,
        typeFactory: SortedList<TypeFactory<*>>,
        private val defaultValue: T
    ): ValueProxy<T>(context, type, typeFactory){
        override operator fun getValue(thisRef: Any?, property: KProperty<*>): T{
            return typeHandler(context[property.name], property.name) ?: defaultValue
        }
    }

    open class ValueProxy<T>(
        val context: HandlerContext,
        val type: KClass<*>,
        val typeFactory: SortedList<TypeFactory<*>>,
    ){
        open operator fun getValue(thisRef: Any?, property: KProperty<*>): T?{
            return typeHandler(context[property.name], property.name)
        }

        open operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
            context[property.name] = value
        }

        fun typeHandler(value: Any?, name: String): T? {
            return value?.let {
                val outputType = type
                // 查找能处理的类型
                typeFactory.find { it.isHandlerCompatible(value, outputType) }?.let {
                    try {
                        it.castSimple(value, context) as T
                    }catch (e: Exception){
                        logger.error("""
                            类型处理失败：
                            输入类型：${value::class.java}
                            输出类型：${outputType.java.name}
                            处理器：${it::class.java.name}
                            在处理类型时出现异常，将会返回 null
                        """.trimIndent(), e)
                        return null
                    }
                } ?: kotlin.run {
                    logger.warning(
                        """
                        类型处理警告：未找到合适的类型处理器，进行默认处理
                        变量名：$name
                        输入类型：${value::class.java}
                        输出类型：${outputType.java.name}
                    """.trimIndent()
                    )
                    value as? T
                }
            }
        }
    }
}