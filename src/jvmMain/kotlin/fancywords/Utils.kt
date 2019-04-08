package fancywords

import org.apache.commons.text.StringEscapeUtils
import java.util.*

/**
 * Created by Shrec on 6/10/2017.
 */
val MEMORY_DIV_K = (1000L * 60 * 60 * 24 * 30).toDouble()
private val random = Random(System.nanoTime())
fun getMastery(logAbsoluteMastery: Double, time: Long): Double =
    Math.exp(logAbsoluteMastery - time / MEMORY_DIV_K)

fun getLogAbsoluteMastery(mastery: Double, time: Long): Double =
    Math.log(mastery) + time / MEMORY_DIV_K

fun increaseLogAbsoluteMasteryByOneByTime(logAbsoluteMastery: Double, time: Long): Double =
    getLogAbsoluteMastery(getMastery(logAbsoluteMastery, time) + 1, time)

fun randomIn4(): Byte =
    Math.abs(random.nextInt() % 4).toByte()

fun toJsonArray(array: Array<String>): String {
    val stringBuilder = StringBuilder("[")
    for (i in array.indices) {
        stringBuilder.append("\"")
            .append(array[i])
            .append("\"")
        if (i < array.size - 1) stringBuilder.append(",")
    }
    stringBuilder.append("]")
    return stringBuilder.toString()
}

fun toPercentage(value: Double): String =
    Math.round(100 * value).toString() + "%"

fun escapeEcmaScript(input: String): String =
    StringEscapeUtils.escapeEcmaScript(input)
