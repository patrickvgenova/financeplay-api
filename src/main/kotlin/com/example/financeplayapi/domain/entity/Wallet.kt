package com.example.financeplayapi.domain.entity

import com.example.financeplayapi.commons.persistence.AbstractEntity
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
@Table(name = Wallet.TABLE_NAME)
@SQLDelete(sql = "UPDATE ${Wallet.TABLE_NAME} SET deleted = true WHERE id_wallet = ?")
@SQLRestriction("deleted = false")
@GenericGenerator(
    name = "wallet_id_wallet_seq",
    strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
    parameters = [
        Parameter(name = "sequence_name", value = "wallet_id_wallet_seq"),
        Parameter(name = "initial_value", value = "1"),
        Parameter(name = "increment_size", value = "1"),
    ],
)
open class Wallet(

    @Column(name = "name", nullable = false)
    open var name: String,

    @Column(name = "color", nullable = false)
    open var color: String,

    @ManyToOne
    @JoinColumn(name = "id_user")
    open var user: User,

    ) : AbstractEntity() {

    @Id
    @GeneratedValue(generator = "wallet_id_wallet_seq")
    @Column(name = "id_wallet")
    override var id: Long? = null

    @Column(name = "deleted")
    open var deleted: Boolean = false


    fun updateFrom(
        name: String?,
        color: String?,
    ): Wallet {
        name?.let { this.name = it }
        color?.let { this.color = it }
        return this
    }

    companion object {
        const val TABLE_NAME = "wallet"
        const val USER__USER = "user"
        const val NAME__STRING = "name"
        const val ID__LONG = "id"
        const val COL__ID_WALLET__LONG = "id_wallet"
    }
}