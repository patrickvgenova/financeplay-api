package com.example.financeplayapi.commons.spring.web

import com.example.financeplayapi.commons.core.util.JsonParser
import com.example.financeplayapi.commons.core.util.emptyUUID
import com.example.financeplayapi.commons.core.util.fromBase64
import com.example.financeplayapi.commons.spring.context.RequestContext
import com.example.financeplayapi.commons.spring.context.RequiresPermission
import com.example.financeplayapi.commons.spring.util.typeRef
import com.example.financeplayapi.domain.PlanType
import com.example.financeplayapi.domain.UserPermissionService
import jakarta.servlet.*
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.context.support.WebApplicationContextUtils
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerMapping
import java.util.UUID

@Component
@Order(value = Ordered.LOWEST_PRECEDENCE - 1000)
class AuthenticationDetailsFilter : Filter {

    @Autowired
    private lateinit var request: RequestContext

//    @Autowired
//    private lateinit var userPermissionService: UserPermissionService

//    override fun init(filterConfig: FilterConfig?) {
//        val context = filterConfig?.servletContext
//        val applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(context!!)
//        userPermissionService = applicationContext.getBean(UserPermissionService::class.java)
//    }

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        request as HttpServletRequest
        val token = request.getHeader("authorization")?.takeIf { it.lowercase().startsWith("bearer") }?.substring(7)
        val userId = request.getHeader("financeplay-X-username")?.let { UUID.fromString(it) }
            ?: token?.let { extractUserId(it) }
        this.request.userId = userId ?: emptyUUID
        this.request.email = token?.let { extractEmail(it) }

        val handlerMethod = getHandleMethod(request)
//        val requiresPermission = handlerMethod?.getMethodAnnotation(RequiresPermission::class.java)

//        if (requiresPermission != null) {
//            val requiredPermission = requiresPermission.permission
//            if (!userPermissionService.hasPermission(this.request.userId, PlanType.valueOf(requiredPermission.uppercase()))) {
//                response as HttpServletResponse
//                response.writer.write( mapOf("errors" to mapOf("_message" to "User does not have permission to access this resource")).toString())
//                response.contentType = MediaType.APPLICATION_JSON_VALUE
//                response.status = HttpServletResponse.SC_FORBIDDEN
//                return
//            }
//        }

        chain.doFilter(request, response)
    }

    fun getHandleMethod(request: HttpServletRequest): HandlerMethod? {
        val handlerMapping = WebApplicationContextUtils.getRequiredWebApplicationContext(request.servletContext)
            .getBean("requestMappingHandlerMapping", HandlerMapping::class.java)
        val handlerExecutionChain = handlerMapping.getHandler(request)
        return if (handlerExecutionChain != null && handlerExecutionChain.handler is HandlerMethod) {
            handlerExecutionChain.handler as HandlerMethod
        } else null
    }

    private fun extractUserId(token: String): UUID? {
        val parts = token.split(".").toTypedArray()
        if (parts.size != 3) {
            throw IllegalArgumentException("Illegal Token")
        }
        val jsonPayload = parts[1].fromBase64()
        val map = JsonParser.toObject<Map<String, String>>(jsonPayload, typeRef<Map<String, Any?>>().type) ?: mapOf()
        return UUID.fromString(map["sub"] as String)
    }

    private fun extractEmail(token: String): String? {
        val parts = token.split(".").toTypedArray()
        if (parts.size != 3) {
            throw IllegalArgumentException("Illegal Token")
        }
        val jsonPayload = parts[1].fromBase64()
        val map = JsonParser.toObject<Map<String, String>>(jsonPayload, typeRef<Map<String, Any?>>().type) ?: mapOf()
        return map["email"] as String
    }

}
