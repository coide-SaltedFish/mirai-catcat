package org.sereinfish.catcat.mirai.catcat.utils

import org.sereinfish.catcat.mirai.catcat.packages.manager.CatPackageManager
import kotlin.reflect.KFunction
import kotlin.reflect.jvm.jvmName

/**
 * 注解的一些工具
 */
object AnnotationUtils {
    /**
     * 获取所有层级下的注解
     */
    fun getAnnotationAll(clazz: Class<*>): Set<Annotation> =
        buildSet {
            clazz.declaredAnnotations.forEach {
                if (
                    it.annotationClass.jvmName.startsWith("kotlin.").not()
                    && it.annotationClass.jvmName.startsWith("java.").not()
                    && it.annotationClass.jvmName.startsWith("jdk.").not()
                ){
                    add(it)
                    addAll(getAnnotationAll(it))
                }
            }
        }

    fun getAnnotationAll(function: KFunction<*>): Set<Annotation> =
        buildSet {
            function.annotations.forEach {
                if (
                    it.annotationClass.jvmName.startsWith("kotlin.").not()
                    && it.annotationClass.jvmName.startsWith("java.").not()
                    && it.annotationClass.jvmName.startsWith("jdk.").not()
                ){
                    add(it)
                    addAll(getAnnotationAll(it))
                }
            }
        }

    private fun getAnnotationAll(annotation: Annotation): Set<Annotation> =
        buildSet {
            annotation.annotationClass.annotations.forEach {
                if (
                    it.annotationClass.jvmName.startsWith("kotlin.").not()
                    && it.annotationClass.jvmName.startsWith("java.").not()
                    && it.annotationClass.jvmName.startsWith("jdk.").not()
                ){
                    add(it)
                    addAll(getAnnotationAll(it))
                }
            }
        }

    /**
     * 判断在所有层级上是否有指定注解
     *
     * true 有
     * false 没有
     */
    inline fun <reified T: Annotation> KFunction<*>.functionExistAnnotationAll(): Boolean {
        val ans = getAnnotationAll(this)
        return ans.find { it::annotationClass == T::class }.isNull().not()
    }
}