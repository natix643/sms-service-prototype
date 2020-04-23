package com.example.sms

import com.example.sms.Variable.PROOF_CODE
import com.example.sms.Variable.REGISTRATION_CODE
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.MongoRepository

@Document
data class Template(
    @Id val id: String? = null,
    val event: Event,
    val businessId: String?,
    val send: Boolean,
    val translations: Map<Language, String>
)

enum class Event(
    val customizable: Boolean,
    vararg variables: Variable
) {
    REGISTRATION(false, REGISTRATION_CODE),
    DELIVERY_PICKUP(true),
    DELIVERY_IM_HERE(true, PROOF_CODE);

    val variables = variables.toList()
}

enum class Variable {
    REGISTRATION_CODE,
    PROOF_CODE
}

enum class Language {
    EN, CS, SK
}

interface TemplateRepository : MongoRepository<Template, String> {
    fun findByEventAndBusinessId(event: Event, businessId: String?): Template?
    fun findAllByBusinessId(businessId: String?): List<Template>
    fun findAllByEvent(event: Event): List<Template>
}
