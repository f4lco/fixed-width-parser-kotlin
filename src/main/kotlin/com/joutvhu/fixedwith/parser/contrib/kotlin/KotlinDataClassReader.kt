package com.joutvhu.fixedwith.parser.contrib.kotlin

import com.joutvhu.fixedwidth.parser.convert.FixedWidthReader
import com.joutvhu.fixedwidth.parser.support.FixedTypeInfo
import com.joutvhu.fixedwidth.parser.support.ReadStrategy
import com.joutvhu.fixedwidth.parser.support.StringAssembler
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.findParameterByName
import kotlin.reflect.full.primaryConstructor

/**
 * Reader for immutable Kotlin data classes.
 *
 * This reader lifts requirements of the built-in [com.joutvhu.fixedwidth.parser.convert.reader.ObjectReader],
 * which requires the class to have a no-args constructor and mutable fields.
 *
 * Both constraints do not apply to the typical (immutable) Kotlin data class. To this end, we change the strategy to
 * locate the primary constructor (which Kotlin guarantees data class have), fill a map from parameters to values,
 * and use that map to call the constructor.
 *
 * Also, this reader supports optional arguments (arguments with default values). If the field's value is absent from
 * the data, and the constructor argument has a default value, the default value will be used.
 */
class KotlinDataClassReader(
    info: FixedTypeInfo,
    strategy: ReadStrategy,
) : FixedWidthReader<Any>(
    info,
    strategy,
) {

    init {
        if (info.fixedObject == null) reject()
        if (!info.type.kotlin.isData) reject()
    }

    override fun read(assembler: StringAssembler): Any? {
        val ctor = findPrimaryConstructor(info)
        val parametersToValues = info.elementTypeInfo.associate { child ->
            val param = findParameterByName(ctor, child)
            val value = read(child, assembler.child(child))
            param to value
        }.filter(::filterAbsentOptionalParameters)
        return ctor.callBy(parametersToValues)
    }

    private fun findPrimaryConstructor(info: FixedTypeInfo): KFunction<*> {
        return info.type.kotlin.primaryConstructor
            ?: throw IllegalArgumentException("No primary constructor on ${info.type.kotlin}")
    }

    private fun findParameterByName(
        f: KFunction<*>,
        info: FixedTypeInfo,
    ): KParameter {
        return f.findParameterByName(info.field.name)
            ?: throw IllegalArgumentException("No parameter named ${info.name} on $f")
    }

    private fun filterAbsentOptionalParameters(
        entry: Map.Entry<KParameter, Any?>,
    ): Boolean {
        val (parameter, value) = entry
        if (parameter.isOptional) {
            return value != null
        }
        return true
    }
}
