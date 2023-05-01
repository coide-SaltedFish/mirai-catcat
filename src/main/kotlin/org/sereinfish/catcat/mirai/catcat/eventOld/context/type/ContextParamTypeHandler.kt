package org.sereinfish.catcat.mirai.catcat.eventOld.context.type

import kotlin.reflect.KClass

/**
 * 上下文参数类型处理器
 */
interface ContextParamTypeHandler<T: Any, R: Any>{
    val type: KClass<T> // 需要进行转换的类型
    val toType: KClass<R> // 转换的结果类型

    fun cast(data: T): R{
        return data as R
    }
}