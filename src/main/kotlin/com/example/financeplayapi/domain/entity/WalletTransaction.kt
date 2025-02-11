package com.example.financeplayapi.domain.entity

import com.example.financeplayapi.commons.core.type.Money
import com.example.financeplayapi.commons.persistence.AbstractEntity
import com.example.financeplayapi.commons.persistence.converter.MoneyConverter
import jakarta.persistence.*
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.Parameter
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction

@Entity
@Table(name = WalletTransaction.TABLE_NAME)
@GenericGenerator(
    name = "wallet_transaction_id_wallet_transaction_seq",
    strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
    parameters = [
        Parameter(name = "sequence_name", value = "wallet_transaction_id_wallet_transaction_seq"),
        Parameter(name = "initial_value", value = "1"),
        Parameter(name = "increment_size", value = "1"),
    ],
)
open class WalletTransaction(

//    @ManyToOne
//    @JoinColumn(name = User.TABLE_NAME)
//    open var user: User,

    @ManyToOne
    @JoinColumn(name = AbstractTransaction.COL__ID_TRANSACTION__LONG)
    open var transaction: AbstractTransaction,

    @ManyToOne
    @JoinColumn(name = Wallet.COL__ID_WALLET__LONG)
    open var wallet: Wallet,

    @Column(name = "value")
    @Convert(converter = MoneyConverter::class)
    open var value: Money

    ) : AbstractEntity() {

    @Id
    @GeneratedValue(generator = "wallet_transaction_id_wallet_transaction_seq")
    @Column(name = "id_wallet_transaction")
    override var id: Long? = null

    companion object {
        const val TABLE_NAME = "wallet_transaction"
        const val TRANSACTION__TRANSACTION = "transaction"
        const val WALLET__WALLET = "wallet"
        const val ID__LONG = "id"
    }
}