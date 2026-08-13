package fr.gouv.gmampa.rapportnav.infrastructure.bff.adapters.mission

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionEnvEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.LegacyControlUnitEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.LegacyControlUnitResourceEntity
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.MissionEnvInput
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.Instant

@ExtendWith(SpringExtension::class)
class MissionEnvInputTest {
    @Test
    fun `execute should return patchInput with the right amount of resources`() {
        val startDateTimeUtc = Instant.parse("2019-09-08T22:00:00.000+01:00")
        val endDateTimeUtc = Instant.parse("2019-09-09T01:00:00.000+01:00")
        val input = MissionEnvInput(
            missionId = 761,
            isUnderJdp = false,
            controlUnitId = 741,
            endDateTimeUtc = endDateTimeUtc,
            startDateTimeUtc = startDateTimeUtc,
            missionTypes = listOf(MissionTypeEnum.AIR),
            observationsByUnit = "My beautiful observation",
            resources = listOf(
                LegacyControlUnitResourceEntity(id = 12, controlUnitId = 741),
                LegacyControlUnitResourceEntity(id = 13, controlUnitId = 741)
            )
        )


        val patchInput = input.toPatchMissionInput(
            controlUnits = listOf(
                LegacyControlUnitEntity(
                    id = 741,
                    resources = mutableListOf(LegacyControlUnitResourceEntity(id = 13, controlUnitId = 741))
                )
            )
        )

        assertThat(patchInput).isNotNull()
        assertThat(patchInput.isUnderJdp).isEqualTo(input.isUnderJdp)
        assertThat(patchInput.missionTypes).isEqualTo(input.missionTypes)
        assertThat(patchInput.endDateTimeUtc).isEqualTo(input.endDateTimeUtc)
        assertThat(patchInput.startDateTimeUtc).isEqualTo(input.startDateTimeUtc)
        assertThat(patchInput.observationsByUnit).isEqualTo(input.observationsByUnit)

        assertThat(patchInput.controlUnits?.size).isEqualTo(1)
        assertThat(patchInput.controlUnits?.get(0)?.resources?.size).isEqualTo(2)
        assertThat(patchInput.controlUnits?.get(0)?.resources?.map { it.id }
            ?.toSet()).isEqualTo(input.resources?.map { it.id }?.toSet())
    }


    @Test
    fun `toPatchMissionInput should strip per-resource usage values before sending to MonitorEnv`() {
        val input = MissionEnvInput(
            missionId = 761,
            controlUnitId = 741,
            resources = listOf(
                LegacyControlUnitResourceEntity(id = 12, controlUnitId = 741, nbKms = 42.0),
                LegacyControlUnitResourceEntity(id = 13, controlUnitId = 741, nbEngineHours = 3.5)
            )
        )

        val patchInput = input.toPatchMissionInput(
            controlUnits = listOf(LegacyControlUnitEntity(id = 741, resources = mutableListOf()))
        )

        val resources = patchInput.controlUnits?.get(0)?.resources
        assertThat(resources?.map { it.id }).containsExactly(12, 13)
        assertThat(resources?.map { it.nbKms }).containsOnlyNulls()
        assertThat(resources?.map { it.nbEngineHours }).containsOnlyNulls()
    }

    @Test
    fun `execute should return a mission env input from a mission env`() {
        val startDateTimeUtc = Instant.parse("2019-09-08T22:00:00.000+01:00")
        val mission = MissionEnvEntity(
            id = 761,
            isDeleted = false,
            isUnderJdp = false,
            hasMissionOrder = false,
            missionTypes = mutableListOf(),
            startDateTimeUtc = startDateTimeUtc,
            isGeometryComputedFromControls = false,
            missionSource = MissionSourceEnum.RAPPORT_NAV,
            controlUnits = listOf(
                LegacyControlUnitEntity(
                    id = 741,
                    resources = mutableListOf(LegacyControlUnitResourceEntity(id = 13, controlUnitId = 741))
                )
            )
        )
        val response = MissionEnvInput.fromMissionEntity(entity = mission, controlUnitId = 741)

        assertThat(response).isNotNull()
        assertThat(response.isUnderJdp).isEqualTo(mission.isUnderJdp)
        assertThat(response.missionTypes).isEqualTo(mission.missionTypes)
        assertThat(response.endDateTimeUtc).isEqualTo(mission.endDateTimeUtc)
        assertThat(response.startDateTimeUtc).isEqualTo(mission.startDateTimeUtc)
        assertThat(response.observationsByUnit).isEqualTo(mission.observationsByUnit)
        assertThat(response.resources?.toSet()).isEqualTo(mission.controlUnits.get(0).resources?.toSet())
    }


}
