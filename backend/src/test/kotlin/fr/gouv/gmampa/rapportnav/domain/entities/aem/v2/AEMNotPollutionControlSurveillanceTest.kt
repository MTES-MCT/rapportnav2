package fr.gouv.gmampa.rapportnav.domain.entities.aem.v2

import fr.gouv.dgampa.rapportnav.domain.entities.aem.AEMNotPollutionControlSurveillance
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.*
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.themes.ThemeEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.target2.v2.TargetType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.ControlEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.InfractionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.TargetEntity
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import java.time.Instant
import java.util.*

@SpringBootTest(classes = [AEMNotPollutionControlSurveillance::class])
class AEMNotPollutionControlSurveillanceTest {

    @Test
    fun `Should init not pollution control surveillance with different values`() {
        val actions = extendedEnvActionEntities()
        val notPollution = AEMNotPollutionControlSurveillance(envActions = actions)

        assertThat(notPollution).isNotNull()
        assertThat(notPollution.nbrOfHourAtSea).isEqualTo(4.5)
        assertThat(notPollution.nbrOfAction).isEqualTo(2.0) // 2 targets in the non-pollution action
        assertThat(notPollution.nbrOfInfraction).isEqualTo(2.0)
        assertThat(notPollution.nbrOfInfractionWithNotice).isEqualTo(2.0)
    }

    @Test
    fun `getNbrOfTargets should count targets across all actions`() {
        val actionId = UUID.randomUUID().toString()
        val actions = listOf(
            null, // null action -> 0
            MissionEnvActionEntity( // null targets -> 0
                missionId = 761,
                id = UUID.randomUUID(),
                envActionType = ActionTypeEnum.CONTROL,
                targets = null
            ),
            MissionEnvActionEntity( // empty targets -> 0
                missionId = 761,
                id = UUID.randomUUID(),
                envActionType = ActionTypeEnum.CONTROL,
                targets = emptyList()
            ),
            MissionEnvActionEntity( // 3 targets -> 3
                missionId = 761,
                id = UUID.randomUUID(),
                envActionType = ActionTypeEnum.CONTROL,
                targets = listOf(
                    TargetEntity(id = UUID.randomUUID(), actionId = actionId, targetType = TargetType.VEHICLE),
                    TargetEntity(id = UUID.randomUUID(), actionId = actionId, targetType = TargetType.VEHICLE),
                    TargetEntity(id = UUID.randomUUID(), actionId = actionId, targetType = TargetType.VEHICLE)
                )
            ),
            MissionEnvActionEntity( // 1 target -> 1
                missionId = 761,
                id = UUID.randomUUID(),
                envActionType = ActionTypeEnum.CONTROL,
                targets = listOf(
                    TargetEntity(id = UUID.randomUUID(), actionId = actionId, targetType = TargetType.VEHICLE)
                )
            )
        )
        val result = AEMNotPollutionControlSurveillance.getNbrOfTargets(actions)
        assertThat(result).isEqualTo(4.0)
    }

    @Test
    fun `getNbrOfTargets should return zero for empty list`() {
        val result = AEMNotPollutionControlSurveillance.getNbrOfTargets(emptyList())
        assertThat(result).isEqualTo(0.0)
    }

