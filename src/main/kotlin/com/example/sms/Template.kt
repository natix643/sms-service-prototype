package com.example.sms

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.MongoRepository

@Document
data class Template(
    @Id val id: String? = null,
    val type: SmsType,
    val businessId: String?,
    val send: Boolean,
    val translations: Map<Language, String>
)

enum class Language {
    EN, CS, SK
}

interface TemplateRepository : MongoRepository<Template, String> {
    fun findByTypeAndBusinessId(type: SmsType, businessId: String?): Template?
    fun findAllByBusinessId(businessId: String?): List<Template>
    fun findAllByType(type: SmsType): List<Template>
}
