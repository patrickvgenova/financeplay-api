package com.example.financeplayapi.commons.spring.jackson

import com.example.financeplayapi.commons.core.type.Email
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdDeserializer

class EmailDeserializer() : StdDeserializer<Email>(Email::class.java) {

    override fun deserialize(p: JsonParser, ctxt: DeserializationContext?): Email {
        val text = p.text
        return Email(text)
    }
}