    @Test
    fun `getNbrOfInfraction should handle all null and edge cases`() {
        val actionId = UUID.randomUUID().toString()
        val actions = listOf(
            null, // null action -> 0
            MissionEnvActionEntity( // null targets -> 0
                missionId = 761,
                id = UUID.randomUUID(),
                envActionType = ActionTypeEnum.CONTROL,
                targets = null
            ),
            MissionEnvActionEntity( // null controls -> 0
                missionId = 761,
                id = UUID.randomUUID(),
                envActionType = ActionTypeEnum.CONTROL,
                targets = listOf(
                    TargetEntity(
                        id = UUID.randomUUID(),
                        actionId = actionId,
                        targetType = TargetType.VEHICLE,
                        controls = null
                    )
                )
            ),
            MissionEnvActionEntity(
                missionId = 761,
                id = UUID.randomUUID(),
                envActionType = ActionTypeEnum.CONTROL,
                targets = listOf(
                    // Target with natinfs -> counts as 1
                    TargetEntity(
                        id = UUID.randomUUID(),
                        actionId = actionId,
                        targetType = TargetType.VEHICLE,
                        controls = listOf(
                            ControlEntity(
                                id = UUID.randomUUID(),
                                controlType = ControlType.ADMINISTRATIVE,
                                amountOfControls = 1,
                                infractions = listOf(
                                    InfractionEntity(id = UUID.randomUUID(), natinfs = listOf("natinf-1"))
                                )
                            )
                        )
                    ),
                    // Target with empty natinfs -> 0
                    TargetEntity(
                        id = UUID.randomUUID(),
                        actionId = actionId,
                        targetType = TargetType.VEHICLE,
                        controls = listOf(
                            ControlEntity(
                                id = UUID.randomUUID(),
                                controlType = ControlType.SECURITY,
                                amountOfControls = 1,
                                infractions = listOf(
                                    InfractionEntity(id = UUID.randomUUID(), natinfs = emptyList())
                                )
                            )
                        )
                    ),
                    // Target with null infractions -> 0
                    TargetEntity(
                        id = UUID.randomUUID(),
                        actionId = actionId,
                        targetType = TargetType.VEHICLE,
                        controls = listOf(
                            ControlEntity(
                                id = UUID.randomUUID(),
                                controlType = ControlType.NAVIGATION,
                                amountOfControls = 1,
                                infractions = null
                            )
                        )
                    ),
                    // Another target with natinfs -> counts as 1
                    TargetEntity(
                        id = UUID.randomUUID(),
                        actionId = actionId,
                        targetType = TargetType.VEHICLE,
                        controls = listOf(
                            ControlEntity(
                                id = UUID.randomUUID(),
                                controlType = ControlType.GENS_DE_MER,
                                amountOfControls = 1,
                                infractions = listOf(
                                    InfractionEntity(id = UUID.randomUUID(), natinfs = listOf("natinf-2"))
                                )
                            )
                        )
                    )
                )
            )
        )
        val result = AEMNotPollutionControlSurveillance.getNbrOfInfraction(actions)
        assertThat(result).isEqualTo(2.0)
    }

    @Test
    fun `getNbrOfInfractionWithNotice should handle all null cases and filter by WITH_REPORT`() {
        val actions = listOf(
            null, // null action -> 0
            MissionEnvActionEntity( // null envInfractions -> 0
                missionId = 761,
                id = UUID.randomUUID(),
                envActionType = ActionTypeEnum.CONTROL,
                envInfractions = null
            ),
            MissionEnvActionEntity( // empty envInfractions -> 0
                missionId = 761,
                id = UUID.randomUUID(),
                envActionType = ActionTypeEnum.CONTROL,
                envInfractions = emptyList()
            ),
            MissionEnvActionEntity(
                missionId = 761,
                id = UUID.randomUUID(),
                envActionType = ActionTypeEnum.CONTROL,
                envInfractions = listOf(
                    InfractionEnvEntity(id = "1", formalNotice = FormalNoticeEnum.NO, infractionType = InfractionTypeEnum.WITH_REPORT), // counts
                    InfractionEnvEntity(id = "2", formalNotice = FormalNoticeEnum.NO, infractionType = InfractionTypeEnum.WITHOUT_REPORT), // filtered
                    InfractionEnvEntity(id = "3", formalNotice = FormalNoticeEnum.NO, infractionType = InfractionTypeEnum.WITH_REPORT), // counts
                    InfractionEnvEntity(id = "4", formalNotice = FormalNoticeEnum.NO, infractionType = InfractionTypeEnum.WAITING) // filtered
                )
            )
        )
        val result = AEMNotPollutionControlSurveillance.getNbrOfInfractionWithNotice(actions)
        assertThat(result).isEqualTo(2.0)
    }

