package com.example.financeplayapi.domain.entity

import com.example.financeplayapi.commons.core.type.Money
import com.example.financeplayapi.commons.persistence.AbstractEntity
import com.example.financeplayapi.commons.persistence.converter.DateTimeConverter
import com.example.financeplayapi.commons.persistence.converter.LocalDateConverter
import com.example.financeplayapi.commons.persistence.converter.MoneyConverter
import jakarta.persistence.*
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.Parameter
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction
import org.joda.time.DateTime
import org.joda.time.LocalDate
import java.util.*

@Entity
@Table(name = AbstractTransaction.TABLE_NAME)
@SQLDelete(sql = "UPDATE ${AbstractTransaction.TABLE_NAME} SET deleted = true WHERE id = ?")
@SQLRestriction("deleted = false")
@DiscriminatorColumn(name = "type")
@GenericGenerator(
    name = "transaction_id_transaction_seq",
    strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
    parameters = [
        Parameter(name = "sequence_name", value = "transaction_id_transaction_seq"),
        Parameter(name = "initial_value", value = "1"),
        Parameter(name = "increment_size", value = "1"),
    ],
)
open class AbstractTransaction(

    @Column(name = "recipient", nullable = true)
    open var recipient: String?,

    @Column(name = "description", nullable = true)
    open var description: String?,

    @Column(name = "total_value", nullable = true)
    @Convert(converter = MoneyConverter::class)
    open var totalValue: Money?,

    @ManyToOne
    @JoinColumn(name = "id_user")
    open var user: User,

    @ManyToOne
    @JoinColumn(name = "id_category", nullable = true)
    open var category: AbstractCategory?,

    @Column(name = "transaction_datetime", nullable = false)
    @Convert(converter = DateTimeConverter::class)
    open var transactionDatetime: DateTime?,

    ): AbstractEntity() {

    @Id
    @GeneratedValue(generator = "transaction_id_transaction_seq")
    @Column(name = "id_transaction")
    override var id: Long? = null

    @Column(name = "id_external", nullable = false)
    open var idExternal: UUID = UUID.randomUUID()

    @Column(name = "deleted")
    open var deleted: Boolean = false

    @Column(name = "created_at")
    @Convert(converter = LocalDateConverter::class)
    open var createdAt: LocalDate = LocalDate.now()

    @OneToMany(mappedBy = "transaction", fetch = FetchType.EAGER)
    open var transactionItems: MutableSet<TransactionItem> = mutableSetOf()

    @OneToMany(mappedBy = "transaction", fetch = FetchType.EAGER)
    open var wallets: MutableSet<WalletTransaction> = mutableSetOf()

    @PreRemove
    fun preRemove() {
        this.deleted = true
        for (item in transactionItems) {
            item.deleted = true
        }
    }

    companion object{
        const val TABLE_NAME = "transaction"
        const val ID_EXTERNAL__UUID = "idExternal"
        const val COL__ID_TRANSACTION__LONG = "id_transaction"
    }
}