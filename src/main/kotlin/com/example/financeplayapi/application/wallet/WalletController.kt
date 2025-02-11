package com.example.financeplayapi.application.wallet

import com.example.financeplayapi.commons.core.exception.IllegalArgumentException
import com.example.financeplayapi.commons.spring.context.RequestContext
import com.example.financeplayapi.domain.WalletService
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/wallet")
class WalletController(
    private val walletService: WalletService,
    private val requestContext: RequestContext,
) {

    @PostMapping
    fun createWallet(
        @RequestBody walletDto: WalletCmdDTO,
    ): ResponseEntity<WalletQueryDTO> {
        val wallet = walletService.createWalletForUser(
            userId = requestContext.userId,
            name = walletDto.name ?: throw IllegalArgumentException("Nome é obrigatório"),
            color = walletDto.color ?: throw IllegalArgumentException("Cor é obrigatório"),
        )
        return ResponseEntity.ok(WalletQueryDTO(wallet))
    }

//    @RequiresPermission("FREE")
    @GetMapping
    fun getWallets(
        @RequestParam("name") name: String?,
    ): ResponseEntity<List<WalletQueryDTO>> {
        val wallet = walletService.getUserWallets(requestContext.userId, null, name)
        return ResponseEntity.ok(wallet.map{ WalletQueryDTO(it) })
    }

    @GetMapping("/{walletId}")
    fun getWallets(
        @PathVariable walletId: Long,
    ): ResponseEntity<List<WalletQueryDTO>> {
        val wallet = walletService.getUserWallets(requestContext.userId, walletId, null)
        return ResponseEntity.ok(wallet.map{ WalletQueryDTO(it) })
    }


    @PutMapping("/{walletId}")
    fun changeWallet(
        @PathVariable walletId: Long,
        @RequestBody walletDto: WalletCmdDTO,
    ): ResponseEntity<WalletQueryDTO> {
        val wallet = walletService.changeUserWallet(
            userId = requestContext.userId,
            walletId = walletId,
            name = walletDto.name,
            color = walletDto.color
        )
        return ResponseEntity.ok(WalletQueryDTO(wallet))
    }

    @DeleteMapping("/{walletId}")
    fun deleteWallet(
        @PathVariable walletId: Long,
    ): ResponseEntity<Unit> {
        walletService.deleteUserWallet(requestContext.userId, walletId)
        return ResponseEntity.ok().build()
    }
}