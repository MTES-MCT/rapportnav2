package fr.gouv.gmampa.rapportnav.mocks.mission

import fr.gouv.dgampa.rapportnav.domain.entities.mission.MissionActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.LegacyControlUnitEntity
import org.locationtech.jts.geom.MultiPolygon
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.ZonedDateTime

object NavMissionMock {
    fun create(
        id: Int = 1,
        missionTypes: List<MissionTypeEnum> = listOf(),
        controlUnits: List<LegacyControlUnitEntity> = listOf(),
        openBy: String? = null,
        closedBy: String? = null,
        observationsCacem: String? = null,
        observationsCnsp: String? = null,
        facade: String? = null,
        geom: MultiPolygon? = null,
        startDateTimeUtc: ZonedDateTime = ZonedDateTime.of(LocalDateTime.of(2022, 1, 2, 12, 0), ZoneOffset.UTC),
        endDateTimeUtc: ZonedDateTime? = null,
        isClosed: Boolean = false,
        isDeleted: Boolean = false,
        isGeometryComputedFromControls: Boolean = false,
        missionSource: MissionSourceEnum = MissionSourceEnum.MONITORENV,
        hasMissionOrder: Boolean = false,
        isUnderJdp: Boolean = false,
        actions: List<MissionActionEntity>? = null,
    ): MissionEntity {
        return MissionEntity(
            id = id,
            missionTypes = missionTypes,
            controlUnits = controlUnits,
            openBy = openBy,
            closedBy = closedBy,
            observationsCacem = observationsCacem,
            observationsCnsp = observationsCnsp,
            facade = facade,
            geom = geom,
            startDateTimeUtc = startDateTimeUtc,
            endDateTimeUtc = endDateTimeUtc,
            isClosed = isClosed,
            isDeleted = isDeleted,
            isGeometryComputedFromControls = isGeometryComputedFromControls,
            missionSource = missionSource,
            hasMissionOrder = hasMissionOrder,
            isUnderJdp = isUnderJdp,
            actions = actions
        )
    }

}
