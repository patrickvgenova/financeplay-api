package com.example.financeplayapi.commons.spring.web

import com.example.financeplayapi.commons.core.exception.*
import com.fasterxml.jackson.databind.JsonMappingException
import jakarta.validation.ConstraintViolationException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.lang.Nullable
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.client.RestClientResponseException
import org.springframework.web.context.request.WebRequest
import org.springframework.web.util.WebUtils

@ControllerAdvice
class RestExceptionHandler {

    @ExceptionHandler(value = [ConstraintViolationException::class])
    protected fun handleConstraintViolations(ex: RuntimeException, request: WebRequest): ResponseEntity<Any> {
        val x: ConstraintViolationException = ex as ConstraintViolationException
        val message = x.constraintViolations.associate { it.propertyPath to it.message }
        return handleExceptionInternal(ex, mapOf("errors" to message), HttpHeaders(), HttpStatus.BAD_REQUEST, request)
    }

    @ExceptionHandler(value = [EntityNotFoundException::class])
    protected fun handleEntityNotFound(ex: EntityNotFoundException, request: WebRequest): ResponseEntity<Any> {
        return handleExceptionInternal(ex, mapOf("errors" to ex.data), HttpHeaders(), HttpStatus.NOT_FOUND, request)
    }

    @ExceptionHandler(value = [ServiceUnreachableException::class])
    protected fun handleServerUnreachable(ex: ServiceUnreachableException, request: WebRequest): ResponseEntity<Any> {
        return handleExceptionInternal(
            ex,
            mapOf("errors" to ex.data),
            HttpHeaders(),
            HttpStatus.SERVICE_UNAVAILABLE,
            request
        )
    }

    @ExceptionHandler(value = [ConflictException::class])
    protected fun handleConflict(ex: ConflictException, request: WebRequest): ResponseEntity<Any> {
        return handleExceptionInternal(ex, mapOf("errors" to ex.data), HttpHeaders(), HttpStatus.CONFLICT, request)
    }

    @ExceptionHandler(value = [RestClientResponseException::class])
    protected fun handleGenericRestClient(ex: RestClientResponseException, request: WebRequest): ResponseEntity<Any> {
        return handleExceptionInternal(
            ex,
            ex.responseBodyAsString,
            HttpHeaders(),
            HttpStatus.valueOf(ex.rawStatusCode),
            request
        )
    }

    @ExceptionHandler(value = [MethodArgumentNotValidException::class])
    protected fun handleIllegalMethodArgument(
        ex: MethodArgumentNotValidException,
        request: WebRequest
    ): ResponseEntity<Any> {
        val body = ex.bindingResult.fieldErrors.map { it.field to it.defaultMessage }.toMap()
        return handleExceptionInternal(ex, mapOf("errors" to body), HttpHeaders(), HttpStatus.BAD_REQUEST, request)
    }

    @ExceptionHandler(value = [JsonMappingException::class])
    protected fun handleJsonMappingError(ex: JsonMappingException, request: WebRequest): ResponseEntity<Any> {
        val m = mapOf("_message" to ex.originalMessage)
        return handleExceptionInternal(ex, mapOf("errors" to m), HttpHeaders(), HttpStatus.BAD_REQUEST, request)
    }

    @ExceptionHandler(value = [java.lang.IllegalArgumentException::class])
    protected fun handleIllegalArgument(
        ex: java.lang.IllegalArgumentException,
        request: WebRequest
    ): ResponseEntity<Any> {
        val m = mapOf("_message" to ex.message)
        return handleExceptionInternal(ex, mapOf("errors" to m), HttpHeaders(), HttpStatus.BAD_REQUEST, request)
    }

    @ExceptionHandler(value = [IllegalArgumentException::class])
    protected fun handleIllegalArgument2(ex: IllegalArgumentException, request: WebRequest): ResponseEntity<Any> {
        return handleExceptionInternal(ex, mapOf("errors" to ex.data), HttpHeaders(), HttpStatus.BAD_REQUEST, request)
    }

    @ExceptionHandler(value = [AccessDeniedException::class])
    protected fun handleAccessDenied(ex: AccessDeniedException, request: WebRequest): ResponseEntity<Any> {
        return handleExceptionInternal(ex, mapOf("errors" to ex.data), HttpHeaders(), HttpStatus.FORBIDDEN, request)
    }

    @ExceptionHandler(value = [PaymentRequiredException::class])
    protected fun handlePaymentRequired(ex: PaymentRequiredException, request: WebRequest): ResponseEntity<Any> {
        return handleExceptionInternal(ex, mapOf("errors" to ex.data), HttpHeaders(), HttpStatus.PAYMENT_REQUIRED, request)
    }

    private fun handleExceptionInternal(
        ex: Exception?,
        @Nullable body: Any?,
        headers: HttpHeaders?,
        status: HttpStatus,
        request: WebRequest,
    ): ResponseEntity<Any> {
        if (HttpStatus.INTERNAL_SERVER_ERROR == status) {
            request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, ex!!, WebRequest.SCOPE_REQUEST)
        }
        return ResponseEntity(body, headers, status)
    }
}
