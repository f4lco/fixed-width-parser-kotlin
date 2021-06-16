package com.joutvhu.fixedwith.parser.contrib.kotlin

import com.joutvhu.fixedwidth.parser.convert.FixedWidthReader
import com.joutvhu.fixedwidth.parser.convert.FixedWidthValidator
import com.joutvhu.fixedwidth.parser.convert.FixedWidthWriter
import com.joutvhu.fixedwidth.parser.module.FixedModule

inline fun <reified T : FixedWidthReader<*>> FixedModule.reader() {
    readers.add(T::class.java)
}

inline fun <reified T : FixedWidthWriter<*>> FixedModule.writer() {
    writers.add(T::class.java)
}

inline fun <reified T : FixedWidthValidator> FixedModule.validator() {
    validators.add(T::class.java)
}
