package org.sereinfish.catcat.mirai.catcat.core.handler

import org.sereinfish.catcat.mirai.catcat.utils.SortedList

/**
 * 这是一个处理器链
 */
interface HandlerChain {
    val handlerChain: SortedList<Handler<HandlerContext>>

    fun add(handler: Handler<HandlerContext>, level: Int){
        handlerChain.add(handler, level)
    }

    fun addAll(chain: HandlerChain){
        handlerChain.addAll(chain.handlerChain)
    }

    // 链的执行
    fun invoke(context: HandlerContext){
        handlerChain.forEach {
            it.invoke(context)
        }
    }
}