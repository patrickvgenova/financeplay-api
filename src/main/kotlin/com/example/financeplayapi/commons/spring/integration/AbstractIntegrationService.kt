package com.example.financeplayapi.commons.spring.integration

import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.DefaultUriBuilderFactory
import java.util.*
import java.util.function.Function

abstract class AbstractIntegrationService {


    protected fun getDefaultRequest(userId: UUID, body: Any? = null): HttpEntity<*> {
        val headers = HttpHeaders()
        headers["financeplayapi-X-username"] = userId.toString()
        headers.setContentType(MediaType.APPLICATION_JSON)
        val request = if (body == null) HttpEntity<Map<Any, Any>>(headers) else HttpEntity(body, headers)
        return request
    }

    protected fun <T> query(sup: Function<RestTemplate, T?>): T? {
        return sup.apply(rest)
    }

    companion object {
        protected val rest = RestTemplate(HttpComponentsClientHttpRequestFactory())
        val factory = generateBuilder()
        fun generateBuilder(): DefaultUriBuilderFactory {
            val factory = DefaultUriBuilderFactory()
            factory.encodingMode = DefaultUriBuilderFactory.EncodingMode.URI_COMPONENT
            return factory
        }
    }
}
