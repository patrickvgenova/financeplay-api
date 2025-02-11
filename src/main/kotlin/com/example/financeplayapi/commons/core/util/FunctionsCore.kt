package com.example.financeplayapi.commons.core.util

import java.lang.reflect.Type
import java.util.*

private val intPattern = "\\d+".toRegex()

private val uuidChars = "[0-9a-fA-F]"
val UUID_PATTERN_STR = "$uuidChars{8}-$uuidChars{4}-$uuidChars{4}-$uuidChars{4}-$uuidChars{12}"
private val uuidPattern = UUID_PATTERN_STR.toRegex()
val emptyUUID = UUID.fromString("00000000-0000-0000-0000-000000000000")
val systemUUID = UUID.fromString("FFFFFFFF-FFFF-FFFF-FFFF-FFFFFFFFFFFF")

val emailPatern =
    "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$".toRegex()

fun String?.isInt(): Boolean {
    return this != null && this.matches(intPattern)
}

fun String.trimToNull(): String? {
    val x = this.trim()
    return x.ifEmpty { null }
}

fun String?.isTrimToNull(): Boolean {
    return this == null || this.trimToNull() == null
}

fun String?.isNotTrimToNull(): Boolean {
    return !this.isTrimToNull()
}

fun String?.matchesUUID(): Boolean {
    return this != null && this.matches(uuidPattern)
}

fun String.toUUID(): UUID {
    return UUID.fromString(this)
}

fun <T> String.jsonToObject(c: Class<T>): T {
    return JsonParser.toObject<T>(this, c)!!
}

fun <T> String.jsonToObject(c: Type): T {
    val x: Any? = JsonParser.toObject(this, c)
    @Suppress("UNCHECKED_CAST")
    return x!! as T
}

fun String.toBase64(): String {
    return Base64.getEncoder().encodeToString(this.encodeToByteArray())
}

fun String.fromBase64(): String {
    return Base64.getDecoder().decode(this).decodeToString()
}

fun String.decodeBase64(): ByteArray {
    return Base64.getDecoder().decode(this)
}

fun String.normalize(): String {
    val ret = this.lowercase().toCharArray()
    for (i in ret.indices) {
        when (ret[i]) {
            'á', 'à', 'ã', 'ä', 'â' -> ret[i] = 'a'
            'é', 'è', 'ë', 'ê' -> ret[i] = 'e'
            'í', 'ì', 'ï', 'î' -> ret[i] = 'i'
            'ó', 'ò', 'ö', 'ô', 'õ' -> ret[i] = 'o'
            'ú', 'ù', 'ü', 'û' -> ret[i] = 'u'
            'ñ' -> ret[i] = 'n'
            'ç' -> ret[i] = 'c'
        }
    }
    return String(ret).trim { it <= ' ' }
}
