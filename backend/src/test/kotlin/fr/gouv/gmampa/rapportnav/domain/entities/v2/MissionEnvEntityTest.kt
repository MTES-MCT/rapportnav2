package fr.gouv.gmampa.rapportnav.domain.entities.v2


import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionEnvEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.LegacyControlUnitEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.LegacyControlUnitResourceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.service.ServiceTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavEntity
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.Instant
import java.util.*

@ExtendWith(SpringExtension::class)
class MissionEnvEntityTest {

    @Test
    fun `execute should get env entity from nav entity`() {
        val missionNavEntity = MissionNavEntity(
            id = UUID.randomUUID(),
            serviceId = 2,
            endDateTimeUtc = Instant.now(),
            startDateTimeUtc = Instant.now(),
            isDeleted = false,
            observationsByUnit = "myObservation",
        )
        val response = MissionEnvEntity.fromMissionNavEntity(missionNavEntity)

        assertThat(response).isNotNull()
        assertThat(response.idUUID).isEqualTo(missionNavEntity.id)
        assertThat(response.isDeleted).isEqualTo(missionNavEntity.isDeleted)
        assertThat(response.endDateTimeUtc).isEqualTo(missionNavEntity.endDateTimeUtc)
        assertThat(response.startDateTimeUtc).isEqualTo(missionNavEntity.startDateTimeUtc)
        assertThat(response.observationsByUnit).isEqualTo(missionNavEntity.observationsByUnit)
        assertThat(response.isUnderJdp).isEqualTo(false)
        assertThat(response.missionSource).isEqualTo(MissionSourceEnum.RAPPORT_NAV)
        assertThat(response.missionTypes?.size).isEqualTo(0)
        assertThat(response.geom).isEqualTo(null)
        assertThat(response.facade).isEqualTo(null)
    }

    @Test
    fun `execute return true to completeness when serviceType is not ULAM`() {
        val missionNavEntity = MissionNavEntity(
            id = UUID.randomUUID(),
            serviceId = 2,
            endDateTimeUtc = Instant.now(),
            startDateTimeUtc = Instant.now(),
            isDeleted = false,
            observationsByUnit = "myObservation",
        )
        val entity = MissionEnvEntity.fromMissionNavEntity(missionNavEntity)
        assertThat(entity.isCompleteForStats()).isEqualTo(true)
        assertThat(entity.isCompleteForStats(isResourcesNotUsed = true)).isEqualTo(true)
        assertThat(
            entity.isCompleteForStats(
                serviceTypeEnum = ServiceTypeEnum.PAM,
                isResourcesNotUsed = true
            )
        ).isEqualTo(true)
        assertThat(
            entity.isCompleteForStats(
                serviceTypeEnum = ServiceTypeEnum.PAM,
                isResourcesNotUsed = false
            )
        ).isEqualTo(true)
    }

    @Test
    fun `execute return true to completeness when serviceType is ULAM and no resources and not is used`() {
        val missionNavEntity = MissionNavEntity(
            id = UUID.randomUUID(),
            serviceId = 2,
            endDateTimeUtc = Instant.now(),
            startDateTimeUtc = Instant.now(),
            isDeleted = false,
            observationsByUnit = "myObservation",
        )
        val entity = MissionEnvEntity.fromMissionNavEntity(missionNavEntity)
        assertThat(
            entity.isCompleteForStats(
                serviceTypeEnum = ServiceTypeEnum.ULAM,
                isResourcesNotUsed = true
            )
        ).isEqualTo(true)
    }

    @Test
    fun `execute return false to completeness when serviceType is ULAM and no resources and is used`() {
        val missionNavEntity = MissionNavEntity(
            id = UUID.randomUUID(),
            serviceId = 2,
            endDateTimeUtc = Instant.now(),
            startDateTimeUtc = Instant.now(),
            isDeleted = false,
            observationsByUnit = "myObservation",
        )
        val entity = MissionEnvEntity.fromMissionNavEntity(missionNavEntity)
        entity.controlUnits =
            listOf(LegacyControlUnitEntity(id = 1, isArchived = false, name = "Unit 1", resources = mutableListOf()))
        assertThat(
            entity.isCompleteForStats(
                serviceTypeEnum = ServiceTypeEnum.ULAM,
                isResourcesNotUsed = false
            )
        ).isEqualTo(false)
    }

    @Test
    fun `execute return true to completeness when serviceType is ULAM and resources is filled`() {
        val missionNavEntity = MissionNavEntity(
            id = UUID.randomUUID(),
            serviceId = 2,
            endDateTimeUtc = Instant.now(),
            startDateTimeUtc = Instant.now(),
            isDeleted = false,
            observationsByUnit = "myObservation",
        )
        val entity = MissionEnvEntity.fromMissionNavEntity(missionNavEntity)
        entity.controlUnits =
            listOf(
                LegacyControlUnitEntity(
                    id = 1, isArchived = false, name = "Unit 1", resources = mutableListOf(
                        LegacyControlUnitResourceEntity(id = 13, controlUnitId = 741)
                    )
                )
            )
        assertThat(
            entity.isCompleteForStats(
                serviceTypeEnum = ServiceTypeEnum.ULAM,
                isResourcesNotUsed = false
            )
        ).isEqualTo(true)
    }

    @Test
    fun `execute not return exception but false when control unit list is empty`() {
        val missionNavEntity = MissionNavEntity(
            id = UUID.randomUUID(),
            serviceId = 2,
            endDateTimeUtc = Instant.now(),
            startDateTimeUtc = Instant.now(),
            isDeleted = false,
            observationsByUnit = "myObservation",
        )
        val entity = MissionEnvEntity.fromMissionNavEntity(missionNavEntity)
        entity.controlUnits = listOf()
        assertThat(
            entity.isCompleteForStats(
                serviceTypeEnum = ServiceTypeEnum.ULAM,
                isResourcesNotUsed = false
            )
        ).isEqualTo(false)
    }
}
