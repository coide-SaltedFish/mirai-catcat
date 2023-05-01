package org.sereinfish.catcat.mirai.catcat.utils

import kotlinx.coroutines.*
import org.sereinfish.catcat.mirai.catcat.PluginMain
import kotlin.coroutines.CoroutineContext
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

val logger = PluginMain.logger

fun Any?.println() =
    println(this)

fun Boolean?.isTrue() =
    this == true

fun Boolean?.isFalse() =
    this == false

fun Any?.isNull() = this == null

fun <T> List<T>.subList(start: Int) =
    this.subList(start, this.size)

inline fun Boolean.isTrue(block: () -> Unit): Boolean{
    if (this) block.invoke()
    return this
}

inline fun Boolean.isFalse(block: () -> Unit): Boolean{
    if (this.not()) block.invoke()
    return this
}

// 异常处理
class CatExceptionHandler<R>{
    val exceptions = HashMap<KClass<out Throwable>, Throwable.() -> R>()

    inline fun <reified T: Throwable> catch(noinline catch: T.() -> R){
        exceptions[T::class] = catch as Throwable.() -> R
    }
}

inline fun <reified R> tryCatch(block: CatExceptionHandler<R>.() -> R): R{
    val handler = CatExceptionHandler<R>()
    return try {
        handler.block()
    }catch (e: Exception){
        for ((type, ex) in handler.exceptions){
            e::class.isSubclassOf(type).isTrue {
                return ex.invoke(e)
            }
        }
        // 抛出
        throw e
    }
}

/**
 * 循环遍历检查
 * action返回 false 停止检查，并且返回 false
 * 默认返回 true
 */
inline fun <T> Iterable<T>.forEachCheck(action: (T) -> Boolean): Boolean {
    for (element in this){
        if(action(element).not()){
            return false
        }
    }
    return true
}

inline fun <T> Array<out T>.forEachCheck(action: (T) -> Boolean): Boolean {
    for (element in this){
        if(action(element).not()){
            return false
        }
    }
    return true
}

/**
 * 循环遍历检查，不会中途退出
 * 默认返回 true, action 中有 false 则返回 false
 */
inline fun <T> Iterable<T>.forEachResult(action: (T) -> Boolean): Boolean {
    var ret = true
    for (element in this){
        if(action(element).not()){
            ret = false
        }
    }
    return ret
}

inline fun <T> Array<out T>.forEachResult(action: (T) -> Boolean): Boolean {
    var ret = true
    for (element in this){
        if(action(element).not()){
            ret = false
        }
    }
    return ret
}

/**
 * 创建一个协程作用域
 */
fun creatContextScope(name: String, dispatcher: CoroutineDispatcher = Dispatchers.Default) =
    ContextScope(Job() + dispatcher + CoroutineName(name))

class ContextScope(context: CoroutineContext) : CoroutineScope {
    override val coroutineContext: CoroutineContext = context
    // CoroutineScope is used intentionally for user-friendly representation
    override fun toString(): String = "CoroutineScope(coroutineContext=$coroutineContext)"
}