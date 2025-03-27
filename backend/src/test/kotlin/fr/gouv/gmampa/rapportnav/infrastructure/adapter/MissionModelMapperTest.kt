package fr.gouv.gmampa.rapportnav.infrastructure.adapter

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavEntity
import fr.gouv.dgampa.rapportnav.infrastructure.database.mapper.mission.toMissionModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.mapper.mission.toMissionNavEntity
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.MissionModel
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.Instant

@ExtendWith(SpringExtension::class)
class MissionModelMapperTest {


    @Test
    fun `should map MissionModel to MissionNavEntity`() {
        val model = MissionModel(
            id = 1,
            startDateTimeUtc = Instant.now(),
            endDateTimeUtc = Instant.parse("2024-10-01T04:50:09Z"),
            missionSource = MissionSourceEnum.RAPPORT_NAV,
            missionTypes = listOf(MissionTypeEnum.AIR),
            isDeleted = false,
            controlUnits = listOf(1),
            controlUnitIdOwner = 1,
            observationsByUnit = "observations by unit",
            completedBy = "test",
            openBy = "test-team"
        )

        val missionNavEntity = model.toMissionNavEntity()

        assertEquals(missionNavEntity.id, model.id)
        assertEquals(missionNavEntity.missionTypes, model.missionTypes)
        assertEquals(missionNavEntity.controlUnits, model.controlUnits)
        assertEquals(missionNavEntity.openBy, model.openBy)
        assertEquals(missionNavEntity.completedBy, model.completedBy)
        assertEquals(missionNavEntity.startDateTimeUtc, model.startDateTimeUtc)
        assertEquals(missionNavEntity.endDateTimeUtc, model.endDateTimeUtc)
        assertEquals(missionNavEntity.isDeleted, model.isDeleted)
        assertEquals(missionNavEntity.missionSource, model.missionSource)
        assertEquals(missionNavEntity.observationsByUnit, model.observationsByUnit)
        assertEquals(missionNavEntity.controlUnitIdOwner, model.controlUnitIdOwner)
    }

    @Test
    fun `should map MissionModel from MissionNavEntity`() {
        val missionNavEntity = MissionNavEntity(
            id = 1,
            startDateTimeUtc = Instant.now(),
            endDateTimeUtc = Instant.parse("2024-10-01T04:50:09Z"),
            missionSource = MissionSourceEnum.RAPPORT_NAV,
            missionTypes = listOf(MissionTypeEnum.AIR),
            isDeleted = false,
            controlUnits = listOf(1),
            controlUnitIdOwner = 1,
            observationsByUnit = "observations by unit",
            completedBy = "test",
            openBy = "test-team"
        )

        val model = missionNavEntity.toMissionModel()

        assertEquals(model.id, missionNavEntity.id)
        assertEquals(model.missionTypes, missionNavEntity.missionTypes)
        assertEquals(model.controlUnits, missionNavEntity.controlUnits)
        assertEquals(model.openBy, missionNavEntity.openBy)
        assertEquals(model.completedBy, missionNavEntity.completedBy)
        assertEquals(model.startDateTimeUtc, missionNavEntity.startDateTimeUtc)
        assertEquals(model.endDateTimeUtc, missionNavEntity.endDateTimeUtc)
        assertEquals(model.isDeleted, missionNavEntity.isDeleted)
        assertEquals(model.missionSource, missionNavEntity.missionSource)
        assertEquals(model.observationsByUnit, missionNavEntity.observationsByUnit)
        assertEquals(model.controlUnitIdOwner, missionNavEntity.controlUnitIdOwner)
    }
}
