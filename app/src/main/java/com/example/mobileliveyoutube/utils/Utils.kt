package com.example.mobileliveyoutube.utils

import android.content.Context

// https://medium.com/@BladeCoder/kotlin-singletons-with-argument-194ef06edd9e
open class SingletonHolder<out OUT: Any, in INPUT>(creator: (INPUT) -> OUT) {
    private var creator: ((INPUT) -> OUT)? = creator
    @Volatile private var instance: OUT? = null

    /** Initializes and returns singleton if none was created. */
    fun initialize(arg: INPUT): OUT {
        val newInstance = instance
        if (newInstance != null) return newInstance
        return synchronized(this) {
            val newInstance2 = instance
            // Re-check if "instance" was assigned
            if (newInstance2 != null) {
                newInstance2
            } else {
                val created = creator!!(arg)
                instance = created
                creator = null
                created
            }
        }
    }
    /** Added for convenience without a [Context], if and only if, components are 100% sure
     * [initialize] was called somewhere else.
     */
    fun getInstance(): OUT =
        instance?:
        throw Error("Use \"${this::creator.javaClass.simpleName}#initialize(Context)\" at least once")
}