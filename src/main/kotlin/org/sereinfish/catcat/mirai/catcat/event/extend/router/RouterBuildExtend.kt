package org.sereinfish.catcat.mirai.catcat.event.extend.router

import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.message.data.MessageContent
import net.mamoe.mirai.message.data.PlainText
import org.sereinfish.catcat.mirai.catcat.core.router.Router
import org.sereinfish.catcat.mirai.catcat.core.router.RouterChain
import org.sereinfish.catcat.mirai.catcat.core.router.RouterContext
import org.sereinfish.catcat.mirai.catcat.event.router.or
import org.sereinfish.catcat.mirai.catcat.event.router.regex
import org.sereinfish.catcat.mirai.catcat.event.router.text
import org.sereinfish.catcat.mirai.catcat.utils.isTrue
import org.sereinfish.catcat.mirai.catcat.utils.logger
import kotlin.reflect.KClass
import kotlin.reflect.cast
import kotlin.reflect.full.isSubclassOf

/**
 * 路由链构建工具
 */
class RouterChainBuilder{
    private var list = ArrayList<Router>()

    operator fun Router.plus(other: Router): List<Router> {
        list.add(this@plus)
        list.add(other)
        return list
    }

    operator fun List<Router>.plus(other: Router): List<Router> {
        list.add(other)
        return list
    }

    operator fun Router.unaryPlus(): List<Router>{
        list.add(this)
        return list
    }

    /**
     * 字符串写法的支持
     */
    operator fun Router.plus(other: String): List<Router> {
        list.add(this@plus)
        list.add(text(other))
        return list
    }

    operator fun String.unaryPlus(): List<Router>{
        list.add(text(this))
        return list
    }

    /**
     * 正则表达式的支持
     *
     * 默认贪心匹配
     */
    operator fun Router.plus(other: Regex): List<Router> {
        list.add(this@plus)
        list.add(regex(other, true))
        return list
    }

    operator fun Regex.unaryPlus(): List<Router>{
        list.add(regex(this))
        return list
    }

    /**
     * 或路由
     */
    infix fun Router.or(second: Router): List<Router> {
        list.add(or(this, second))
        return list
    }

    fun build(): Router{
        var node: Router = Router
        list.forEach {
            node = node.then(it)
        }
        return node;
    }
}

/**
 * 路由构建工具
 */
