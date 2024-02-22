package fr.gouv.dgampa.rapportnav.domain.repositories.mission

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.MissionCrewEntity

interface IRpnExportRepository {

    fun exportOdt(
        service: String?,
        id: String,
        startDateTime: String?,
        endDateTime: String?,
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
        crew: List<MissionCrewEntity>

    )
}
