package fr.gouv.dgampa.rapportnav.domain.use_cases.missions

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionEntity
import org.slf4j.LoggerFactory

@UseCase
class UpdateEnvMission {
    private val logger = LoggerFactory.getLogger(UpdateEnvMission::class.java)

    fun execute(
        mission: MissionEntity,
    ): Any {
        // TODO do API call to Monitor
        return {} as Any
    }
}
