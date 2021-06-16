package com.joutvhu.fixedwith.parser.contrib.kotlin

import com.joutvhu.fixedwidth.parser.FixedParser
import com.joutvhu.fixedwidth.parser.support.ItemReader
import java.io.InputStream
import java.nio.charset.Charset
import java.util.stream.Stream

inline fun <reified T> FixedParser.parse(
    line: String,
): T {
    return parse(T::class.java, line)
}

inline fun <reified T> FixedParser.parse(
    stream: Stream<String>,
): Stream<T> {
    return parse(T::class.java, stream)
}

inline fun <reified T> FixedParser.parse(
    input: InputStream,
): ItemReader<T> {
    return parse(T::class.java, input)
}

inline fun <reified T> FixedParser.parse(
    input: InputStream,
    charset: Charset,
): ItemReader<T> {
    return parse(T::class.java, input, charset.name())
}
