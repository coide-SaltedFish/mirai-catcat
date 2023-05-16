# mirai-console-plugin-catcat-mirai

基于 [Mirai Console](https://github.com/mamoe/mirai-console) 的猫猫插件框架

目标是让插件开发者专注于插件功能开发，而不是在重复性的功能实现上耗费精力

### 如何使用？

首先在你的插件启动类中添加插件依赖

```kotlin
dependsOn("org.sereinfish.catcat.mirai.catcat")
```

然后将插件注册到框架的插件管理器

```kotlin
CatPackageManager.init(this, jvmPluginClasspath)
```

这样，我们就可以将自己的插件托管给框架进行管理了

### 如何创建一个命令？

在此框架中，并没有命令这一个概念。如需要更多了解请前往这个 [文档]()(还没写好) 进行查看。

不过，并非我们就无法按照我们所想的那样进行命令实现了，下面是一个简单的命令实现

```kotlin
class TestEvent: CatEventListener {
    @Handler
    suspend fun myCommand() = handler<MessageEvent>({
        router {
            atBot() + text("你好") + space() + param(array(any()), "value")
        }
    }) {
        val value by it.valueOrDefault<String>("null")
        sendMessage {
            + "你好，"
            + value
        }
    }
}
```

这个命令会匹配 `@Bot 你好 123` 这种格式的命令，并且将 `@Bot 你好 ` 后面的内容提取保存到上下文中

`val value by it.valueOrDefault<String>("null")` 这段代码会将属性 `value` 委托给上下文处理，我们可以用这种方法很方便的获取上下文中的值，并且获得类型转换器的支持

然后通过 `sendMessage {}` 将恢复消息发送到来源 

在上面的命令实现中，我们其实只是实现了一个 `MessageEvent` 事件的监听器，但通过添加 `RouterFilterHandler` 也就是路由筛选器，就可以过滤指定的消息事件了，于是就实现好了一个命令
