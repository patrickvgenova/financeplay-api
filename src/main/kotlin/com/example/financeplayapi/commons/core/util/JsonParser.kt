package com.example.financeplayapi.commons.core.util

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.lang.reflect.Type

/**
 * Parser de JSON utilizando a biblioteca GSON.
 */
object JsonParser {
    private val gson = instance
    private val gsonPretty = instanceBeauty

    /**
     * Converte o objeto informado em uma [String] JSON. Caso a conversão não puder ser realizada retornará `null`.
     *
     * @param object
     * Objeto a ser convertido.
     * @return String json.
     */
    fun toJson(`object`: Any?): String {
        return toJson(`object`, false)
    }

    /**
     * Converte o objeto informado em uma [String] JSON. Caso a conversão não puder ser realizada retornará `null`.
     *
     * @param object
     * Objeto a ser convertido.
     * @return String json.
     */
    fun toJson(`object`: Any?, pretty: Boolean): String {
        return if (pretty) gsonPretty.toJson(`object`) else gson.toJson(`object`)
    }

    /**
     * Converte a [String] JSON informada em um objeto do tipo informado. Caso a conversão não puder ser realizada retornará `null`.
     *
     * @param json
     * [String] JSON a ser convertida.
     * @param clazz
     * [Class] do tipo do objeto a ser gerado.
     * @param <T>
     * Tipo do objeto.
     *
     * @return Objeto.
    </T> */
    fun <T> toObject(json: String?, clazz: Class<T>): T {
        return gson.fromJson(json, clazz)
    }

    /**
     * Converte a [String] JSON informada em um objeto do tipo informado. Caso a conversão não puder ser realizada retornará `null`.
     *
     * @param json
     * [String] JSON a ser convertida.
     * @param clazz
     * [Class] do tipo do objeto a ser gerado.
     * @param <T>
     * Tipo do objeto.
     *
     * @return Objeto.
    </T> */
    fun <T> toObject(json: String?, clazz: Type): T? {
        return gson.fromJson(json, clazz)
    }

    /**
     * Converte o json para uma lista de mapa.
     *
     * @param json    json.
     * @param mapType tipo da lista
     * @param <T>     tipo contido na lista
     *
     * @return lista de mapa.
    </T> */
    fun <T> toList(json: String?, mapType: Type?): List<T>? {
        return gson.fromJson<List<T>>(json, mapType)
    }

    private val instance: Gson
        get() {
            val builder = GsonBuilder()
            return builder.create()
        }
    private val instanceBeauty: Gson
        get() {
            val builder = GsonBuilder().setPrettyPrinting()
            // builder.registerTypeAdapter(LocalDate.class, new LocalDateHandler());
            return builder.create()
        }
}
