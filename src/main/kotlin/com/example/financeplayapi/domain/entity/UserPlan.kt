package com.example.financeplayapi.domain.entity

import com.example.financeplayapi.commons.persistence.AbstractEntity
import com.example.financeplayapi.commons.persistence.converter.DateTimeConverter
import jakarta.persistence.*
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.Parameter
import org.joda.time.DateTime

@Entity
@Table(name = UserPlan.TABLE_NAME)
@GenericGenerator(
    name = "user_plan_id_user_plan_seq",
    strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
    parameters = [
        Parameter(name = "sequence_name", value = "user_plan_id_user_plan_seq"),
        Parameter(name = "initial_value", value = "1"),
        Parameter(name = "increment_size", value = "1"),
    ],
)
open class UserPlan(
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id_user", nullable = false)
    open var user: User,

    @ManyToOne
    @JoinColumn(name = "plan", referencedColumnName = "id_plan", nullable = true)
    open var plan: Plan? = null,

    @ManyToOne
    @JoinColumn(name = "last_plan", referencedColumnName = "id_plan", nullable = true)
    open var lastPlan: Plan? = null,
) : AbstractEntity() {

    @Id
    @GeneratedValue(generator = "user_plan_id_user_plan_seq")
    @Column(name = "id_user_plan")
    override var id: Long? = null

    @Column(name = "updated_at", nullable = false)
    @Convert(converter = DateTimeConverter::class)
    open var updatedAt: DateTime = DateTime.now()

    fun updateFrom(
        plan: Plan? = null,
        lastPlan: Plan? = null,
    ): UserPlan {
        val ret = (this.plan != plan) ||
                (this.lastPlan != lastPlan)

        if (ret) {
            plan?.let { this.plan = it }
            lastPlan?.let { this.lastPlan = it }
            updatedAt = DateTime.now()
        }
        return this
    }


    companion object {
        const val TABLE_NAME = "user_plan"
        const val USER_ID__BIGINT = "user_id"
        const val PLAN__BIGINT = "plan"
        const val LAST_PLAN__BIGINT = "last_plan"
        const val UPDATED_AT__TIMESTAMP = "updated_at"
    }
}