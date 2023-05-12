package org.sereinfish.catcat.mirai.catcat.core.handler

import kotlin.reflect.KClass

/**
 * 类型处理
 */
interface TypeHandler<T : Any, R: Any> {
    val inputType: KClass<T> // 进入的类型
    val outputType: KClass<R> // 输出的类型

    fun cast(value: T): R

    /**
     * 在不知道具体泛型的情况下使用这个函数
     */
    fun <TR> castSimple(value: Any): TR{
        return cast(value as T) as TR
    }
}