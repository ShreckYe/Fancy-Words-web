package fancywords

import java.sql.SQLException

/**
 * Created by Shrec on 6/10/2017.
 */
fun main() {
    try {
        val server = FancyWordsServer.INSTANCE
        println(server.requestAllEntries())
    } catch (e: SQLException) {
        e.printStackTrace()
    }
}
