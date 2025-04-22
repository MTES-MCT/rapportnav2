package fr.gouv.gmampa.rapportnav.mocks.mission

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.LegacyControlUnitEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.EnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.env.MissionEnvEntity
import org.locationtech.jts.geom.MultiPolygon
import java.time.Instant

object EnvMissionMockv2 {
    fun create(
        id: String? = "1",
        missionTypes: List<MissionTypeEnum> = listOf(),
        controlUnits: List<LegacyControlUnitEntity> = listOf(),
        openBy: String? = null,
        completedBy: String? = null,
        observationsCacem: String? = null,
        observationsCnsp: String? = null,
        facade: String? = null,
        geom: MultiPolygon? = null,
        startDateTimeUtc: Instant = Instant.parse("2022-01-02T12:00:01Z"),
        endDateTimeUtc: Instant? = null,
        isGeometryComputedFromControls: Boolean = false,
        missionSource: MissionSourceEnum = MissionSourceEnum.MONITORENV,
        hasMissionOrder: Boolean = false,
        isUnderJdp: Boolean = false,
        envActions: List<EnvActionEntity>? = null,
        observationsByUnit: String? = null
    ): MissionEnvEntity {
        return MissionEnvEntity(
            id = id,
            missionTypes = missionTypes,
            controlUnits = controlUnits,
            openBy = openBy,
            completedBy = completedBy,
            observationsCacem = observationsCacem,
            observationsCnsp = observationsCnsp,
            facade = facade,
            geom = geom,
            startDateTimeUtc = startDateTimeUtc,
            endDateTimeUtc = endDateTimeUtc,
            isGeometryComputedFromControls = isGeometryComputedFromControls,
            missionSource = missionSource,
            hasMissionOrder = hasMissionOrder,
            isUnderJdp = isUnderJdp,
            envActions = envActions,
            observationsByUnit = observationsByUnit
        )
    }

}
