package org.sereinfish.catcat.mirai.catcat.utils

/**
 * 优先级列表
 * 根据提供的优先级对内容进行排序
 */
class SortedList<T>(
    private val items: MutableList<Item<T>> = mutableListOf()
): MutableCollection<T>, List<T> {
    override val size: Int
        get() = items.size

    override fun clear() {
        items.clear()
    }

    @Deprecated("在此列表中，此方法不被支持")
    override fun addAll(elements: Collection<T>): Boolean {
        TODO("Not yet implemented")
    }

    override fun add(element: T): Boolean {
        add(element, 0)
        return true
    }

    @Deprecated("在此列表中，此方法不被支持")
    override fun retainAll(elements: Collection<T>): Boolean {
        TODO("Not yet implemented")
    }

    @Deprecated("在此列表中，此方法不被支持")
    override fun removeAll(elements: Collection<T>): Boolean {
        TODO("Not yet implemented")
    }

    @Deprecated("在此列表中，此方法不被支持")
    override fun remove(element: T): Boolean {
        TODO("Not yet implemented")
    }

    override fun containsAll(elements: Collection<T>): Boolean =
        elements.forEachResult {
            contains(it)
        }

    override fun contains(element: T) =
        items.forEachResult {
            // 如果存在，返回 false 结束循环
            (it.data == element).not()
        }.not() // 返回 false 表示存在


    override fun get(index: Int): T = items[index].data
    override fun isEmpty() = items.isEmpty()

    override fun iterator(): MutableIterator<T> = SortedListIterator()

    override fun listIterator(): ListIterator<T> = SortedListIterator()

    override fun listIterator(index: Int): ListIterator<T> = SortedListIterator(index)

    override fun subList(fromIndex: Int, toIndex: Int): List<T> =
        items.subList(fromIndex, toIndex).map { it.data }

    fun subList(fromIndex: Int): List<T> =
        items.subList(fromIndex, items.size).map { it.data }

    override fun lastIndexOf(element: T): Int{
        var lastIndex = -1
        items.forEachIndexed { index, item ->
            if (item.data == element){
                lastIndex = index
            }
        }
        return lastIndex
    }

    override fun indexOf(element: T): Int {
        for (i in items.indices){
            if (items[i].data == element){
                return i
            }
        }
        return -1
    }

    /**
     * 获取指定优先级的开头索引
     */
    private fun getLevelIndex(level: Int): Int{
        for (i in items.indices){
            if (items[i].level > level)
                return i
        }
        return items.size
    }

    /**
     * 添加指定优先级数据
     */
    fun add(element: T, level: Int = 0){
        items.add(getLevelIndex(level), Item(level, element))
    }

    /**
     * 获取指定优先级的数据
     */
    fun getByLevel(level: Int): List<T> =
        buildList {
            items.forEach {
                if (it.level == level)
                    add(it.data)
            }
        }

    /**
     * 在不影响优先级的情况下进行元素转换
     */
    fun <R> map(transform: (T) -> R): SortedList<R> {
        val newList = SortedList<R>()
        items.forEach {
            newList.add(transform(it.data), it.level)
        }
        return newList
    }

    data class Item<T>(
        val level: Int,
        val data: T
    )

    private inner class SortedListIterator(private var index: Int = 0) : MutableIterator<T>, ListIterator<T> {
        override fun hasNext() = index < items.size

        override fun hasPrevious(): Boolean = index > 0

        override fun next(): T = items[index++].data

        override fun remove() {
            items.removeAt(index)
        }

        override fun nextIndex(): Int = index

        override fun previous(): T = items[--index].data

        override fun previousIndex(): Int = index - 1
    }
}