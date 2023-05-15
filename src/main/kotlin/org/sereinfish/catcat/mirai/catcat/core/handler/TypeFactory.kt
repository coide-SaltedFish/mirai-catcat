package org.sereinfish.catcat.mirai.catcat.core.handler

import org.sereinfish.catcat.mirai.catcat.event.handler.type.*
import org.sereinfish.catcat.mirai.catcat.utils.SortedList
import kotlin.reflect.KClass

/**
 * 类型处理
 */
interface TypeFactory<R: Any> {
    /**
     * 是否可以处理这个类型的值
     */
    fun isHandlerCompatible(value: Any?, outputType: KClass<*>): Boolean

    fun cast(value: Any?, context: HandlerContext): R?

    /**
     * 在不知道具体泛型的情况下使用这个函数
     */
    fun <TR> castSimple(value: Any, context: HandlerContext): TR{
        return cast(value, context) as TR
    }

    companion object {
        private val list by lazy { getDefaultTypeFactorys() }

        fun getDefaultTypeFactoryList() = SortedList<TypeFactory<*>>().also {
            it.addAll(list)
        }

        private fun getDefaultTypeFactorys(): SortedList<TypeFactory<*>> {
            val list = SortedList<TypeFactory<*>>()
            list.add(AtToMemberFactory())
            list.add(ContactToLongFactory())
            list.add(ContentToContentFactory())
            list.add(ContentToStringFactory())
            list.add(MessageContentListToStringFactory())
            return list
        }
    }
}