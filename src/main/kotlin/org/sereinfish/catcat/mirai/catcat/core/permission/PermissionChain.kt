package org.sereinfish.catcat.mirai.catcat.core.permission

abstract class PermissionChain: Permission {
    override var inner: Permission? = null
}