class RouterBuilderTool(
    val context: RouterContext
){
    val matchFuncs = ArrayList<MatchFuncData>()

    val event: MessageEvent
        get() = context.messageEvent

    // 等待匹配的消息
    var waitMatchData: MutableList<MessageContent>?
        get() = context.waitMatchData
        set(value) { context.waitMatchData = value }

    val matchDataTemp = ArrayList<MessageContent>() // 已匹配的临时保存数据

    /**
     * 取出一个消息元素进行匹配处理
     *
     * 会同步更新等待匹配数据
     *
     * 返回 true 继续匹配
     * 返回 false 表示匹配失败
     */
    fun contentMatch(block: (MessageContent?) -> Boolean){
        matchFuncs.add(MatchFuncData(block))
    }

    /**
     * 取出一个消息元素进行匹配处理
     *
     * 会同步更新等待匹配数据
     *
     * 返回 true 继续匹配
     * 返回 false 表示匹配失败
     */
    fun contentMatchNotNull(block: (MessageContent) -> Boolean){
        matchFuncs.add(MatchFuncData {
            it?.let(block) ?: false
        })
    }

    /**
     * 类型匹配
     */
    fun contentType(type: KClass<out MessageContent>){
        matchFuncs.add(MatchFuncData {
            it?.let {
                (type.java.isAssignableFrom(it::class.java)).isTrue {
                    // 存入缓存
                    matchDataTemp.add(it)
                }
            } ?: false
        })
    }

    /**
     * 取出指定类型的消息元素进行匹配
     *
     * 如果没有元素或者元素类型不匹配，则匹配失败
     * 否则进行自定义匹配
     */
    inline fun <reified T: MessageContent> content(crossinline block: (T) -> Boolean){
        matchFuncs.add(MatchFuncData {
            it?.let { content ->
                if (content::class.isSubclassOf(T::class)){
                    block(T::class.cast(content)).isTrue {
                        // 存入缓存
                        matchDataTemp.add(it)
                    }
                }else false
            } ?: false
        })
    }

    /**
     * 匹配固定的文本
     */
    fun text(text: String){
        matchFuncs.add(MatchFuncData {
            it?.let { content ->
                if (content is PlainText){
                    // 如果完全相等
                    if (text == content.content) {
                        // 存入缓存
                        matchDataTemp.add(PlainText(text))
                        true
                    } else if (content.content.startsWith(text)){
                        // 开头相等
                        val waitMatchText = content.content.substring(text.length) // 取出还没匹配到的部分
                        waitMatchData?.add(0, PlainText(waitMatchText)) ?: kotlin.run {
                            // 设置为新的数组
                            waitMatchData = arrayListOf(PlainText(waitMatchText))
                        }
                        // 存入缓存
                        matchDataTemp.add(PlainText(text))
                        true
                    }else false
                }else text == content.contentToString()
            } ?: text.isEmpty()
        })
    }

    /**
     * 正则匹配
     */
    fun regex(regex: Regex, isGreedy: Boolean = true){
        matchFuncs.add(MatchFuncData {
            it?.let {
                if (it is PlainText){
                    var isSuccess = false
                    var matchText = "" // 已匹配的数据
                    var waitMatchText = "" // 等待匹配的文本

                    for (c in it.contentToString()){
                        waitMatchText += c
                        if (regex.matches(waitMatchText)){
                            // 匹配成功
                            isSuccess = true
                            matchText = waitMatchText
                            if (!isGreedy) // 如果不贪心，停止匹配
                                break
                        }
                    }
                    // 处理数据
                    if (matchText != it.contentToString()){
                        // 还剩没匹配的
                        waitMatchData?.add(0, PlainText(it.contentToString().substring(matchText.length))) ?: kotlin.run {
                            // 设置为新的数组
                            waitMatchData = arrayListOf(PlainText(waitMatchText))
                        }
                    }

                    isSuccess.isTrue {
                        // 存入缓存
                        matchDataTemp.add(PlainText(matchText))
                    }
                }else regex.matches(it.contentToString()).isTrue {
                    // 存入缓存
                    matchDataTemp.add(it)
                }
            } ?: regex.matches("")
        })
    }

    /**
     * 保存已匹配数据到上下文
     */
    fun save(name: String){
        if (matchDataTemp.isEmpty()){
            logger.warning("尝试保存已匹配数据[$name]，但已匹配数据为空")
        }else {
            context[name] = if (matchDataTemp.size == 1){
                matchDataTemp.firstOrNull()
            }else matchDataTemp
        }

    }

    /**
     * 保存数据到上下文
     */
    fun save(name: String, value: Any?){
        context[name] = value
    }

    /**
     * 最终处理
     */
    fun build(): Boolean {
        for (item in matchFuncs){
            val data = waitMatchData?.firstOrNull()
            waitMatchData?.removeFirstOrNull()
            if (!item.block(data)){
                return false
            }
        }
        return true
    }

    data class MatchFuncData(
        val block: (MessageContent?) -> Boolean,
    )
}

/**
 * 继承此类，实现自定义路由
 */
abstract class AbstractRouter: RouterChain() {
    override fun match(context: RouterContext): Boolean {
        val tool = RouterBuilderTool(context)
        tool.match()
        return tool.build().isTrue {
            // 进行匹配数据缓存同步
            context.matchDataCache.addAll(tool.matchDataTemp)
        }
    }

    /**
     * 在路由构建工具环境下进行方便的路由构建
     */
    abstract fun RouterBuilderTool.match()
}

inline fun buildRouterChain(block: RouterChainBuilder.() -> Unit): Router {
    val builder = RouterChainBuilder()
    builder.block()
    return builder.build()
}
