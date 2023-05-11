package org.sereinfish.catcat.mirai.catcat.event.untils

/**
 * 处理器优先级
 */
object HandlerLevel {
    const val ROUTER = 1001 // 路由优先级应该为理论最低
    const val LOW = 1000
    const val NORMAL = 0
    const val HIGH = -1000
}