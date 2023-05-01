package org.sereinfish.catcat.mirai.catcat.eventOld.listener

/**
 * 实现此接口的类被视为事件监听器
 */
interface CatEventListener {

    /**
     * 事件处理器
     */
    @Retention(AnnotationRetention.RUNTIME)
    @Target(AnnotationTarget.FUNCTION)
    annotation class Handler

    /**
     * 前置处理器
     */
    @Retention(AnnotationRetention.RUNTIME)
    @Target(AnnotationTarget.FUNCTION)
    annotation class Before


    /**
     * 后置处理器
     */
    @Retention(AnnotationRetention.RUNTIME)
    @Target(AnnotationTarget.FUNCTION)
    annotation class After


    /**
     * 表示此方法为事件异常处理器
     */
    @Retention(AnnotationRetention.RUNTIME)
    @Target(AnnotationTarget.FUNCTION)
    annotation class Catch


    // 事件流程控制异常
    class Skip: Exception()
    class Stop: Exception()
}