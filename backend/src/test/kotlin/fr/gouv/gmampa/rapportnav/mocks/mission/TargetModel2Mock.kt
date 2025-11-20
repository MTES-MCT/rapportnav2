package fr.gouv.gmampa.rapportnav.mocks.mission

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.InfractionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselSizeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlResult
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.target2.v2.TargetStatusType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.target2.v2.TargetType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.ControlEntity2
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.InfractionEntity2
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.control.v2.ControlModel2
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.target2.v2.TargetModel2
import fr.gouv.gmampa.rapportnav.mocks.mission.action.ControlEntity2Mock
import java.time.Instant
import java.util.*

object TargetModel2Mock {
    fun create(
        id: UUID? = UUID.randomUUID(),
        agent: String? = null,
        actionId: String? = null,
        targetType: TargetType? = null,
        status: TargetStatusType? = TargetStatusType.IN_PROCESS,
        controls: List<ControlModel2>? = null,
        source: MissionSourceEnum? = MissionSourceEnum.MONITORENV,
    ): TargetModel2 {
        return TargetModel2(
            startDateTimeUtc = Instant.parse("2020-01-01T00:00:00Z"),
            endDateTimeUtc = Instant.parse("2020-02-01T00:00:00Z"),
            id = id ?: UUID.randomUUID(),
            status = status.toString(),
            actionId = actionId ?: UUID.randomUUID().toString(),
            source = source.toString(),
            vesselSize = VesselSizeEnum.FROM_12_TO_24m.toString(),
            vesselType = VesselTypeEnum.SAILING.toString(),
            vesselIdentifier = "My vesselIdentifier",
            agent = agent?: "My agent",
            vesselName = "My vesselName",
            identityControlledPerson = "My identityContolledPerson",
            targetType = targetType ?: TargetType.COMPANY,
            controls = ((controls?: listOf(
                ControlEntity2Mock.create(
                    id = UUID.randomUUID(),
                    controlType = ControlType.NAVIGATION,
                    amountOfControls = 0,
                    hasBeenDone = false,
                    observations = "My observations",
                    staffOutnumbered = ControlResult.NO,
                    upToDateMedicalCheck = ControlResult.NO,
                    compliantOperatingPermit = ControlResult.YES,
                    upToDateNavigationPermit = ControlResult.NO,
                    compliantSecurityDocuments = ControlResult.NOT_CONCERNED,
                    knowledgeOfFrenchLawAndLanguage = ControlResult.NOT_CONTROLLED,
                    infractions = listOf(
                        InfractionEntity2(
                            id = UUID.randomUUID(),
                            infractionType = InfractionTypeEnum.WITH_REPORT,
                            natinfs = listOf("natInf2", "natInf3"),
                            observations = "My observations"
                        )
                    )
                ).toControlModel(),
                ControlEntity2Mock.create(
                    id = UUID.randomUUID(),
                    controlType = ControlType.SECURITY,
                    amountOfControls = 1,
                    hasBeenDone = false,
                    observations = "My observations",
                    staffOutnumbered = ControlResult.NO,
                    upToDateMedicalCheck = ControlResult.NO,
                    compliantOperatingPermit = ControlResult.YES,
                    upToDateNavigationPermit = ControlResult.NO,
                    compliantSecurityDocuments = ControlResult.NOT_CONCERNED,
                    knowledgeOfFrenchLawAndLanguage = ControlResult.NOT_CONTROLLED,
                    infractions = listOf(
                        InfractionEntity2(
                            id = UUID.randomUUID(),
                            infractionType = InfractionTypeEnum.WITH_REPORT,
                            natinfs = listOf("natInf2", "natInf3"),
                            observations = "My observations"
                        )
                    )
                ).toControlModel(),
                ControlEntity2Mock.create(
                    id = UUID.randomUUID(),
                    controlType = ControlType.GENS_DE_MER,
                    amountOfControls = 0,
                    hasBeenDone = false,
                    observations = "My observations",
                    staffOutnumbered = ControlResult.NO,
                    upToDateMedicalCheck = ControlResult.NO,
                    compliantOperatingPermit = ControlResult.YES,
                    upToDateNavigationPermit = ControlResult.NO,
                    compliantSecurityDocuments = ControlResult.NOT_CONCERNED,
                    knowledgeOfFrenchLawAndLanguage = ControlResult.NOT_CONTROLLED,
                    infractions = listOf(
                        InfractionEntity2(
                            id = UUID.randomUUID(),
                            infractionType = InfractionTypeEnum.WITH_REPORT,
                            natinfs = listOf("natInf2", "natInf3"),
                            observations = "My observations"
                        )
                    )
                ).toControlModel(),
                ControlEntity2Mock.create(
                    id = UUID.randomUUID(),
                    controlType = ControlType.GENS_DE_MER,
                    amountOfControls = 1,
                    hasBeenDone = false,
                    observations = "My observations",
                    staffOutnumbered = ControlResult.NO,
                    upToDateMedicalCheck = ControlResult.NO,
                    compliantOperatingPermit = ControlResult.YES,
                    upToDateNavigationPermit = ControlResult.NO,
                    compliantSecurityDocuments = ControlResult.NOT_CONCERNED,
                    knowledgeOfFrenchLawAndLanguage = ControlResult.NOT_CONTROLLED,
                    infractions = listOf(
                        InfractionEntity2(
                            id = UUID.randomUUID(),
                            infractionType = InfractionTypeEnum.WITHOUT_REPORT,
                            natinfs = listOf("natInf2", "natInf3"),
                            observations = "My observations"
                        )
                    )
                ).toControlModel(),
            )))
        )
    }
}
