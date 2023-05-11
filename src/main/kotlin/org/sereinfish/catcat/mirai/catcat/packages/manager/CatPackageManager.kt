package org.sereinfish.catcat.mirai.catcat.packages.manager

import net.mamoe.mirai.console.plugin.Plugin
import net.mamoe.mirai.console.plugin.id
import net.mamoe.mirai.console.plugin.jvm.JvmPluginClasspath
import org.sereinfish.catcat.mirai.catcat.event.CatEventListener
import org.sereinfish.catcat.mirai.catcat.event.CatEventManager
import org.sereinfish.catcat.mirai.catcat.packages.manager.utils.PackageUtils
import org.sereinfish.catcat.mirai.catcat.utils.AnnotationUtils
import org.sereinfish.catcat.mirai.catcat.utils.tryCatch
import java.io.File
import java.util.jar.JarFile
import kotlin.reflect.KFunction
import kotlin.reflect.full.functions
import kotlin.reflect.full.memberExtensionFunctions


object CatPackageManager {
    // id, info
    private val pluginInfos = HashMap<String, PluginInfoData>()

    /**
     * 初始化包管理器
     */
    fun init(plugin: Plugin, pluginClasspath: JvmPluginClasspath, level: Int = 0) =
        PluginInfoData(plugin, pluginClasspath).also {
            pluginInfos[plugin.id] = it

            // 注册事件监听器
            CatEventManager.registerListener(it, level)
        }


    /**
     * 插件的信息
     */
    class PluginInfoData(
        val plugin: Plugin,
        val pluginClasspath: JvmPluginClasspath
    ){
        val classList = scanPluginJarFile(pluginClasspath.pluginFile)

        /**
         * 获取在所有层级上带有指定注解的类
         * 不包含注解类
         */
        fun getClassByAnnotationAll(annotation: Annotation): List<ClassInfo> =
            classList.filter {
                it.annotationAll.contains(annotation) && it.clazz.isAnnotation.not()
            }.map {
                it
            }


        /**
         * 获取带有指定注解的类
         * 不包含注解类
         */
        fun getClassByAnnotation(annotation: Annotation): List<ClassInfo> =
            classList.filter {
                it.annotations.contains(annotation) && it.clazz.isAnnotation.not()
            }.map {
                it
            }


        /**
         * 获取实现了指定接口的类
         */
        fun getClassByInterface(interfaceClass: Class<*>): List<ClassInfo> =
            if (interfaceClass.isInterface){
                classList.filter {
                    it.interfaces.contains(interfaceClass)
                }.map {
                    it
                }
            }else listOf()

        /**
         * 获取所有层级下实现了指定接口的类
         */
        fun getClassByInterfaceAll(interfaceClass: Class<*>): List<ClassInfo> =
            if (interfaceClass.isInterface){
                classList.filter {
                    it.interfacesAll.contains(interfaceClass)
                }.map {
                    it
                }
            }else listOf()


        /**
         * 扫描指定插件
         */
        private fun scanPluginJarFile(file: File): List<ClassInfo>{
            return buildList {
                val jarFile = JarFile(file)
                val entries = jarFile.entries()
                while (entries.hasMoreElements()){
                    val entry = entries.nextElement()
                    if (entry.name.endsWith(".class")){
                        val className = entry.name.substring(0, entry.name.lastIndexOf(".")).replace("/", ".")
                        val clazz = pluginClasspath.pluginClassLoader.loadClass(className)
                        tryCatch<Unit> {
                            catch<Exception> {  }
                            clazz.kotlin.functions
                            add(ClassInfo(clazz))
                        }
                    }
                }
            }
        }

        /**
         * 关于类的信息
         * 主要做缓存，优化性能
         */
        class ClassInfo(
            val clazz: Class<*>,
        ){

            // 所有方法
            val functions = clazz.kotlin.functions
            val memberExtensionFunctions = clazz.kotlin.memberExtensionFunctions // 扩展函数

            // 所有层级下的注解
            val annotationAll = AnnotationUtils.getAnnotationAll(clazz)
            // 类的注解
            val annotations = clazz.annotations
            // 所有层级下的接口
            val interfacesAll = PackageUtils.getInterfaceAll(clazz)
            val interfaces = clazz.interfaces

            /**
             * 判断是否是扩展函数
             */
            fun isExtension(function: KFunction<*>): Boolean = memberExtensionFunctions.contains(function)

            /**
             * 得到所有层级上具有指定注解的方法
             */
            inline fun <reified T: Annotation> getFunctionByAnnotationAll() =
                buildList {
                    functions.forEach{ func ->
                        val ans = AnnotationUtils.getAnnotationAll(func)
                        ans.find { it.annotationClass == T::class }?.let {
                            add(FunctionInfo(func, it as T))
                        }
                    }
                }

            inline fun <reified T: Annotation> getFunctionByAnnotation() =
                buildList {
                    functions.forEach{ func ->
                        func.annotations.find { it::class == T::class }?.let {
                            add(FunctionInfo(func, it as T))
                        }
                    }
                }

            inline fun <reified T: Annotation> getMemberExtensionFunctionsByAnnotation() =
                buildList {
                    memberExtensionFunctions.forEach{ func ->
                        func.annotations.find { it::class == T::class }?.let {
                            add(FunctionInfo<T>(func, it as T))
                        }
                    }
                }


            data class FunctionInfo<T: Annotation>(
                val function: KFunction<*>,
                val annotation: T
            )
        }
    }
}