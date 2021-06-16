package com.joutvhu.fixedwith.parser.contrib.kotlin

import com.joutvhu.fixedwidth.parser.module.FixedModule

class KotlinModule : FixedModule() {
    init {
        reader<KotlinDataClassReader>()
    }
}
