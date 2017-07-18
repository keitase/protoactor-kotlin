package actor.proto

import java.util.concurrent.ConcurrentHashMap

typealias Partition = ConcurrentHashMap<String, Process>

internal class HashedConcurrentDictionary {
    companion object {
        private val HashSize: Int = 1024
        /*fun calculateHash (read : String) : Long {
            var hashedValue : Long = 3074457345618258791
            for (i in 0..read.length) {
                hashedValue += read[i].toInt()
                hashedValue *= 3074457345618258799
            }
            return hashedValue
        }*/
    }

    private val _partitions: Array<Partition> = Array(HashSize, { ConcurrentHashMap<String, Process>() })
    private fun getPartition(key: String): Partition {
        val hash: Int = Math.abs(key.hashCode()) % HashSize
        val p = _partitions[hash]
        return p
    }

    fun put(key: String, reff: Process): Boolean {
        val p = getPartition(key)
        if (p.containsKey(key)) {
            return false
        }
        p.put(key, reff)
        return true
    }

    fun tryGetValue(key: String): Process? {
        val p = getPartition(key)
        return p[key]
    }

    fun remove(key: String) {
        val p = getPartition(key)
        p.remove(key)
    }

    fun getOrDefault(key: String, default: Process): Process {
        return tryGetValue(key) ?: default
    }
}