package com.example.sms

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/messages")
@RestController
class MessageController(
    private val templateRepository: TemplateRepository,
    private val messageRepository: MessageRepository,
    private val sender: Sender
) {
    @GetMapping
    fun getSentMessages(): List<Message> = messageRepository.findAll()

    @PostMapping
    fun sendMessage(
        @RequestParam phoneNumber: String,
        @RequestParam language: Language,
        @RequestParam event: Event,
        @RequestBody variables: Map<String, String>
    ): ResponseEntity<Unit> {
        val template = templateRepository.findByEventAndLanguage(event, language)
        return if (template != null) {
            sender.send(phoneNumber, template, variables)
            ResponseEntity.accepted().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}
