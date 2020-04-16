package com.example.sms

import com.example.sms.Language.CS
import com.example.sms.Language.EN
import com.example.sms.Event.DELIVERY_IM_HERE
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

fun main(args: Array<String>) {
    runApplication<SmsApplication>(*args)
}

@SpringBootApplication
class SmsApplication {
    @Bean
    fun initDb(templateRepository: TemplateRepository) = ApplicationRunner {
        templateRepository.saveAll(listOf(
            Template(
                event = DELIVERY_IM_HERE,
                language = EN,
                text = "driver is here, your code is \${proofCode}"
            ),
            Template(
                event = DELIVERY_IM_HERE,
                language = CS,
                text = "řidič je tady, váš kód je \${proofCode}"
            )
        ))
    }
}
