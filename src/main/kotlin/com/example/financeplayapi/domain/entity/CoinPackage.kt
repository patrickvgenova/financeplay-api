package com.example.financeplayapi.domain.entity

import com.example.financeplayapi.commons.persistence.AbstractEntity
import com.example.financeplayapi.commons.persistence.converter.DateTimeConverter
import jakarta.persistence.*
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.Parameter
import org.joda.time.DateTime
import java.math.BigDecimal

@Entity
@Table(name = CoinPackage.TABLE_NAME)
@GenericGenerator(
    name = "coin_package_id_coin_package_seq",
    strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
    parameters = [
        Parameter(name = "sequence_name", value = "coin_package_id_coin_package_seq"),
        Parameter(name = "initial_value", value = "1"),
        Parameter(name = "increment_size", value = "1"),
    ],
)
open class CoinPackage(
    @Column(name = "name", nullable = false)
    open var name: String,

    @Column(name = "coins_amount", nullable = false)
    open var coinsAmount: Long,

    @Column(name = "price", nullable = false, precision = 15, scale = 2)
    open var price: BigDecimal,

    @Column(name = "active", nullable = false)
    open var active: Boolean = true,
) : AbstractEntity() {

    @Id
    @GeneratedValue(generator = "coin_package_id_coin_package_seq")
    @Column(name = "id_coin_package")
    override var id: Long? = null

    @Column(name = "created_at", nullable = false)
    @Convert(converter = DateTimeConverter::class)
    open var createdAt: DateTime = DateTime.now()

    @Column(name = "updated_at", nullable = false)
    @Convert(converter = DateTimeConverter::class)
    open var updatedAt: DateTime = DateTime.now()

    companion object {
        const val TABLE_NAME = "coin_package"
        const val NAME__STRING = "name"
        const val COINS_AMOUNT__BIGINT = "coins_amount"
        const val PRICE__DECIMAL = "price"
        const val ACTIVE__BOOLEAN = "active"
        const val CREATED_AT__TIMESTAMP = "created_at"
        const val UPDATED_AT__TIMESTAMP = "updated_at"
    }

    fun updateFrom(
        name: String? = null,
        coinsAmount: Long? = null,
        price: BigDecimal? = null,
        active: Boolean? = null,
    ): CoinPackage {
        val ret = (this.name != name) ||
                (this.coinsAmount != coinsAmount) ||
                (this.price != price) ||
                (this.active != active)

        if (ret) {
            name?.let { this.name = it }
            coinsAmount?.let { this.coinsAmount = it }
            price?.let { this.price = it }
            active?.let { this.active = it }
            updatedAt = DateTime.now()
        }
        return this
    }
}