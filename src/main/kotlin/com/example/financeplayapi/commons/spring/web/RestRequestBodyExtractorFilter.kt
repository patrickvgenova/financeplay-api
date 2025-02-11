package com.example.financeplayapi.commons.spring.web

import com.example.financeplayapi.commons.spring.context.RequestContext
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException
import org.springframework.core.MethodParameter
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpInputMessage
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice
import java.io.IOException
import java.io.InputStream
import java.lang.reflect.Type

@RestControllerAdvice
class RestRequestBodyExtractorFilter(
    private val requestContext: RequestContext,
) : RequestBodyAdvice {

    @Throws(IOException::class)
    override fun beforeBodyRead(
        inputMessage: HttpInputMessage,
        parameter: MethodParameter,
        targetType: Type,
        converterType: Class<out HttpMessageConverter<*>>
    ): HttpInputMessage {
        val bytearray = inputMessage.body.readAllBytes()

        try {
            val x: JsonElement = JsonParser.parseReader(bytearray.inputStream().reader())
            requestContext.restRequestBody = x
        } catch (e: JsonSyntaxException) {
        }

        return object : HttpInputMessage {
            @Throws(IOException::class)
            override fun getBody(): InputStream {
                return bytearray.inputStream()
            }

            override fun getHeaders(): HttpHeaders {
                return inputMessage.headers
            }
        }
    }

    override fun supports(
        methodParameter: MethodParameter,
        targetType: Type,
        converterType: Class<out HttpMessageConverter<*>>
    ): Boolean {
        return true
    }

    override fun afterBodyRead(
        body: Any,
        inputMessage: HttpInputMessage,
        parameter: MethodParameter,
        targetType: Type,
        converterType: Class<out HttpMessageConverter<*>>
    ): Any {
        return body
    }

    override fun handleEmptyBody(
        body: Any?,
        inputMessage: HttpInputMessage,
        parameter: MethodParameter,
        targetType: Type,
        converterType: Class<out HttpMessageConverter<*>>
    ): Any? {
        return body
    }
}
