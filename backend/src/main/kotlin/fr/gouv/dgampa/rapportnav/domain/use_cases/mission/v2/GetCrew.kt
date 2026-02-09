package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.CrewEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.service.GetCrewByServiceId
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.generalInfo.MissionGeneralInfo
import org.slf4j.LoggerFactory
import java.util.*

@UseCase
class GetCrew(
    private val getActiveCrewForService: GetCrewByServiceId,
) {
    private val logger = LoggerFactory.getLogger(GetCrew::class.java)
    fun execute(
        newServiceId: Int? = null,
        oldServiceId: Int? = null,
        missionId: Int? = null,
        missionIdUUID: UUID? = null,
        generalInfo: MissionGeneralInfo? = null
    ): List<CrewEntity> {

        if ((newServiceId == oldServiceId) && generalInfo?.crew != null) {
            return generalInfo.crew.map {
                it.toMissionCrewEntity(missionIdUUID = missionIdUUID, missionId = missionId)
            }
        }
        if (newServiceId == null) return emptyList()
        return getActiveCrewForService.execute(serviceId = newServiceId).map {
            CrewEntity(
                role = it.role,
                agent = it,
                missionId = missionId,
                missionIdUUID = missionIdUUID
            )
        }
    }
}
