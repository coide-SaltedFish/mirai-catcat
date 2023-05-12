package org.sereinfish.catcat.mirai.catcat.utils

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

class Context{
    val a = "1"
}

interface Handler{
    val handler: suspend (Context) -> Unit

    suspend fun invoke(context: Context){
        println("我是原始方法：${context.a}")
        handler
    }
}

class TestHandler(
    override val handler: suspend (Context) -> Unit
) : Handler{
    override suspend fun invoke(context: Context) {
        println("改写：${context.a}")
        super.invoke(context)
    }
}

suspend fun a(){
    delay(10)
}

fun main() {
    runBlocking {
        TestHandler {
            println("func ${it.a}")
            a()
            println("test")
        }
    }
}