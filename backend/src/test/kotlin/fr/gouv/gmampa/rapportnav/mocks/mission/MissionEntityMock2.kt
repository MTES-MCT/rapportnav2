package fr.gouv.gmampa.rapportnav.mocks.mission

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.LegacyControlUnitEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.MissionGeneralInfoEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEntity2
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionGeneralInfoEntity2
import org.locationtech.jts.geom.MultiPolygon
import java.time.Instant

object MissionEntityMock2 {
    fun create(
        id: Int? = 1,
        missionTypes: List<MissionTypeEnum> = listOf(),
        controlUnits: List<LegacyControlUnitEntity> = listOf(),
        openBy: String? = null,
        completedBy: String? = null,
        observationsCacem: String? = null,
        observationsCnsp: String? = null,
        facade: String? = null,
        geom: MultiPolygon? = null,
        startDateTimeUtc: Instant = Instant.parse("2022-01-02T12:00:00Z"),
        endDateTimeUtc: Instant? = null,
        isDeleted: Boolean = false,
        isGeometryComputedFromControls: Boolean = false,
        missionSource: MissionSourceEnum = MissionSourceEnum.MONITORENV,
        hasMissionOrder: Boolean = false,
        isUnderJdp: Boolean = false,
        actions: List<MissionActionEntity> = listOf(),
        generalInfos: MissionGeneralInfoEntity2? = MissionGeneralInfoEntity2(data = MissionGeneralInfoEntity(missionId = 1, id = 1)),
    ): MissionEntity2 {
        return MissionEntity2(
            id = id,
            data = MissionEntity(
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
                isDeleted = isDeleted,
                isGeometryComputedFromControls = isGeometryComputedFromControls,
                missionSource = missionSource,
                hasMissionOrder = hasMissionOrder,
                isUnderJdp = isUnderJdp
            ),
            actions = actions,
            generalInfos = generalInfos
        )
    }

}
