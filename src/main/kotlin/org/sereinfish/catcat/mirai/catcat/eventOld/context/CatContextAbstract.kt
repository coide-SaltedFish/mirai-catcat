package org.sereinfish.catcat.mirai.catcat.eventOld.context

abstract class CatContextAbstract: CatContext {
    override val context: HashMap<String, Any?> = HashMap()

    override fun putAll(other: CatContext) {
        context.putAll(other.context)
    }

    override fun get(key: String): Any? {
        return context[key]
    }

    override fun set(key: String, value: Any?) {
        context[key] = value
    }
}