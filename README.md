# mirai-console-plugin-catcat-mirai

基于 [Mirai Console](https://github.com/mamoe/mirai-console) 的猫猫插件框架

### 如何使用？

首先将你的插件添加到框架

在你的插件启动类中添加插件依赖

```kotlin
dependsOn("org.sereinfish.catcat.mirai.catcat")
```

将插件注册到框架的插件管理器

```kotlin
CatPackageManager.init(this, jvmPluginClasspath)
```

完成插件添加

### 如何创建一个命令？

在框架中，命令这个概念被集成到了事件监听器中。需要了解更多请前往这个 [文档]() (还没写好) 进行查看。

下面是一个简单的命令实现

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

这个事件处理器会匹配并处理 `@Bot 你好 123` 这种格式的消息，并将 `@Bot 你好 ` 后面的内容提取并保存到上下文中，值的Key为`value`

`val value by it.valueOrDefault<String>("null")` 这段代码会将属性 `value` 委托给上下文处理，可以以此获取上下文中的值，并且获得类型转换器的支持

然后通过 `sendMessage {}` 将消息发送到来源 

在上面的例子中，我们实现了一个 `MessageEvent` 事件的监听器，并且添加了路由筛选器 `RouterFilterHandler`，它会过滤指定的消息事件。
