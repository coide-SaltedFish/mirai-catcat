package org.sereinfish.catcat.mirai.catcat.core.context

/**
 * 上下文
 *
 * 根据名称获取内容
 */
interface Context<K> {
    val context: HashMap<K, Any?>

    operator fun get(key: K) = context[key]
    operator fun set(key: K, value: Any?){
        context[key] = value
    }

    /**
     * 进行上下文合并
     */
    fun addAll(other: Context<K>){
        other.context.forEach { (key, value) ->
            context[key] = value
        }
    }
}