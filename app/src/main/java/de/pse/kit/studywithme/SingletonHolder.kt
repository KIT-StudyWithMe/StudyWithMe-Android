package de.pse.kit.studywithme

/**
 * Class for the singleton pattern
 *
 * @param T
 * @param A
 * @constructor
 *
 * @param creator
 */
open class SingletonHolder<out T: Any, in A>(creator: (A) -> T) {
    private var creator: ((A) -> T)? = creator
    @Volatile private var instanceParam: A? = null
    @Volatile private var instance: T? = null

    fun getInstance(arg: A): T {
        val i = instance
        val p = instanceParam
        if (i != null && p == arg) {
            return i
        }

        return synchronized(this) {
            val i2 = instance
            if (i2 != null) {
                i2
            } else {
                val created = creator!!(arg)
                instance = created
                instanceParam = arg
                creator = null
                created
            }
        }
    }
}