package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.export.MissionAEMExportEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export.v2.ExportMissionsAEM
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.export.MissionAEMExportInput
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

@Controller
class MissionExportController(
    private val exportMissionsAEM: ExportMissionsAEM
)
{
    @QueryMapping
    fun missionAEMExportV2(@Argument missionExportAEMExportInput: MissionAEMExportInput): MissionAEMExportEntity? {
        return exportMissionsAEM.execute(missionExportAEMExportInput.ids)
    }
}
