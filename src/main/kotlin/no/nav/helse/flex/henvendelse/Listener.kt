package no.nav.helse.flex.henvendelse

import no.nav.helse.flex.logger
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Component

@Component
class Listener(
    val henvendelseRepository: HenvendelseRepository
) {

    val log = logger()

    @KafkaListener(
        topics = [topic],
        containerFactory = "aivenKafkaListenerContainerFactory"
    )
    fun listen(cr: ConsumerRecord<String, String>, acknowledgment: Acknowledgment) {
        val henvendelseKafkaDTO = cr.value().tilHenvendelseKafkaDTO()
        henvendelseRepository.save(henvendelseKafkaDTO.tilHenvendelseDbRecord())
        log.info("Mottok og lagret $henvendelseKafkaDTO") // TODO skal bort innen prod
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