    @Test
    fun `getNotPollutionActions should filter pollution themes and handle null cases`() {
        val actions = listOf(
            null, // filtered out
            MissionEnvActionEntity( // filtered out - null themes
                missionId = 761,
                id = UUID.randomUUID(),
                envActionType = ActionTypeEnum.CONTROL,
                themes = null
            ),
            MissionEnvActionEntity( // filtered out - pollution theme 19
                missionId = 761,
                id = UUID.randomUUID(),
                envActionType = ActionTypeEnum.CONTROL,
                themes = listOf(ThemeEntity(id = 19, name = "Illicit Rejects"))
            ),
            MissionEnvActionEntity( // filtered out - pollution theme 102
                missionId = 761,
                id = UUID.randomUUID(),
                envActionType = ActionTypeEnum.CONTROL,
                themes = listOf(ThemeEntity(id = 102, name = "Pollution"))
            ),
            MissionEnvActionEntity( // filtered out - mixed themes containing pollution
                missionId = 761,
                id = UUID.randomUUID(),
                envActionType = ActionTypeEnum.CONTROL,
                themes = listOf(ThemeEntity(id = 101, name = "Other"), ThemeEntity(id = 19, name = "Pollution"))
            ),
            MissionEnvActionEntity( // included - empty themes (intersect is empty)
                missionId = 761,
                id = UUID.randomUUID(),
                envActionType = ActionTypeEnum.CONTROL,
                themes = emptyList()
            ),
            MissionEnvActionEntity( // included - non-pollution theme
                missionId = 761,
                id = UUID.randomUUID(),
                envActionType = ActionTypeEnum.CONTROL,
                themes = listOf(ThemeEntity(id = 101, name = "Other Theme"))
            )
        )
        val notPollution = AEMNotPollutionControlSurveillance(envActions = actions)
        assertThat(notPollution.nbrOfAction).isEqualTo(0.0) // 2 actions pass the filter but neither has targets
    }

    private fun extendedEnvActionEntities(): List<MissionEnvActionEntity> {
        val actionId = UUID.randomUUID().toString()
        return listOf(
            MissionEnvActionEntity(
                missionId = 761,
                id = UUID.randomUUID(),
                envActionType = ActionTypeEnum.CONTROL,
                themes = listOf(ThemeEntity(id = 101, name = "Theme 101")),
                startDateTimeUtc = Instant.parse("2019-09-09T00:00:00.000+01:00"),
                endDateTimeUtc = Instant.parse("2019-09-09T04:30:00.000+01:00"),
                envInfractions = listOf(
                    InfractionEnvEntity(
                        id = "",
                        formalNotice = FormalNoticeEnum.YES,
                        infractionType = InfractionTypeEnum.WITH_REPORT,
                        natinf = listOf("natinf-1", "natinf-2", "natinf-3"),
                    ),
                    InfractionEnvEntity(
                        id = "",
                        formalNotice = FormalNoticeEnum.NO,
                        infractionType = InfractionTypeEnum.WITHOUT_REPORT,
                        natinf = listOf("natinf-1")
                    ),
                    InfractionEnvEntity(
                        id = "",
                        formalNotice = FormalNoticeEnum.YES,
                        infractionType = InfractionTypeEnum.WITH_REPORT,
                        natinf = listOf("natinf-1", "natinf-2")
                    )
                ),
                targets = listOf(
                    TargetEntity(
                        id = UUID.randomUUID(),
                        actionId = actionId,
                        targetType = TargetType.VEHICLE,
                        controls = listOf(
                            ControlEntity(
                                id = UUID.randomUUID(),
                                controlType = ControlType.ADMINISTRATIVE,
                                amountOfControls = 1,
                                infractions = listOf(
                                    InfractionEntity(id = UUID.randomUUID(), natinfs = listOf("natinf-1", "natinf-2"))
                                )
                            )
                        )
                    ),
                    TargetEntity(
                        id = UUID.randomUUID(),
                        actionId = actionId,
                        targetType = TargetType.VEHICLE,
                        controls = listOf(
                            ControlEntity(
                                id = UUID.randomUUID(),
                                controlType = ControlType.SECURITY,
                                amountOfControls = 1,
                                infractions = listOf(
                                    InfractionEntity(id = UUID.randomUUID(), natinfs = listOf("natinf-3"))
                                )
                            )
                        )
                    )
                )
            ),
            MissionEnvActionEntity(
                missionId = 761,
                id = UUID.randomUUID(),
                envActionType = ActionTypeEnum.SURVEILLANCE,
                themes = listOf(ThemeEntity(id = 19, name = "Theme 19")),
                startDateTimeUtc = Instant.parse("2019-09-09T02:00:00.000+01:00"),
                endDateTimeUtc = Instant.parse("2019-09-09T04:00:00.000+01:00"),
            ),
            MissionEnvActionEntity(
                missionId = 761,
                id = UUID.randomUUID(),
                envActionType = ActionTypeEnum.SURVEILLANCE,
                themes = listOf(ThemeEntity(id = 102, name = "Pollution")),
                startDateTimeUtc = Instant.parse("2019-09-09T12:00:00.000+01:00"),
                endDateTimeUtc = Instant.parse("2019-09-09T16:00:00.000+01:00"),
            )
        )
    }
}