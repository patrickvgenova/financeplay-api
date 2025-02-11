package com.example.financeplayapi.commons.spring.jackson

import com.example.financeplayapi.commons.core.type.Email
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer

class EmailSerializer() : StdSerializer<Email>(Email::class.java) {

    override fun serialize(value: Email?, gen: JsonGenerator, provider: SerializerProvider?) {
        if (value == null) {
            gen.writeNull()
        } else {
            gen.writeString(value.toEmailString())
        }
    }
}
