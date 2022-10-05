package com.example.springkotlinsandbox.data.entity

import javax.persistence.*

@Entity
data class Child(

    @Id
    @GeneratedValue
    val id: Int? = null,

    val name: String,

    @ManyToOne(cascade = [CascadeType.PERSIST], fetch = FetchType.EAGER)
    val parent: Parent? = null,
) {

    override fun toString(): String = "{name: ${name}, parent: ${parent?.name}}"
}