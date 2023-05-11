package org.sereinfish.catcat.mirai.catcat.utils

import org.sereinfish.catcat.mirai.catcat.core.router.Router
import org.sereinfish.catcat.mirai.catcat.event.extend.router.RouterChainBuilder
import org.sereinfish.catcat.mirai.catcat.event.router.*

fun main() {
    val a: RouterChainBuilder.() -> Unit = {
        + text("123") + regexEmail()
        + at(123L) + text("你好")
    }
    val b = RouterChainBuilder()
    b.a()
    var r: Router? = b.build()
    while (r != null){
        println(r::class.java.name)
        if (r is TextRouter) println(r.text)
//        if (r is RegexRouter) println(r.regex.toString())

        r = r.inner
    }
}