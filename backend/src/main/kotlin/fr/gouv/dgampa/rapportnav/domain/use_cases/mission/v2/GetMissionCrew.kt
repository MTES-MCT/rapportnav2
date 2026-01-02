package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.MissionCrewEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.service.GetCrewByServiceId2
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.generalInfo.MissionGeneralInfo2
import org.slf4j.LoggerFactory
import java.util.*

@UseCase
class GetMissionCrew(
    private val getActiveCrewForService: GetCrewByServiceId2,
) {
    private val logger = LoggerFactory.getLogger(GetMissionCrew::class.java)
    fun execute(
        newServiceId: Int? = null,
        oldServiceId: Int? = null,
        missionId: Int? = null,
        missionIdUUID: UUID? = null,
        generalInfo: MissionGeneralInfo2? = null
    ): List<MissionCrewEntity> {

        if ((newServiceId == oldServiceId) && generalInfo?.crew != null) {
            return generalInfo.crew.map {
                it.toMissionCrewEntity(missionIdUUID = missionIdUUID, missionId = missionId)
            }
        }
        if (newServiceId == null) return emptyList()
        return getActiveCrewForService.execute(serviceId = newServiceId).map {
            MissionCrewEntity(
                role = it.role,
                agent = it,
                missionId = missionId,
                missionIdUUID = missionIdUUID
            )
        }
    }
}
