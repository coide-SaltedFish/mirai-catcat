package org.sereinfish.catcat.mirai.catcat.event

/**
 * 实现此接口即可接入框架事件处理器
 */
interface CatEventListener {
    // 监听器优先级
    val level: Int
        get() = 0

    /**
     * 前置处理器
     */
    @Retention(AnnotationRetention.RUNTIME)
    @Target(AnnotationTarget.FUNCTION)
    annotation class Before(
        val level: Int = 0, // 优先级
    )

    /**
     * 处理器
     */
    @Retention(AnnotationRetention.RUNTIME)
    @Target(AnnotationTarget.FUNCTION)
    annotation class Handler(
        val level: Int = 0, // 优先级
    )

    /**
     * 后置处理器
     */
    @Retention(AnnotationRetention.RUNTIME)
    @Target(AnnotationTarget.FUNCTION)
    annotation class After(
        val level: Int = 0, // 优先级
    )

    /**
     * 异常处理器
     */
    @Retention(AnnotationRetention.RUNTIME)
    @Target(AnnotationTarget.FUNCTION)
    annotation class Catch(
        val level: Int = 0, // 优先级
    )

    /**
     * 标记为全局处理器
     */
    @Retention(AnnotationRetention.RUNTIME)
    @Target(AnnotationTarget.FUNCTION)
    annotation class Global
}