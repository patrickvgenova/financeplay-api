package com.example.financeplayapi.commons.spring.context

import com.example.financeplayapi.commons.core.util.trimToNull
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import jakarta.servlet.http.HttpServletRequest
import org.springframework.context.annotation.Scope
import org.springframework.context.annotation.ScopedProxyMode
import org.springframework.stereotype.Component
import org.springframework.web.context.WebApplicationContext
import java.util.*

/**
 * Dados do usuário armazenados no contexto de request.
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
open class RequestContext(
    private val httpRequest: HttpServletRequest,
) {

    lateinit var userId: UUID
    var email: String? = null

    private val map: MutableMap<String, Any?> = HashMap()

    var restRequestBody: JsonElement? = null

    /**
     * Retorna o body de uma requisicao rest no formato object.
     *
     *  @return body no formato object, caso o body da requisição seja um json de um object (e nao null, um array ou um tipo primitivo)
     */
    val restRequestBodyObject: JsonObject?
        get() {
            val r = this.restRequestBody ?: return null
            if (r.isJsonObject) {
                return restRequestBody as JsonObject
            }
            return null
        }

    /**
     * Retorna o body de uma requisicao rest no formato array.
     *
     *  @return body no formato array, caso o body da requisição seja um json de um array (e nao null, um object ou um tipo primitivo)
     */
    val restRequestBodyArray: JsonArray?
        get() {
            val r = this.restRequestBody ?: return null
            if (r.isJsonArray) {
                return restRequestBody as JsonArray
            }
            return null
        }

    /**
     * Retorna o body de uma requisicao rest no formato primiivo.
     *
     *  @return body no formato primitivo, caso o body da requisição seja um json de um primitivo (e nao null, um array ou um object)
     */
    val restRequestBodyPrimitive: JsonPrimitive?
        get() {
            val r = this.restRequestBody ?: return null
            if (r.isJsonPrimitive) {
                return restRequestBody as JsonPrimitive
            }
            return null
        }

    /**
     * Verifica se há informação armazenada no contexto de request.
     *
     * @param key chave da informacao
     *
     * @return true para informação armazenada; false caso contrário
     */
    fun containsKey(key: String): Boolean {
        return map.containsKey(key)
    }

    /**
     * Remove uma informação do contexto de request.
     *
     * @param key chave
     */
    fun remove(key: String) {
        map.remove(key)
    }

    /**
     * Registra uma informação no contexto de request.
     *
     * @param key   chave
     * @param value valor
     */
    operator fun set(key: String, value: Any?) {
        map[key] = value
    }

    /**
     * Obtem uma informação no contexto de request.
     *
     * @param key   chave
     * @return value valor
     */
    operator fun get(key: String): Any? {
        return map[key]
    }

    /**
     * Le o valor de um header do request.
     *
     * @param name nome do header
     * @return valor do header; null caso o header nao esteja definido no request
     */
    fun getHeader(name: String): String? {
        return this.httpRequest.getHeader(name)?.trimToNull()
    }
}
