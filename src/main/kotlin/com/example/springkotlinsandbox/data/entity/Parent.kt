package com.example.springkotlinsandbox.data.entity

import javax.persistence.*

@Entity
data class Parent(
    @Id
    @GeneratedValue
    var id: Int? = null,
    var name: String = "",
    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_id")
    var children: List<Child> = listOf(),
) {
    override fun toString() = "{name: ${this.name}, children: ${children.map { it.name }}}"
}