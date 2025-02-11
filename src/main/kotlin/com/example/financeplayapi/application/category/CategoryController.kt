package com.example.financeplayapi.application.category

import com.example.financeplayapi.commons.core.exception.EntityNotFoundException
import com.example.financeplayapi.commons.core.exception.IllegalArgumentException
import com.example.financeplayapi.commons.spring.context.RequestContext
import com.example.financeplayapi.domain.CategoryType
import com.example.financeplayapi.domain.ExpenseCategoryService
import com.example.financeplayapi.domain.RevenueCategoryService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/category/{categoryType}")
class CategoryController(
    private val expenseCategoryService: ExpenseCategoryService,
    private val revenueCategoryService: RevenueCategoryService,
    private val requestContext: RequestContext,
) {

    @PostMapping
    fun createCategory(
        @PathVariable categoryType: CategoryType,
        @RequestBody categoryDto: CategoryCmdDTO,
    ): ResponseEntity<CategoryQueryDTO> {
        val category = if (categoryType === CategoryType.REVENUE) {
            revenueCategoryService.createRevenueCategory(
                userId = requestContext.userId,
                name = categoryDto.name ?: throw IllegalArgumentException("Nome é obrigatório"),
                color = categoryDto.color ?: throw IllegalArgumentException("Cor é obrigatório"),
            )
        } else if (categoryType === CategoryType.EXPENSE) {
            expenseCategoryService.createExpenseCategoryForMe(
                userId = requestContext.userId,
                name = categoryDto.name ?: throw IllegalArgumentException("Nome é obrigatório"),
                color = categoryDto.color ?: throw IllegalArgumentException("Cor é obrigatório"),
            )
        } else throw EntityNotFoundException("Category type not found")
        return ResponseEntity.ok(CategoryQueryDTO(category))
    }

    //    @RequiresPermission("FREE")
    @GetMapping
    fun getCategories(
        @PathVariable categoryType: CategoryType,
        @RequestParam("name") name: String?,
    ): ResponseEntity<List<CategoryQueryDTO>> {
        val category = if (categoryType === CategoryType.REVENUE) {
            revenueCategoryService.findRevenueCategories(requestContext.userId, name)
        } else if (categoryType === CategoryType.EXPENSE) {
            expenseCategoryService.findExpenseCategories(requestContext.userId, name)
        } else throw EntityNotFoundException("Category type not found")
        return ResponseEntity.ok(category.map { CategoryQueryDTO(it) })
    }

    @GetMapping("/{categoryId}")
    fun getCategory(
        @PathVariable categoryType: CategoryType,
        @PathVariable categoryId: Long,
    ): ResponseEntity<CategoryQueryDTO> {
        val category = if (categoryType === CategoryType.REVENUE) {
            revenueCategoryService.findRevenueCategory(requestContext.userId, categoryId)
        } else if (categoryType === CategoryType.EXPENSE) {
            expenseCategoryService.findExpenseCategory(requestContext.userId, categoryId)
        } else throw EntityNotFoundException("Category type not found")
        return ResponseEntity.ok(CategoryQueryDTO(category))
    }


    @PutMapping("/{categoryId}")
    fun changeCategory(
        @PathVariable categoryType: CategoryType,
        @PathVariable categoryId: Long,
        @RequestBody categoryDto: CategoryCmdDTO,
    ): ResponseEntity<CategoryQueryDTO> {
        val category = if (categoryType === CategoryType.REVENUE) {
            revenueCategoryService.changeRevenueCategory(
                userId = requestContext.userId,
                id = categoryId,
                name = categoryDto.name,
                color = categoryDto.color
            )
        } else if (categoryType === CategoryType.EXPENSE) {
            expenseCategoryService.changeExpenseCategory(
                userId = requestContext.userId,
                id = categoryId,
                name = categoryDto.name,
                color = categoryDto.color
            )
        } else throw EntityNotFoundException("Category type not found")
        return ResponseEntity.ok(CategoryQueryDTO(category))
    }

    @DeleteMapping("/{categoryId}")
    fun deleteCategory(
        @PathVariable categoryType: CategoryType,
        @PathVariable categoryId: Long,
    ): ResponseEntity<Unit> {
        val category = if (categoryType === CategoryType.REVENUE) {
            revenueCategoryService.deleteRevenueCategory(requestContext.userId, categoryId)
        } else if (categoryType === CategoryType.EXPENSE) {
            expenseCategoryService.deleteExpenseCategory(requestContext.userId, categoryId)
        } else throw EntityNotFoundException("Category type not found")
        return ResponseEntity.ok().build()
    }
}