package fr.gouv.dgampa.rapportnav.domain.repositories.mission

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.MissionCrewEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.export.MissionExportEntity
import java.time.LocalDate
import java.time.ZonedDateTime

interface IRpnExportRepository {

    fun exportOdt(
        service: String?,
        id: String,
        startDateTime: ZonedDateTime?,
        endDateTime: ZonedDateTime?,
        presenceMer: Map<String, Int>,
        presenceQuai: Map<String, Int>,
        indisponibilite: Map<String, Int>,
        nbJoursMer: Int,
        dureeMission: Int,
        patrouilleEnv: Int,
        patrouilleMigrant: Int,
        distanceMilles: Float?,
        goMarine: Float?,
        essence: Float?,
        crew: List<MissionCrewEntity>,
        timeline: Map<LocalDate, List<String>>?
    ) : MissionExportEntity?
}
