package com.joutvhu.fixedwith.parser.contrib.kotlin

import com.joutvhu.fixedwidth.parser.FixedParser
import com.joutvhu.fixedwidth.parser.annotation.FixedField
import com.joutvhu.fixedwidth.parser.annotation.FixedObject
import com.joutvhu.fixedwidth.parser.domain.KeepPadding
import com.joutvhu.fixedwidth.parser.module.DefaultModule
import com.joutvhu.fixedwidth.parser.module.FixedModule
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class KotlinDataClassReaderTest {

    @Test
    fun canReadDataClass() {
        val parser = FixedParser.parser().use(combinedModule())
        val person = Person(name = "Max", age = 32)
        val line = parser.export(person)

        val read = parser.parse(Person::class.java, line)

        assertEquals(person, read)
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
