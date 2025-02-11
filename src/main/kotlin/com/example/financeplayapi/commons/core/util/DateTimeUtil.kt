package com.example.financeplayapi.commons.core.util

import org.joda.time.Interval
import org.joda.time.Period
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.ISODateTimeFormat
import java.util.*

/**
 * Métodos utilitários para manipulação de datas e horas.
 */
object DateTimeUtil {
    val FORMAT_YMDT_HMSSZ = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    val FORMAT_YMDT_HMS = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss")
    val ISO_8601_DATE_FORMAT = ISODateTimeFormat.dateTime()
    val ISO_8601_DATE_FORMAT_NO_MILLIS_WITH_OFFSET = ISODateTimeFormat.dateTimeNoMillis().withOffsetParsed()
    val FORMAT_YMD = DateTimeFormat.forPattern("yyyy-MM-dd")
    val FORMAT_YYYY = DateTimeFormat.forPattern("yyyy")
    val FORMAT_MM = DateTimeFormat.forPattern("MM")
    val FORMAT_DD = DateTimeFormat.forPattern("dd")
    val FORMAT_YM = DateTimeFormat.forPattern("yyyy-MM")
    val FORMAT_MY = DateTimeFormat.forPattern("MM/yyyy")
    val FORMAT_MD = DateTimeFormat.forPattern("MM-dd")
    val FORMAT_SQL_YMD_HM = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm")
    val FORMAT_YMD_HM = ISODateTimeFormat.dateHourMinute()
    val FORMAT_DMY_HM = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm")
    val FORMAT_DMY_HMS = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss")
    val FORMAT_YMD_HMS = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")
    val FORMAT_YMD_HMSS = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS")
    private val dmyFormatter = DateTimeFormat.forPattern("dd/MM/yyyy")
    val FORMAT_DMY = dmyFormatter
    private val hmFormatter = DateTimeFormat.forPattern("HH:mm")
    val FORMAT_HM = hmFormatter
    private val hmsFormatter = DateTimeFormat.forPattern("HH:mm:ss")
    val FORMAT_HMS = hmsFormatter

    /**
     * Mede a distancia de que um intervalo está de outra intervalo de referencia. a distancia entre 2 intervalos é a quantidade de
     * milissegundos entre o fim de um intervalo e o
     * início de outro. Caso os intervalos tenham overlap, a distancia é zero.
     *
     * @param interval1 intervalo 1
     * @param interval2 intervalo 2
     *
     * @return distancia
     */
    fun distanceInMillis(interval1: Interval, interval2: Interval?): Long {
        val gap = interval1.gap(interval2) ?: return 0
        return gap.toDurationMillis()
    }

    /**
     * Formata um período em string HH:mm.
     *
     * @param p periodo
     *
     * @return string formatada
     */
    fun formatHourMinute(p: Period?): String? {
        if (p == null) {
            return null
        }
        val totalMinutes = p.toStandardMinutes().minutes
        val hours = totalMinutes / 60
        val minutes = totalMinutes % 60
        return String.format("%02d:%02d", hours, minutes)
    }

    /**
     * Subtrai intervalos com intersecção. Uma subtração de um intervalo consiste em buscar um ou mais intervalos que tenham sobreposição
     * com um intervalo mas que não tenham
     * nenhuma sobreposição com o segundo.
     *
     * @param intervalA intervalo a ser subtraído
     * @param intervalB intersecção que será subtraída do primeiro intervalo
     *
     * @return zero ou mais intervalos que tenham sobreposição com intervalA mas que NÃO tenham sobreposição com intervalB
     */
    fun subtractInterval(intervalA: Interval, intervalB: Interval): Collection<Interval> {
        if (!intervalA.overlaps(intervalB)) {
            return Arrays.asList(intervalA)
        }
        val ret: MutableList<Interval> = ArrayList()
        if (intervalA.start.isBefore(intervalB.start)) {
            ret.add(Interval(intervalA.start, intervalB.start))
        }
        if (intervalA.end.isAfter(intervalB.end)) {
            ret.add(Interval(intervalB.end, intervalA.end))
        }
        return ret
    }
}
