package no.nav.helse.flex.henvendelse

import no.nav.helse.flex.objectMapper
import java.time.Instant

fun String.tilHenvendelseKafkaDTO(): HenvendelseKafkaDTO {
    return objectMapper.readValue(this, HenvendelseKafkaDTO::class.java)
}

data class HenvendelseKafkaDTO(
    val fnr: String,
    val tema: String?,
    val temagruppe: String,
    val traadId: String,
    val tidspunkt: Instant
)
