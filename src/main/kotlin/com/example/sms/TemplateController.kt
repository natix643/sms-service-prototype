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

    @GetMapping("/{event}")
    fun getAllByEvent(@PathVariable event: Event): List<Template> {
        return repository.findAllByEvent(event)
    }

    @GetMapping("/{event}/default")
    fun getByEventDefault(
        @PathVariable event: Event
    ): ResponseEntity<Template> {
        val template = repository.findByEventAndBusinessId(event, null)
        return if (template != null) ResponseEntity.ok(template)
        else ResponseEntity.notFound().build()
    }

    @GetMapping("/{event}/business/{businessId}")
    fun getByEventAndBusiness(
        @PathVariable event: Event,
        @PathVariable businessId: String
    ): ResponseEntity<Template> {
        val template = repository.findByEventAndBusinessId(event, businessId)
        return if (template != null) ResponseEntity.ok(template)
        else ResponseEntity.notFound().build()
    }

    @PostMapping
    fun save(@RequestBody r: SaveTemplateRequest) {
        val old = repository.findByEventAndBusinessId(r.event, r.businessId)
        val new = old?.copy(
            send = r.send,
            translations = r.translations
        ) ?: Template(
            event = r.event,
            businessId = r.businessId,
            send = r.send,
            translations = r.translations
        )
        repository.save(new)
    }
}

data class SaveTemplateRequest(
    val event: Event,
    val businessId: String?,
    val send: Boolean,
    val translations: Map<Language, String>
)
