package com.example.financeplayapi.domain.entity

import com.example.financeplayapi.commons.persistence.AbstractEntity
import com.example.financeplayapi.commons.persistence.converter.DateTimeConverter
import jakarta.persistence.*
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.Parameter
import org.joda.time.DateTime

@Entity
@Table(name = UserCoins.TABLE_NAME)
@GenericGenerator(
    name = "user_coins_id_user_coins_seq",
    strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
    parameters = [
        Parameter(name = "sequence_name", value = "user_coins_id_user_coins_seq"),
        Parameter(name = "initial_value", value = "1"),
        Parameter(name = "increment_size", value = "1"),
    ],
)
open class UserCoins(
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id_user", nullable = false)
    open var user: User,

    @Column(name = "current_balance", nullable = false)
    open var currentBalance: Long = 0,

    @Column(name = "total_earned", nullable = false)
    open var totalEarned: Long = 0,

    @Column(name = "total_spent", nullable = false)
    open var totalSpent: Long = 0,

    @Column(name = "last_monthly_credit_at", nullable = true)
    @Convert(converter = DateTimeConverter::class)
    open var lastMonthlyCreditAt: DateTime? = null,
) : AbstractEntity() {

    @Id
    @GeneratedValue(generator = "user_coins_id_user_coins_seq")
    @Column(name = "id_user_coins")
    override var id: Long? = null

    @Column(name = "created_at", nullable = false)
    @Convert(converter = DateTimeConverter::class)
    open var createdAt: DateTime = DateTime.now()

    @Column(name = "updated_at", nullable = false)
    @Convert(converter = DateTimeConverter::class)
    open var updatedAt: DateTime = DateTime.now()


    fun updateFrom(
        currentBalance: Long? = null,
        totalEarned: Long? = null,
        totalSpent: Long? = null,
        lastMonthlyCreditAt: DateTime? = null,
    ): UserCoins {
        val ret = (this.currentBalance != currentBalance) ||
                (this.totalEarned != totalEarned) ||
                (this.totalSpent != totalSpent) ||
                (this.lastMonthlyCreditAt != lastMonthlyCreditAt)

        if (ret) {
            currentBalance?.let { this.currentBalance = it }
            totalEarned?.let { this.totalEarned = it }
            totalSpent?.let { this.totalSpent = it }
            lastMonthlyCreditAt?.let { this.lastMonthlyCreditAt = it }
            updatedAt = DateTime.now()
        }
        return this
    }

    companion object {
        const val TABLE_NAME = "user_coins"
        const val USER_ID__BIGINT = "user_id"
        const val CURRENT_BALANCE__BIGINT = "current_balance"
        const val TOTAL_EARNED__BIGINT = "total_earned"
        const val TOTAL_SPENT__BIGINT = "total_spent"
        const val LAST_MONTHLY_CREDIT_AT__TIMESTAMP = "last_monthly_credit_at"
        const val CREATED_AT__TIMESTAMP = "created_at"
        const val UPDATED_AT__TIMESTAMP = "updated_at"
    }
}