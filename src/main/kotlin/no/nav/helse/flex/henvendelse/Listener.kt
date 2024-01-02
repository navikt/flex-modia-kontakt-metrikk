package no.nav.helse.flex.henvendelse

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.dao.DuplicateKeyException
import org.springframework.data.relational.core.conversion.DbActionExecutionException
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Component

@Component
class Listener(
    val henvendelseRepository: HenvendelseRepository,
) {
    @KafkaListener(
        topics = [TOPIC],
        containerFactory = "aivenKafkaListenerContainerFactory",
    )
    fun listen(
        cr: ConsumerRecord<String, String>,
        acknowledgment: Acknowledgment,
    ) {
        val henvendelseKafkaDTO = cr.value().tilHenvendelseKafkaDTO()
        lagreHenvendelse(henvendelseKafkaDTO)
        acknowledgment.acknowledge()
    }

    fun lagreHenvendelse(henvendelseKafkaDTO: HenvendelseKafkaDTO) {
        try {
            henvendelseRepository.save(henvendelseKafkaDTO.tilHenvendelseDbRecord())
        } catch (e: DbActionExecutionException) {
            if (e.cause is DuplicateKeyException) {
                return
            }
            throw e
        }
    }
}

private fun HenvendelseKafkaDTO.tilHenvendelseDbRecord(): HenvendelseDbRecord =
    HenvendelseDbRecord(
        id = null,
        fnr = fnr,
        tema = tema,
        temagruppe = temagruppe,
        traadId = traadId,
        tidspunkt = tidspunkt,
    )

const val TOPIC = "personoversikt.henvendelse-oppdatering-melding"
