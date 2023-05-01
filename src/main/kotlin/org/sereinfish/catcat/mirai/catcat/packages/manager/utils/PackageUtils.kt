package org.sereinfish.catcat.mirai.catcat.packages.manager.utils

object PackageUtils{

    /**
     * 获取类的所有层级接口
     */
    fun getInterfaceAll(clazz: Class<*>): List<Class<*>> =
        buildList {
            clazz.superclass?.let {
                addAll(getInterfaceAll(it)) // 获取父类的接口
            }

            clazz.interfaces.forEach {
                add(it)
                addAll(getInterfaceAll(it))
            }
        }
}