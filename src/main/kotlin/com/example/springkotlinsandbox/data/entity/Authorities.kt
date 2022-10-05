package com.example.springkotlinsandbox.data.entity

import javax.persistence.*

@Entity
data class Authorities(

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Int = -1,

    val authority: String,

    @ManyToMany(mappedBy = "authorities")
    val roles: Collection<Roles> = emptyList()
)