package fr.gouv.dgampa.rapportnav.domain.repositories.mission

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.MissionCrewEntity
import java.time.ZonedDateTime

interface IRpnExportRepository {

    fun exportOdt(
        service: String?,
        id: String,
        startDateTime: ZonedDateTime?,
        endDateTime: ZonedDateTime?,
        presenceMer: Map<String, Long>,
        presenceQuai: Map<String, Long>,
        indisponibilite: Map<String, Long>,
        nbJoursMer: Int,
        dureeMission: Int,
        patrouilleEnv: Int,
        patrouilleMigrant: Int,
        distanceMilles: Float?,
        goMarine: Float?,
        essence: Float?,
        crew: List<MissionCrewEntity>

    )
}
