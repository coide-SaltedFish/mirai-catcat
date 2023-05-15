package org.sereinfish.catcat.mirai.catcat.utils

operator fun String.plus(a: Int): String{
    return "a$a";
}
fun main() {
    println("123" + 2)
}