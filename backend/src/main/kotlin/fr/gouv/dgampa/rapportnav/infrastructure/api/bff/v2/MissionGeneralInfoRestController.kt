package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionGeneralInfoEntity2
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.UpdateGeneralInfo
import fr.gouv.dgampa.rapportnav.domain.utils.isValidUUID
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.generalInfo.MissionGeneralInfo2
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/v2/missions/{missionId}/general_infos")
class MissionGeneralInfoRestController(
    private val updateGeneralInfo: UpdateGeneralInfo
)
{
    private val logger = LoggerFactory.getLogger(MissionGeneralInfoRestController::class.java)
    @PutMapping
    fun update(
        @PathVariable missionId: String,
        @RequestBody generalInfo: MissionGeneralInfo2
    ): MissionGeneralInfoEntity2? {
        return if (isValidUUID(missionId))
            updateGeneralInfo.execute(missionIdUUID = UUID.fromString(missionId), generalInfo = generalInfo)
        else updateGeneralInfo.execute(
            missionId = Integer.valueOf(missionId), generalInfo = generalInfo
        )
    }
}
