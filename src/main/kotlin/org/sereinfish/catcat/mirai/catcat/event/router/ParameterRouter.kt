package org.sereinfish.catcat.mirai.catcat.event.router

import net.mamoe.mirai.message.data.MessageContent
import org.sereinfish.catcat.mirai.catcat.core.router.Router
import org.sereinfish.catcat.mirai.catcat.core.router.RouterChain
import org.sereinfish.catcat.mirai.catcat.core.router.RouterContext
import org.sereinfish.catcat.mirai.catcat.event.extend.router.RouterChainBuilder
import org.sereinfish.catcat.mirai.catcat.utils.isTrue

/**
 * 匹配数据保存为参数
 */
class ParameterRouter(
    val router: Router,
    val name: String,
    val typeHandler: RouterContext.(List<MessageContent>) -> Any? = {
        if (it.size == 1){
            it.firstOrNull()
        }else if (it.isEmpty()){
            null
        }else it
    }
): RouterChain(){
    override fun match(context: RouterContext): Boolean {
        context.matchDataCache = ArrayList() // 清空数据
        return router.toInner(context).isTrue {
            // 匹配成功，获取数据
            context[name] = context.typeHandler(context.matchDataCache)
        }
    }
}

fun RouterChainBuilder.param(
    router: Router,
    name: String,
    typeHandler: RouterContext.(List<MessageContent>) -> Any? = {
        if (it.size == 1){
            it.firstOrNull()
        }else if (it.isEmpty()){
            null
        }else it
    }
) = ParameterRouter(router, name, typeHandler)

fun RouterChainBuilder.param(
    routerBuilder: RouterChainBuilder.() -> Unit,
    name: String,
    typeHandler: RouterContext.(List<MessageContent>) -> Any? = {
        if (it.size == 1){
            it.firstOrNull()
        }else if (it.isEmpty()){
            null
        }else it
    }
) = ParameterRouter(let {
    val builder = RouterChainBuilder()
    builder.routerBuilder()
    builder.build()
}, name, typeHandler)