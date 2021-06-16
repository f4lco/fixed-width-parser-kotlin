package com.joutvhu.fixedwith.parser.contrib.kotlin

import com.joutvhu.fixedwidth.parser.FixedParser
import com.joutvhu.fixedwidth.parser.annotation.FixedField
import com.joutvhu.fixedwidth.parser.annotation.FixedObject
import com.joutvhu.fixedwidth.parser.domain.KeepPadding
import com.joutvhu.fixedwidth.parser.module.DefaultModule
import com.joutvhu.fixedwidth.parser.module.FixedModule
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class KotlinDataClassReaderTest {

    private lateinit var parser: FixedParser

    @BeforeEach
    fun setup() {
        parser = FixedParser.parser().use(combinedModule())
    }

    @Test
    fun canReadDataClass() {
        val person = Person(name = "Max", age = 32)
        val line = parser.export(person)

        val read = parser.parse<Person>(line)

        assertEquals(person, read)
    }

    @Test
    fun optionalParameter_absentInData() {
        val read: Whale = parser.parse("     blue")

        assertEquals(Whale("Herb", "blue"), read)
    }

    @Test
    fun optionalParameter_presentInData() {
        val read: Whale = parser.parse("Emma black")

        assertEquals(Whale("Emma", "black"), read)
    }

    private fun combinedModule(): FixedModule = KotlinModule().merge(DefaultModule())
}

@FixedObject
data class Person(
    @FixedField(start = 0, length = 20, keepPadding = KeepPadding.DROP, label = "name")
    val name: String,
    @FixedField(start = 20, length = 3, label = "my-age")
    val age: Int,
)

@FixedObject
data class Whale(
    @FixedField(length = 5, keepPadding = KeepPadding.DROP, label = "name")
    val name: String = "Herb",
    @FixedField(start = 5, length = 5, keepPadding = KeepPadding.DROP, label = "color")
    val color: String,
)
