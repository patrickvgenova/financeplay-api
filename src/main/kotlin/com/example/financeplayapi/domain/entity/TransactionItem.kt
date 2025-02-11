package com.example.financeplayapi.domain.entity

import com.example.financeplayapi.commons.core.type.Money
import com.example.financeplayapi.commons.persistence.AbstractEntity
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.DiscriminatorColumn
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.PreRemove
import jakarta.persistence.Table
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.Parameter
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction
import org.joda.time.DateTime
import org.joda.time.LocalDate
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters.LocalDateConverter
import java.util.*

@Entity
@Table(name = TransactionItem.TABLE_NAME)
@SQLDelete(sql = "UPDATE ${TransactionItem.TABLE_NAME} SET deleted = true WHERE id = ?")
@SQLRestriction("deleted = false")
@GenericGenerator(
    name = "transaction_item_id_transaction_item_seq",
    strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
    parameters = [
        Parameter(name = "sequence_name", value = "transaction_item_id_transaction_item_seq"),
        Parameter(name = "initial_value", value = "1"),
        Parameter(name = "increment_size", value = "1"),
    ],
)
open class TransactionItem(

    @Column(name = "code", nullable = true)
    open var code: String?,

    @Column(name = "code_type", nullable = true)
    open var codeType: String?,

    @Column(name = "name", nullable = false)
    open var name: String,

    @Column(name = "measure", nullable = true)
    open var measure: String?,

    @Column(name = "quantity", nullable = false)
    open var quantity: Double,

    @Column(name = "unit_value", nullable = false)
    open var unitValue: Money,

    @Column(name = "total_value", nullable = false)
    open var totalValue: Money,

    @ManyToOne
    @JoinColumn(name = "id_category", nullable = true)
    open var category: AbstractCategory?,

    @ManyToOne
    @JoinColumn(name = "id_transaction")
    open var transaction: AbstractTransaction,

    ): AbstractEntity() {

    @Id
    @GeneratedValue(generator = "transaction_item_id_transaction_item_seq")
    @Column(name = "id_transaction_item")
    override var id: Long? = null

    @Column(name = "id_external", nullable = false)
    open var idExternal: UUID = UUID.randomUUID()

    @Column(name = "deleted")
    open var deleted: Boolean = false

    @Column(name = "created_at")
    @Convert(converter = LocalDateConverter::class)
    open var createdAt: LocalDate = LocalDate.now()


    companion object{
        const val TABLE_NAME = "transaction_item"
        const val ID_EXTERNAL__UUID = "idExternal"
    }
}