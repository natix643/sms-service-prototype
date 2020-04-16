package com.example.sms

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.MongoRepository

@Document
data class Template(
    @Id val id: String? = null,
    val event: Event,
    val language: Language,
    val text: String
    // TODO val businessId: String
)

enum class Event(
    vararg val variableNames: String
) {
    DELIVERY_PICKUP,
    DELIVERY_IM_HERE("proofCode");
}

enum class Language {
    EN, CS, SK
}

interface TemplateRepository : MongoRepository<Template, String> {
    fun findByEventAndLanguage(event: Event, language: Language): Template?
    fun findAllByEvent(event: Event): List<Template>
}
