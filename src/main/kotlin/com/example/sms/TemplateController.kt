package com.example.sms

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/sms/templates")
class TemplateController(
    private val repository: TemplateRepository
) {

    @GetMapping("/{type}")
    fun getAllByType(@PathVariable type: SmsType): List<Template> {
        return repository.findAllByType(type)
    }

    @GetMapping("/{type}/default")
    fun getByTypeDefault(
        @PathVariable type: SmsType
    ): ResponseEntity<Template> {
        val template = repository.findByTypeAndBusinessId(type, null)
        return if (template != null) ResponseEntity.ok(template)
        else ResponseEntity.notFound().build()
    }

    @GetMapping("/{type}/business/{businessId}")
    fun getByTypeAndBusiness(
        @PathVariable type: SmsType,
        @PathVariable businessId: String
    ): ResponseEntity<Template> {
        val template = repository.findByTypeAndBusinessId(type, businessId)
        return if (template != null) ResponseEntity.ok(template)
        else ResponseEntity.notFound().build()
    }

    @PostMapping
    fun save(@RequestBody r: SaveTemplateRequest) {
        val old = repository.findByTypeAndBusinessId(r.type, r.businessId)
        val new = old?.copy(
            send = r.send,
            translations = r.translations
        ) ?: Template(
            type = r.type,
            businessId = r.businessId,
            send = r.send,
            translations = r.translations
        )
        repository.save(new)
    }
}

data class SaveTemplateRequest(
    val type: SmsType,
    val businessId: String?,
    val send: Boolean,
    val translations: Map<Language, String>
)
