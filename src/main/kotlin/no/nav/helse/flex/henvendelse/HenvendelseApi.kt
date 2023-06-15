package no.nav.helse.flex.henvendelse

import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1")
class HenvendelseApi(
    private val feedbackRepository: HenvendelseRepository
) {

    @GetMapping("/henvendelse")
    @ResponseBody
    fun hentFeedback(): List<HenvendelseDbRecord> {
        return feedbackRepository.findAll().toList()
    }
}
