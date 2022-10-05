package com.example.springkotlinsandbox.data.entity

import javax.persistence.*

@Entity
data class Users(

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Int = -1,

    val username: String,

    val password: String,

    val enabled: Boolean = true,

    @ManyToMany
    @JoinTable(
        name = "users_roles",
        joinColumns = [JoinColumn(name = "user_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "role_id", referencedColumnName = "id")]
    )
    val roles: Set<Roles>
)