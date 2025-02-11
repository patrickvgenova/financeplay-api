package com.example.financeplayapi.domain.entity

import com.example.financeplayapi.commons.persistence.AbstractEntity
import com.example.financeplayapi.commons.persistence.converter.DateTimeConverter
import com.example.financeplayapi.domain.PlanType
import jakarta.persistence.*
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.Parameter
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction
import org.hibernate.annotations.SQLUpdate
import org.joda.time.DateTime
import java.math.BigDecimal
import java.util.UUID

@Entity
@Table(name = Plan.TABLE_NAME)
@SQLDelete(sql = "UPDATE ${Plan.TABLE_NAME} SET deleted = true WHERE id_plan = ?")
@SQLRestriction("deleted = false")
@GenericGenerator(
    name = "plan_id_plan_seq",
    strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
    parameters = [
        Parameter(name = "sequence_name", value = "plan_id_plan_seq"),
        Parameter(name = "initial_value", value = "1"),
        Parameter(name = "increment_size", value = "1"),
    ],
)
open class Plan(
    @Column(name = "id_external", nullable = false)
    open var idExternal: UUID = UUID.randomUUID(),

    @Column(name = "name", nullable = false)
    open var name: String,

    @Column(name = "description", nullable = true)
    open var description: String? = null,

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    open var type: PlanType = PlanType.FREE,

    @Column(name = "price", nullable = true)
    open var price: BigDecimal? = null,
) : AbstractEntity() {

    @Id
    @GeneratedValue(generator = "plan_id_plan_seq")
    @Column(name = "id_plan")
    override var id: Long? = null

    @Column(name = "deleted", nullable = false)
    open var deleted: Boolean = false

    @Column(name = "created_at", nullable = false)
    @Convert(converter = DateTimeConverter::class)
    open var createdAt: DateTime = DateTime.now()

    fun updateFrom(
        name: String? = null,
        description: String? = null,
        type: PlanType? = null,
        price: BigDecimal? = null,
    ): Plan {
        val ret = (this.name != name) ||
                (this.description != description) ||
                (this.type != type) ||
                (this.price != price)

        if (ret) {
            name?.let { this.name = it }
            description?.let { this.description = it }
            type?.let { this.type = it }
            price?.let { this.price = it }
        }
        return this
    }

    companion object {
        const val TABLE_NAME = "plan"
        const val ID_EXTERNAL__UUID = "idExternal"
        const val NAME__STRING = "name"
        const val DESCRIPTION__TEXT = "description"
        const val TYPE__STRING = "type"
        const val PRICE__DECIMAL = "price"
        const val CREATED_AT__TIMESTAMP = "created_at"
        const val DELETED__BOOLEAN = "deleted"
    }
}