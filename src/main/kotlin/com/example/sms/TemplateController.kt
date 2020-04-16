package com.example.sms

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/templates")
class TemplateController(
    private val repository: TemplateRepository
) {
    @GetMapping
    fun getAllForEvent(@RequestParam event: Event): GetTemplatesResponse {
        val templates = repository.findAllByEvent(event)
        return GetTemplatesResponse(
            variableNames = event.variableNames.toList(),
            templatesByLanguage = templates.map { it.language to it }.toMap()
        )
    }

    @PostMapping
    fun saveTemplate(@RequestBody r: SaveTemplateRequest) {
        val old = repository.findByEventAndLanguage(r.event, r.language)
        val new = old?.copy(text = r.text)
            ?: Template(event = r.event, language = r.language, text = r.text)
        repository.save(new)
    }
}

data class GetTemplatesResponse(
    val variableNames: List<String>,
    val templatesByLanguage: Map<Language, Template>
)

data class SaveTemplateRequest(
    val event: Event,
    val language: Language,
    val text: String
)
