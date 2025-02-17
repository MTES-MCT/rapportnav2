package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionGeneralInfoEntity2
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.CreateOrUpdateGeneralInfo
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.generalInfo.MissionGeneralInfo2
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v2/missions/{missionId}/general_infos")
class MissionGeneralInfoRestController(
    private val createOrUpdateGeneralInfo: CreateOrUpdateGeneralInfo
)
{
    private val logger = LoggerFactory.getLogger(MissionGeneralInfoRestController::class.java)
    @PutMapping
    fun update(@RequestBody generalInfo: MissionGeneralInfo2, @PathVariable missionId: Int): MissionGeneralInfoEntity2? {
        try {
            return createOrUpdateGeneralInfo.execute(generalInfo, missionId)
        } catch (e: Exception) {
            logger.error("Error while updating general info mission id ${generalInfo.missionId} : ${e.message}")
            return null
        }
    }
}
