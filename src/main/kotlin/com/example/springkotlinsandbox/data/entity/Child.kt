package com.example.springkotlinsandbox.data.entity

import javax.persistence.*

@Entity
data class Child(
    @Id
    @GeneratedValue
    var id: Int? = null,
    var name: String = "",
    @ManyToOne(cascade = [CascadeType.PERSIST], fetch = FetchType.EAGER)
    var parent: Parent? = null,
) {

    override fun toString(): String = "{name: ${name}, parent: ${parent?.name}}"
}