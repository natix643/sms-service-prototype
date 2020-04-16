package com.example.sms

import com.example.sms.Gateway.TWILLIO
import org.slf4j.LoggerFactory
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.accepted
import org.springframework.http.ResponseEntity.notFound
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.Instant

@Document
data class Message(
    @Id val id: String? = null,
    val sentAt: Instant = Instant.now(),
    val event: Event,
    val language: Language,
    val text: String,
    val gateway: Gateway,
    val phoneNumber: String
)

enum class Gateway {
    TWILLIO,
    NEXMO,
    DAKTELA
}

interface MessageRepository : MongoRepository<Message, String>

interface Sender {
    val gateway: Gateway
    fun send(phoneNumber: String, template: Template, variables: Map<String, String>)

    fun render(templateText: String, variables: Map<String, String>): String {
        var result = templateText
        variables.forEach { (name, value) ->
            result = result.replace("\${$name}", value)
        }
        return result
    }
}

@Service
class TwillioSender(
    private val messageRepository: MessageRepository
) : Sender {
    private val logger = LoggerFactory.getLogger(TwillioSender::class.java)
    override val gateway = TWILLIO

    override fun send(phoneNumber: String, template: Template, variables: Map<String, String>) {
        val message = Message(
            event = template.event,
            language = template.language,
            text = render(template.text, variables),
            gateway = gateway,
            phoneNumber = phoneNumber
        )
        val savedMessage = messageRepository.save(message)
        logger.info("sending message id=${savedMessage.id} event=${template.event} phoneNumber=$phoneNumber thru=$gateway")
    }
}

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
        @RequestBody bindings: Map<String, String>
    ): ResponseEntity<Unit> {
        val template = templateRepository.findByEventAndLanguage(event, language)
        return if (template != null) {
            sender.send(phoneNumber, template, bindings)
            accepted().build()
        } else {
            notFound().build()
        }
    }
}
