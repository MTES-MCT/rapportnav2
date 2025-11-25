package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.passenger

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.MissionCrewEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.MissionPassengerEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IMissionPassengerRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.crew.GetActiveCrewForService
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.generalInfo.MissionGeneralInfo2
import org.slf4j.LoggerFactory
import java.util.*

@UseCase
class GetMissionPassengers(
    private val repo: IMissionPassengerRepository
) {
    private val logger = LoggerFactory.getLogger(GetMissionPassengers::class.java)

    fun execute(
        missionId: Int? = null,
    ): List<MissionPassengerEntity> {
        return repo.findByMissionId(missionId = missionId!!).map { MissionPassengerEntity.fromMissionPassengerModel(it) }
    }

    fun execute(
        missionIdUUID: UUID? = null,
    ): List<MissionPassengerEntity> {
        return repo.findByMissionIdUUID(missionIdUUID = missionIdUUID!!).map { MissionPassengerEntity.fromMissionPassengerModel(it) }
    }
}
