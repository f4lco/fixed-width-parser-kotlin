package com.joutvhu.fixedwith.parser.contrib.kotlin

import com.joutvhu.fixedwidth.parser.convert.FixedWidthReader
import com.joutvhu.fixedwidth.parser.support.FixedTypeInfo
import com.joutvhu.fixedwidth.parser.support.ReadStrategy
import com.joutvhu.fixedwidth.parser.support.StringAssembler
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.findParameterByName
import kotlin.reflect.full.primaryConstructor

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
        }
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
}
