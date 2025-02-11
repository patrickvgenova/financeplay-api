package com.example.financeplayapi.domain.entity

import com.example.financeplayapi.commons.persistence.AbstractEntity
import com.example.financeplayapi.commons.persistence.converter.DateTimeConverter
import jakarta.persistence.*
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.Parameter
import org.joda.time.DateTime

@Entity
@Table(name = FeaturePrice.TABLE_NAME)
@GenericGenerator(
    name = "feature_price_id_feature_price_seq",
    strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
    parameters = [
        Parameter(name = "sequence_name", value = "feature_price_id_feature_price_seq"),
        Parameter(name = "initial_value", value = "1"),
        Parameter(name = "increment_size", value = "1"),
    ],
)
open class FeaturePrice(
    @Column(name = "feature_name", nullable = false, unique = true)
    open var featureName: String,

    @Column(name = "coins_price", nullable = false)
    open var coinsPrice: Long,

    @Column(name = "description", nullable = true)
    open var description: String? = null,

    @Column(name = "active", nullable = false)
    open var active: Boolean = true,
) : AbstractEntity() {

    @Id
    @GeneratedValue(generator = "feature_price_id_feature_price_seq")
    @Column(name = "id_feature_price")
    override var id: Long? = null

    @Column(name = "created_at", nullable = false)
    @Convert(converter = DateTimeConverter::class)
    open var createdAt: DateTime = DateTime.now()

    @Column(name = "updated_at", nullable = false)
    @Convert(converter = DateTimeConverter::class)
    open var updatedAt: DateTime = DateTime.now()

    companion object {
        const val TABLE_NAME = "feature_price"
        const val FEATURE_NAME__STRING = "feature_name"
        const val COINS_PRICE__BIGINT = "coins_price"
        const val DESCRIPTION__TEXT = "description"
        const val CREATED_AT__TIMESTAMP = "created_at"
        const val UPDATED_AT__TIMESTAMP = "updated_at"
        const val ACTIVE__BOOLEAN = "active"
    }

    fun updateFrom(
        featureName: String? = null,
        coinsPrice: Long? = null,
        description: String? = null,
        active: Boolean? = null,
    ): FeaturePrice {
        val ret = (this.featureName != featureName) ||
                (this.coinsPrice != coinsPrice) ||
                (this.description != description) ||
                (this.active != active)

        if (ret) {
            featureName?.let { this.featureName = it }
            coinsPrice?.let { this.coinsPrice = it }
            description?.let { this.description = it }
            active?.let { this.active = it }
            updatedAt = DateTime.now()
        }
        return this
    }
}