package com.example.springkotlinsandbox.data.repository

import com.example.springkotlinsandbox.data.entity.Child
import com.example.springkotlinsandbox.data.entity.Parent
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class OneToManyTest @Autowired constructor(
    private val childRepository: ChildRepository,
    private val parentRepository: ParentRepository
) {

    @BeforeEach
    fun setUp() {
        val childA = Child(name = "Child A")
        val childB = Child(name = "Child B")
        val parentOne = Parent(name = "Parent 1", children = listOf(childA, childB))
        parentRepository.save(parentOne)

        val parentTwo = Parent(name = "Parent 2")
        val childC = Child(name = "Child C", parent = parentTwo)
        childRepository.save(childC)
    }

    @AfterEach
    fun clean() {
        childRepository.deleteAll()
        parentRepository.deleteAll()
    }

    @Test
    fun `There should be 2 parent`() {
        val actual = parentRepository.count()

        assertEquals(2, actual)
    }

    @Test
    fun `There should be 3 children`() {
        val actual = childRepository.count()

        assertEquals(3, actual)
    }

    @Test
    fun `Parent should have child when saved by parent`() {
        val actual = parentRepository.findAll().toList().filter { it.name == "Parent 1" }[0]

        assertEquals(2, actual.children.size)
    }

    @Test
    fun `Child should have parent when saved by parent`() {
        val actual = childRepository.findAll().toList().filter { it.name != "Child C" }

        assertEquals(2, actual.size)
        assertNotNull(actual[0].parent)
        assertNotNull(actual[1].parent)
    }

    @Test
    fun `Parent should have child when saved by child`() {
        val actual = parentRepository.findAll().toList().filter { it.name == "Parent 2" }[0]

        assertEquals(1, actual.children.size)
    }

    @Test
    fun `Child should have parent when saved by child`() {
        val actual = childRepository.findAll().toList().filter { it.name == "Child C" }[0]

        assertNotNull(actual.parent)
    }

    @Test
    fun read() {
        for (p in parentRepository.findAll()) println(p)
        for (c in childRepository.findAll()) println(c)
    }
}