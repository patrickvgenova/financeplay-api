package com.example.financeplayapi.application.user

import com.example.financeplayapi.domain.PlanType
import com.example.financeplayapi.domain.UserService
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/api/user")
class UserController(
    private val userService: UserService
) {

    @PostMapping(consumes = [
        MediaType.APPLICATION_JSON_VALUE,
        MediaType.MULTIPART_FORM_DATA_VALUE
    ])
    fun createUser(
        @ModelAttribute userDto: UserCmdDTO,
    ): ResponseEntity<UserQueryDTO> {
        val user = userService.createUser(
            name = userDto.name,
            photoUrl = userDto.photoUrl,
            document = userDto.document,
            phoneNumber = userDto.phoneNumber,
            plan = userDto.planType ?: PlanType.FREE,
        )
        return ResponseEntity.ok(UserQueryDTO(user))
    }

//    @RequiresPermission("FREE")
    @GetMapping("/{userId}")
    fun getUser(
        @PathVariable userId: UUID,
    ): ResponseEntity<UserQueryDTO> {
        val user = userService.findUser(userId)
        return ResponseEntity.ok(UserQueryDTO(user))
    }


    @PutMapping("/{userId}", consumes = [
        MediaType.APPLICATION_JSON_VALUE,
        MediaType.MULTIPART_FORM_DATA_VALUE
    ])
    fun changeUser(
        @PathVariable userId: UUID,
        @ModelAttribute userDto: UserCmdDTO,
    ): ResponseEntity<UserQueryDTO> {
        val user = userService.changeUser(
            userId = userId,
            name = userDto.name,
            photoUrl = userDto.photoUrl,
            document = userDto.document,
            phoneNumber = userDto.phoneNumber,
        )
        return ResponseEntity.ok(UserQueryDTO(user))
    }
}