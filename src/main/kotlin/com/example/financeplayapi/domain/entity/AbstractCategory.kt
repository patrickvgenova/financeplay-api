package com.example.financeplayapi.domain.entity

import com.example.financeplayapi.commons.persistence.AbstractEntity
import jakarta.persistence.Column
import jakarta.persistence.DiscriminatorColumn
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
@Table(name = AbstractCategory.TABLE_NAME)
@SQLDelete(sql = "UPDATE ${AbstractCategory.TABLE_NAME} SET deleted = true WHERE id = ?")
@SQLRestriction("deleted = false")
@DiscriminatorColumn(name = "type")
@GenericGenerator(
    name = "category_id_category_seq",
    strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
    parameters = [
        Parameter(name = "sequence_name", value = "category_id_category_seq"),
        Parameter(name = "initial_value", value = "1"),
        Parameter(name = "increment_size", value = "1"),
    ],
)
open class AbstractCategory(

    @Column(name = "name", nullable = false)
    open var name: String,

    @Column(name = "color", nullable = false)
    open var color: String,

    @ManyToOne
    @JoinColumn(name = "id_user")
    open var user: User,

): AbstractEntity() {

    @Id
    @GeneratedValue(generator = "category_id_category_seq")
    @Column(name = "id_category")
    override var id: Long? = null

    @Column(name = "deleted")
    open var deleted: Boolean = false

    companion object{
        const val TABLE_NAME = "category"
    }
}