package de.theunknownxy.mcdocs.utils

import java.util.LinkedHashMap

public class LRUCache<K, V>(private val cacheSize: Int) : LinkedHashMap<K, V>() {
    override fun removeEldestEntry(eldest: MutableMap.MutableEntry<K, V>?): Boolean {
        return size() >= cacheSize
    }
}