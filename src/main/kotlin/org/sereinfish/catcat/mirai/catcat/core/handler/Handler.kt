package org.sereinfish.catcat.mirai.catcat.core.handler

/**
 * 这是一个处理器
 */
interface Handler<C: HandlerContext> {
    var level: Int?

    // 前置处理链
    var beforeHandlerChain: HandlerChain
    // 后置处理链
    var afterHandlerChain: HandlerChain
    // 异常处理链
    var catchHandlerChain: HandlerChain

    /**
     * 处理器的执行
     */
    fun invoke(context: C){
        try {
            beforeHandlerChain.invoke(context) // 前置链执行
            handle(context)
            afterHandlerChain.invoke(context) // 后置链执行
        }catch (e: Exception){
            catchHandlerChain.invoke(context) // 异常处理链执行

            // 如果没有完成处理，抛出异常
            context.throwable?.let {
                throw it
            }
        }
    }

    /**
     * 进行处理
     */
    fun handle(context: C)
}