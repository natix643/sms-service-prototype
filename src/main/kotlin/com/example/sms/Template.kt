package com.example.sms

import com.example.sms.Variable.PROOF_CODE
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

enum class Event(vararg variables: Variable) {
    DELIVERY_PICKUP,
    DELIVERY_IM_HERE(PROOF_CODE);

    val variables = variables.toList()
}

enum class Variable {
    PROOF_CODE
}

enum class Language {
    EN, CS, SK
}

interface TemplateRepository : MongoRepository<Template, String> {
    fun findByEventAndBusinessId(event: Event, businessId: String?): Template?
    fun findAllByEvent(event: Event): List<Template>
}
