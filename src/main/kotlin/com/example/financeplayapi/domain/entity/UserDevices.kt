package com.example.financeplayapi.domain.entity

import com.example.financeplayapi.commons.persistence.AbstractEntity
import com.example.financeplayapi.commons.persistence.converter.DateTimeConverter
import jakarta.persistence.*
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.Parameter
import org.joda.time.DateTime

@Entity
@Table(name = UserDevices.TABLE_NAME)
@GenericGenerator(
    name = "user_devices_id_seq",
    strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
    parameters = [
        Parameter(name = "sequence_name", value = "user_devices_id_seq"),
        Parameter(name = "initial_value", value = "1"),
        Parameter(name = "increment_size", value = "1"),
    ],
)
open class UserDevices(
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id_user", nullable = false)
    open var user: User,

    @Column(name = "device_token", nullable = true)
    open var deviceToken: String? = null,

    @Column(name = "device_type", nullable = true)
    open var deviceType: String? = null,

    @Column(name = "last_active", nullable = true)
    @Convert(converter = DateTimeConverter::class)
    open var lastActive: DateTime? = null,
) : AbstractEntity() {

    @Id
    @GeneratedValue(generator = "user_devices_id_seq")
    @Column(name = "id")
    override var id: Long? = null

    @Column(name = "created_at", nullable = false)
    @Convert(converter = DateTimeConverter::class)
    open var createdAt: DateTime = DateTime.now()

    companion object {
        const val TABLE_NAME = "user_devices"
        const val USER_ID__BIGINT = "user_id"
        const val DEVICE_TOKEN__STRING = "device_token"
        const val DEVICE_TYPE__STRING = "device_type"
        const val LAST_ACTIVE__TIMESTAMP = "last_active"
        const val CREATED_AT__TIMESTAMP = "created_at"
    }

    fun updateFrom(
        deviceToken: String? = null,
        deviceType: String? = null,
        lastActive: DateTime? = null,
    ): UserDevices {
        val ret = (this.deviceToken != deviceToken) ||
                (this.deviceType != deviceType) ||
                (this.lastActive != lastActive)

        if (ret) {
            deviceToken?.let { this.deviceToken = it }
            deviceType?.let { this.deviceType = it }
            lastActive?.let { this.lastActive = it }
        }
        return this
    }
}