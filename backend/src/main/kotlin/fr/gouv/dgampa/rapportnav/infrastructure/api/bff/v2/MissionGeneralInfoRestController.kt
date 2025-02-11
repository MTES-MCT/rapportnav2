package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionGeneralInfoEntity2
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.CreateOrUpdateGeneralInfo
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.generalInfo.MissionGeneralInfo2
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v2/missions/{missionId}/mission_general_infos")
class MissionGeneralInfoRestController(
    private val createOrUpdateGeneralInfo: CreateOrUpdateGeneralInfo
)
{
    @PutMapping
    fun update(@RequestBody generalInfo: MissionGeneralInfo2): MissionGeneralInfoEntity2 {
        return createOrUpdateGeneralInfo.execute(generalInfo)
    }
}
