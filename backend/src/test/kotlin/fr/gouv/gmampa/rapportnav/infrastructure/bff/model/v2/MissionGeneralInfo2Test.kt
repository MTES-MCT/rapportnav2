package fr.gouv.gmampa.rapportnav.infrastructure.bff.model.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.LegacyControlUnitResourceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.ServiceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.MissionGeneralInfoEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionGeneralInfoEntity2
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionReportTypeEnum
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.generalInfo.MissionGeneralInfo2
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

        val service1 = ServiceEntity(
            id = 1,
            controlUnits = listOf(1),
            name = "TEST-1"
        )

        val generalInfoEntity = MissionGeneralInfoEntity2(
            data = MissionGeneralInfoEntity(
                id = 1,
                missionId = 1,
                serviceId = 3,
                consumedGOInLiters = 2.5f,
                consumedFuelInLiters = 2.7f,
                distanceInNauticalMiles = 1.9f,
                nbrOfRecognizedVessel = 9,
                resources = listOf(resource1),
            ),
            services = listOf(service1)
        )

        val generalInfo = MissionGeneralInfo2.fromMissionGeneralInfoEntity(generalInfoEntity)
        assertThat(generalInfo).isNotNull();
        assertThat(generalInfo.id).isEqualTo(generalInfoEntity.data?.id);
        assertThat(generalInfo.missionId).isEqualTo(generalInfoEntity.data?.missionId);
        assertThat(generalInfo.serviceId).isEqualTo(generalInfoEntity.data?.serviceId);
        assertThat(generalInfo.consumedGOInLiters).isEqualTo(generalInfoEntity.data?.consumedGOInLiters);
        assertThat(generalInfo.consumedFuelInLiters).isEqualTo(generalInfoEntity.data?.consumedFuelInLiters);
        assertThat(generalInfo.distanceInNauticalMiles).isEqualTo(generalInfoEntity.data?.distanceInNauticalMiles);
        assertThat(generalInfo.nbrOfRecognizedVessel).isEqualTo(generalInfoEntity.data?.nbrOfRecognizedVessel);
    }


    @Test
    fun `execute should show missionReportType is FIELD_REPORT if none`() {
        val generalInfoEntity = MissionGeneralInfoEntity2(
            data = MissionGeneralInfoEntity(
                id = 1,
                missionId = 1,
                serviceId = 3
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
            MissionGeneralInfo2(
                id = 1,
                missionId = missionId,
                serviceId = 3,
                consumedGOInLiters = 2.5f,
                consumedFuelInLiters = 2.7f,
                distanceInNauticalMiles = 1.9f,
                nbrOfRecognizedVessel = 9,
                missionTypes = listOf(MissionTypeEnum.AIR),
                startDateTimeUtc = Instant.now(),
                missionReportType = MissionReportTypeEnum.FIELD_REPORT,
            ).toMissionGeneralInfoEntity(missionId = missionId);

        assertThat(generalInfoEntity).isNotNull();
        assertThat(generalInfoEntity.id).isEqualTo(1);
        assertThat(generalInfoEntity.missionId).isEqualTo(missionId);
        assertThat(generalInfoEntity.serviceId).isEqualTo(3);
        assertThat(generalInfoEntity.consumedGOInLiters).isEqualTo(2.5f);
        assertThat(generalInfoEntity.consumedFuelInLiters).isEqualTo(2.7f);
        assertThat(generalInfoEntity.distanceInNauticalMiles).isEqualTo(1.9f);
        assertThat(generalInfoEntity.nbrOfRecognizedVessel).isEqualTo(9);
    }
}
