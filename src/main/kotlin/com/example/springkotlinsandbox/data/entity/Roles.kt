package com.example.springkotlinsandbox.data.entity

import javax.persistence.*

@Entity
data class Roles(

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Int = -1,

        val role: String,

        @ManyToMany(mappedBy = "roles")
        val users: Collection<Users> = emptyList(),

        @ManyToMany
        @JoinTable(
                name = "roles_authorities",
                joinColumns = [JoinColumn(name = "role_id", referencedColumnName = "id")],
                inverseJoinColumns = [JoinColumn(name = "authority_id", referencedColumnName = "id")]
        )
        val authorities: Collection<Authorities>
)
