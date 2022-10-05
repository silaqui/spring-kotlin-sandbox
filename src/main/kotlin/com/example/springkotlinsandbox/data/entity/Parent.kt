package com.example.springkotlinsandbox.data.entity

import javax.persistence.*

@Entity
data class Parent(

    @Id
    @GeneratedValue
    val id: Int? = null,

    val name: String,

    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_id")
    val children: List<Child> = emptyList(),
) {
    override fun toString() = "{name: ${this.name}, children: ${children.map { it.name }}}"
}