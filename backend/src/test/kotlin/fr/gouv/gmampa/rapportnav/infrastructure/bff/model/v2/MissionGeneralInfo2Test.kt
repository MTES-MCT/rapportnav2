package fr.gouv.gmampa.rapportnav.infrastructure.bff.model.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.LegacyControlUnitResourceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.service.ServiceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.MissionGeneralInfoEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionGeneralInfoEntity2
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionReportTypeEnum
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.generalInfo.MissionGeneralInfo2
import fr.gouv.gmampa.rapportnav.mocks.mission.MissionGeneralInfoEntity2Mock
import fr.gouv.gmampa.rapportnav.mocks.mission.MissionGeneralInfoEntityMock
import fr.gouv.gmampa.rapportnav.mocks.mission.crew.ServiceEntityMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.Instant

@ExtendWith(SpringExtension::class)
class MissionGeneralInfo2Test {

    @Test
    fun `execute should retrieve mission general from entity`() {

        val resource1 = LegacyControlUnitResourceEntity(
            id = 1,
            name = "Ford F150",
            controlUnitId = 1
        )

        val service1 = ServiceEntityMock.create(
            id = 1,
            controlUnits = listOf(1),
            name = "TEST-1"
        )

        val generalInfoEntity = MissionGeneralInfoEntity2(
            data = MissionGeneralInfoEntityMock.create(
                id = 1,
                missionId = 1,
                service = ServiceEntityMock.create(id = 3),
                consumedGOInLiters = 2.5f,
                consumedFuelInLiters = 2.7f,
                distanceInNauticalMiles = 1.9f,
                nbrOfRecognizedVessel = 9,
                resources = listOf(resource1),
            ),
            services = listOf(service1)
        )

        val generalInfo = MissionGeneralInfo2.fromMissionGeneralInfoEntity(generalInfoEntity, isUnderJdp = true)
        assertThat(generalInfo).isNotNull();
        assertThat(generalInfo.isUnderJdp).isEqualTo(true);
        assertThat(generalInfo.id).isEqualTo(generalInfoEntity.data?.id);
        assertThat(generalInfo.missionId).isEqualTo(generalInfoEntity.data?.missionId);
        assertThat(generalInfo.service?.id).isEqualTo(generalInfoEntity.data?.service?.id);
        assertThat(generalInfo.consumedGOInLiters).isEqualTo(generalInfoEntity.data?.consumedGOInLiters);
        assertThat(generalInfo.consumedFuelInLiters).isEqualTo(generalInfoEntity.data?.consumedFuelInLiters);
        assertThat(generalInfo.distanceInNauticalMiles).isEqualTo(generalInfoEntity.data?.distanceInNauticalMiles);
        assertThat(generalInfo.nbrOfRecognizedVessel).isEqualTo(generalInfoEntity.data?.nbrOfRecognizedVessel);
    }


    @Test
    fun `execute should show missionReportType is FIELD_REPORT if none`() {
        val generalInfoEntity = MissionGeneralInfoEntity2(
            data = MissionGeneralInfoEntityMock.create(
                id = 1,
                missionId = 1,
            )
        )
        val generalInfo = MissionGeneralInfo2.fromMissionGeneralInfoEntity(generalInfoEntity)
        assertThat(generalInfo).isNotNull();
        assertThat(generalInfo.missionReportType).isEqualTo(MissionReportTypeEnum.FIELD_REPORT);
    }


    @Test
    fun `execute should retrieve mission general info entity`() {
        val missionId = 1
        val generalInfoEntity =
            MissionGeneralInfoEntityMock.create(
                id = 1,
                missionId = missionId,
                service = ServiceEntityMock.create(id = 3),
                consumedGOInLiters = 2.5f,
                consumedFuelInLiters = 2.7f,
                distanceInNauticalMiles = 1.9f,
                nbrOfRecognizedVessel = 9,
                missionReportType = MissionReportTypeEnum.FIELD_REPORT,
            )

        assertThat(generalInfoEntity).isNotNull();
        assertThat(generalInfoEntity.id).isEqualTo(1);
        assertThat(generalInfoEntity.missionId).isEqualTo(missionId);
        assertThat(generalInfoEntity.service?.id).isEqualTo(3);
        assertThat(generalInfoEntity.consumedGOInLiters).isEqualTo(2.5f);
        assertThat(generalInfoEntity.consumedFuelInLiters).isEqualTo(2.7f);
        assertThat(generalInfoEntity.distanceInNauticalMiles).isEqualTo(1.9f);
        assertThat(generalInfoEntity.nbrOfRecognizedVessel).isEqualTo(9);
    }
}
