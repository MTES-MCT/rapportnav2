package fr.gouv.gmampa.rapportnav.mocks.mission

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.InfractionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselSizeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlResult
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.target2.v2.TargetStatusType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.target2.v2.TargetType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.ControlEntity2
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.InfractionEntity2
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.TargetEntity2
import java.time.Instant
import java.util.*

object TargetMissionMock {
    fun create(): TargetEntity2 {
        return TargetEntity2(
            startDateTimeUtc = Instant.parse("2020-01-01T00:00:00Z"),
            endDateTimeUtc = Instant.parse("2020-02-01T00:00:00Z"),
            id = UUID.randomUUID(),
            status = TargetStatusType.IN_PROCESS,
            actionId = UUID.randomUUID(),
            vesselSize = VesselSizeEnum.FROM_12_TO_24m,
            vesselType = VesselTypeEnum.SAILING,
            vesselIdentifier = "My vesselIdentifier",
            agent = "My agent",
            vesselName = "My vesselName",
            identityContolledPerson = "My identityContolledPerson",
            targetType = TargetType.COMPANY,
            controls = listOf(
                ControlEntity2(
                    id = UUID.randomUUID(),
                    nbrOfHours = 4,
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
                ),
                ControlEntity2(
                    id = UUID.randomUUID(),
                    nbrOfHours = 4,
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
                ),
                ControlEntity2(
                    id = UUID.randomUUID(),
                    nbrOfHours = 4,
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
                ),
                ControlEntity2(
                    id = UUID.randomUUID(),
                    nbrOfHours = 4,
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
                )
            )
        )
    }
}
