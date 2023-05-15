package org.sereinfish.catcat.mirai.catcat.utils

import kotlinx.coroutines.*
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.MessageChainBuilder
import net.mamoe.mirai.message.data.buildMessageChain
import org.sereinfish.catcat.mirai.catcat.PluginMain
import kotlin.coroutines.CoroutineContext
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.instanceParameter
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.jvm.jvmErasure

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
 * 方法是否需要实例执行
 */
val KFunction<*>.isInstance
    get() = this.instanceParameter.isNull().not()

/**
 * 是否是扩展函数
 */
val KFunction<*>.isExtension
    get() = this.parameters.any { it.kind == KParameter.Kind.EXTENSION_RECEIVER }

/**
 * 获取扩展函数所属类
 */
val KFunction<*>.extensionInfo: KClass<*>?
    get() = this.parameters.firstOrNull {
        it.kind == KParameter.Kind.EXTENSION_RECEIVER
    }?.type?.jvmErasure

/**
 * 更方便的消息发送
 */
suspend fun MessageEvent.sendMessage(message: Message) =
    subject.sendMessage(message)

suspend fun MessageEvent.sendMessage(message: String) =
    subject.sendMessage(message)

suspend fun MessageEvent.sendMessage(block: MessageChainBuilder.() -> Unit) =
    sendMessage(buildMessageChain(block))

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