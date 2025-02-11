package com.example.financeplayapi.domain.entity

import com.example.financeplayapi.commons.persistence.AbstractEntity
import com.example.financeplayapi.commons.persistence.converter.DateTimeConverter
import jakarta.persistence.*
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.Parameter
import org.joda.time.DateTime

@Entity
@Table(name = UserCoinsTransaction.TABLE_NAME)
@GenericGenerator(
    name = "user_coins_transaction_id_user_coins_transaction_seq",
    strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
    parameters = [
        Parameter(name = "sequence_name", value = "user_coins_transaction_id_user_coins_transaction_seq"),
        Parameter(name = "initial_value", value = "1"),
        Parameter(name = "increment_size", value = "1"),
    ],
)
open class UserCoinsTransaction(
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id_user", nullable = false)
    open var user: User,

    @Column(name = "transaction_type", nullable = false)
    open var transactionType: String, // PURCHASE, MONTHLY_CREDIT, USAGE, REFUND

    @Column(name = "amount", nullable = false)
    open var amount: Long,

    @Column(name = "balance_after", nullable = false)
    open var balanceAfter: Long,

    @Column(name = "description", nullable = true)
    open var description: String? = null,

    @Column(name = "feature_used", nullable = true)
    open var featureUsed: String? = null,

    @Column(name = "metadata", columnDefinition = "jsonb", nullable = true)
    open var metadata: String? = null, // JSONB pode ser representado como String
) : AbstractEntity() {

    @Id
    @GeneratedValue(generator = "user_coins_transaction_id_user_coins_transaction_seq")
    @Column(name = "id_user_coins_transaction")
    override var id: Long? = null

    @Column(name = "created_at", nullable = false)
    @Convert(converter = DateTimeConverter::class)
    open var createdAt: DateTime = DateTime.now()

    companion object {
        const val TABLE_NAME = "user_coins_transaction"
        const val USER_ID__BIGINT = "user_id"
        const val TRANSACTION_TYPE__STRING = "transaction_type"
        const val AMOUNT__BIGINT = "amount"
        const val BALANCE_AFTER__BIGINT = "balance_after"
        const val DESCRIPTION__TEXT = "description"
        const val FEATURE_USED__STRING = "feature_used"
        const val METADATA__JSONB = "metadata"
        const val CREATED_AT__TIMESTAMP = "created_at"
    }

    fun updateFrom(
        transactionType: String? = null,
        amount: Long? = null,
        balanceAfter: Long? = null,
        description: String? = null,
        featureUsed: String? = null,
        metadata: String? = null,
    ): UserCoinsTransaction {
        val ret = (this.transactionType != transactionType) ||
                (this.amount != amount) ||
                (this.balanceAfter != balanceAfter) ||
                (this.description != description) ||
                (this.featureUsed != featureUsed) ||
                (this.metadata != metadata)

        if (ret) {
            transactionType?.let { this.transactionType = it }
            amount?.let { this.amount = it }
            balanceAfter?.let { this.balanceAfter = it }
            description?.let { this.description = it }
            featureUsed?.let { this.featureUsed = it }
            metadata?.let { this.metadata = it }
        }
        return this
    }
}