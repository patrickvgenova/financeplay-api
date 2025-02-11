package com.example.financeplayapi.domain.entity

import com.example.financeplayapi.commons.persistence.AbstractEntity
import com.example.financeplayapi.commons.persistence.converter.DateTimeConverter
import com.example.financeplayapi.commons.persistence.converter.LocalDateConverter
import com.example.financeplayapi.domain.PlanType
import com.example.financeplayapi.domain.StatusUserType
import jakarta.persistence.*
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.Parameter
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction
import org.joda.time.DateTime
import org.joda.time.LocalDate
import java.util.*

@Entity
@Table(name = User.TABLE_NAME)
@SQLDelete(sql = "UPDATE ${User.TABLE_NAME} SET deleted = true WHERE id_user = ?")
@SQLRestriction("deleted = false")
@GenericGenerator(
    name = "sys_user_id_user_seq",
    strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
    parameters = [
        Parameter(name = "sequence_name", value = "sys_user_id_user_seq"),
        Parameter(name = "initial_value", value = "1"),
        Parameter(name = "increment_size", value = "1"),
    ],
)
open class User(
    @Column(name = "id_external", nullable = false)
    open var idExternal: UUID,
    @Column(name = "name", nullable = true)
    open var name: String? = null,
    @Column(name = "email", nullable = false)
    open var email: String,
    @Column(name = "photo_url", nullable = true)
    open var photoUrl: String? = null,
    @Column(name = "document", nullable = true)
    open var document: String? = null,
    @Column(name = "phone_country_code", nullable = true)
    open var phoneCountryCode: String? = null,
    @Column(name = "phone_area_code", nullable = true)
    open var phoneAreaCode: String? = null,
    @Column(name = "phone_number", nullable = true)
    open var phoneNumber: String? = null,
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    open var status: StatusUserType = StatusUserType.ACTIVE,
): AbstractEntity() {

    @Id
    @GeneratedValue(generator = "sys_user_id_user_seq")
    @Column(name = "id_user")
    override var id: Long? = null

    @Column(name = "deleted")
    open var deleted: Boolean = false

    @Column(name = "created_at")
    @Convert(converter = DateTimeConverter::class)
    open var createdAt: DateTime = DateTime.now()

    @Column(name = "updated_at")
    @Convert(converter = DateTimeConverter::class)
    open var updatedAt: DateTime = DateTime.now()

    @Column(name = "last_login")
    @Convert(converter = DateTimeConverter::class)
    open var lastLogin: DateTime? = null


    fun updateFrom(
        name: String? = null,
        photoUrl: String? = null,
        document: String? = null,
        phoneCountryCode: String? = null,
        phoneAreaCode: String? = null,
        phoneNumber: String? = null,
        status: StatusUserType? = null,
        lastLogin: DateTime? = null,
    ): User {
        val ret = (this.name != name) ||
        (this.photoUrl != photoUrl) ||
        (this.document != document) ||
        (this.phoneCountryCode != phoneCountryCode) ||
        (this.phoneAreaCode != phoneAreaCode) ||
        (this.phoneNumber != phoneNumber) ||
        (this.status != status) ||
        (this.lastLogin != lastLogin)
        
        if (ret) {
            name?.let { this.name = it }
            photoUrl?.let { this.photoUrl = it }
            document?.let { this.document = it }
            phoneCountryCode?.let { this.phoneCountryCode = it }
            phoneAreaCode?.let { this.phoneAreaCode = it }
            phoneNumber?.let { this.phoneNumber = it }
            status?.let { this.status = it }
            lastLogin?.let { this.lastLogin = it }
            updatedAt = DateTime.now()
        }
        return this
    }

    companion object{
        const val TABLE_NAME = "sys_user"
        const val ID_EXTERNAL__UUID = "idExternal"
        const val EMAIL__STRING = "email"
    }
}