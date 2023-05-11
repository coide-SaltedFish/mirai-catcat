package org.sereinfish.catcat.mirai.catcat.event.router

import org.sereinfish.catcat.mirai.catcat.core.router.Router
import org.sereinfish.catcat.mirai.catcat.event.extend.router.AbstractRouter
import org.sereinfish.catcat.mirai.catcat.event.extend.router.RouterBuilderTool
import org.sereinfish.catcat.mirai.catcat.event.extend.router.RouterChainBuilder

/**
 * 正则匹配路由
 */
class RegexRouter(
    val regex: Regex,
    val isGreedy: Boolean = true
): AbstractRouter() {
    override fun RouterBuilderTool.match() {
        regex(regex, isGreedy)
    }
}

fun RouterChainBuilder.regex(regex: Regex, isGreedy: Boolean = true) = RegexRouter(regex, isGreedy)

fun RouterChainBuilder.regexEmail() = regex(
    "[\\w!#\$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#\$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?".toRegex()
)

fun RouterChainBuilder.regexNumber() =
    regex("[0-9]+".toRegex())

fun RouterChainBuilder.regexFloat() =
    regex("[0-9]+(.[0-9]{1,6})?".toRegex())