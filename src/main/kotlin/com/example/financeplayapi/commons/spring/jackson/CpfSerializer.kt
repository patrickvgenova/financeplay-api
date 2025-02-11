package com.example.financeplayapi.commons.spring.jackson

import com.example.financeplayapi.commons.core.type.CPF
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer

class CpfSerializer() : StdSerializer<CPF>(CPF::class.java) {

    override fun serialize(value: CPF?, gen: JsonGenerator, provider: SerializerProvider?) {
        if (value == null) {
            gen.writeNull()
        } else {
            gen.writeString(value.printNumbers())
        }
    }
}
