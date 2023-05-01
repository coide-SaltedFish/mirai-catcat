package org.sereinfish.catcat.mirai.catcat.eventOld.context

import org.sereinfish.catcat.mirai.catcat.eventOld.context.type.ContextParamTypeHandler
import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.cast
import kotlin.reflect.full.isSubclassOf

/**
 * 路由属性代理器
 */
class RouterDelegatedProperties<T: Any>(
    val context: CatContext,
    val type: KClass<T>,
    // <值的类型, <转换要求类型, 转换器>>
    val paramTypeHandlers: Map<KClass<*>, HashMap<KClass<*>, ContextParamTypeHandler<*, T>>>
) {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): T?{
        var value = context[property.name] ?: return null
        if (value::class.isSubclassOf(type).not()){
            // 进行类型转换
//            paramTypeHandlers[value::class]?.get(type)?.let {
//                it.cast(value)
//            }
        }
        return type.cast(value)
    }
    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T){
        context[property.name] = value
    }
}