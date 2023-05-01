package org.sereinfish.catcat.mirai.catcat.eventOld.context

interface CatContext {
    val context: HashMap<String, Any?>

    /**
     * 整合其他上下文
     */
    fun putAll(other: CatContext)

    operator fun get(key: String): Any?
    operator fun set(key: String, value: Any?)
}