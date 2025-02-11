package com.example.financeplayapi.commons.spring.config

import com.example.financeplayapi.commons.spring.jackson.CpfDeserializer
import com.example.financeplayapi.commons.spring.jackson.CpfSerializer
import com.example.financeplayapi.commons.spring.jackson.DateTimeDeserializer
import com.example.financeplayapi.commons.spring.jackson.DateTimeSerializer
import com.example.financeplayapi.commons.spring.jackson.EmailDeserializer
import com.example.financeplayapi.commons.spring.jackson.EmailSerializer
import com.example.financeplayapi.commons.spring.jackson.LocalDateDeserializer
import com.example.financeplayapi.commons.spring.jackson.LocalDateSerializer
import com.example.financeplayapi.commons.spring.jackson.LocalTimeDeserializer
import com.example.financeplayapi.commons.spring.jackson.LocalTimeSerializer
import com.example.financeplayapi.commons.spring.jackson.MoneyDeserializer
import com.example.financeplayapi.commons.spring.jackson.MoneySerializer
import com.example.financeplayapi.commons.spring.jackson.RateDeserializer
import com.example.financeplayapi.commons.spring.jackson.RateSerializer
import com.example.financeplayapi.commons.spring.jackson.YearMonthDeserializer
import com.example.financeplayapi.commons.spring.jackson.YearMonthSerializer
import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder

@Configuration
open class TypeConverterConfigurartion {

    @Bean
    open fun jackson2ObjectMapperBuilder(): Jackson2ObjectMapperBuilder {
        val serializers = arrayOf(
            LocalDateSerializer(),
            DateTimeSerializer(),
            LocalTimeSerializer(),
            YearMonthSerializer(),
            CpfSerializer(),
            EmailSerializer(),
            MoneySerializer(),
            RateSerializer(),
        )

        val deserializers = arrayOf(
            LocalDateDeserializer(),
            DateTimeDeserializer(),
            LocalTimeDeserializer(),
            YearMonthDeserializer(),
            CpfDeserializer(),
            EmailDeserializer(),
            MoneyDeserializer(),
            RateDeserializer(),
        )

        return Jackson2ObjectMapperBuilder()
            .serializers(*serializers).serializationInclusion(JsonInclude.Include.USE_DEFAULTS)
            .deserializers(*deserializers)
    }
}
