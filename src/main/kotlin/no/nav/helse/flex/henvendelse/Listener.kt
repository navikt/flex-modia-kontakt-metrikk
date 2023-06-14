package no.nav.helse.flex.henvendelse

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Component

@Component
class Listener(
    val henvendelseRepository: HenvendelseRepository
) {
    @KafkaListener(
        topics = [topic],
        containerFactory = "aivenKafkaListenerContainerFactory"
    )
    fun listen(cr: ConsumerRecord<String, String>, acknowledgment: Acknowledgment) {
        henvendelseRepository.save(cr.value().tilHenvendelseKafkaDTO().tilHenvendelseDbRecord())
        acknowledgment.acknowledge()
    }
}

private fun HenvendelseKafkaDTO.tilHenvendelseDbRecord(): HenvendelseDbRecord = HenvendelseDbRecord(
    id = null,
    fnr = fnr,
    tema = tema,
    tidspunkt = tidspunkt
)

const val topic = "personoversikt.henvendelse-oppdatering-melding"
