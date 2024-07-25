package fr.gouv.gmampa.rapportnav.infrastructure.bff.model.generalInfo

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.MissionGeneralInfoEntity
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.generalInfo.MissionGeneralInfo
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
class MissionGeneralInfoTest {
    @Test
    fun `execute should retrieve mission general info entity`() {
        val generalInfoEntity =
            MissionGeneralInfo(
                id = 1,
                missionId = 1,
                serviceId = 3,
                consumedGOInLiters = 2.5f,
                consumedFuelInLiters = 2.7f,
                distanceInNauticalMiles = 1.9f,
                nbrOfRecognizedVessel = 9
            ).toMissionGeneralInfoEntity();

        assertThat(generalInfoEntity).isNotNull();
        assertThat(generalInfoEntity.id).isEqualTo(1);
        assertThat(generalInfoEntity.missionId).isEqualTo(1);
        assertThat(generalInfoEntity.serviceId).isEqualTo(3);
        assertThat(generalInfoEntity.consumedGOInLiters).isEqualTo(2.5f);
        assertThat(generalInfoEntity.consumedFuelInLiters).isEqualTo(2.7f);
        assertThat(generalInfoEntity.distanceInNauticalMiles).isEqualTo(1.9f);
        assertThat(generalInfoEntity.nbrOfRecognizedVessel).isEqualTo(9);
    }

    @Test
    fun `execute should retrieve mission general from entity`() {
        val generalInfoEntity = MissionGeneralInfoEntity(
            id = 1,
            missionId = 1,
            serviceId = 3,
            consumedGOInLiters = 2.5f,
            consumedFuelInLiters = 2.7f,
            distanceInNauticalMiles = 1.9f,
            nbrOfRecognizedVessel = 9
        );
        val generalInfo = MissionGeneralInfo.fromMissionGeneralInfoEntity(generalInfoEntity);
        assertThat(generalInfo).isNotNull();
        assertThat(generalInfo?.id).isEqualTo(generalInfoEntity.id);
        assertThat(generalInfo?.missionId).isEqualTo(generalInfoEntity.missionId);
        assertThat(generalInfo?.serviceId).isEqualTo(generalInfoEntity.serviceId);
        assertThat(generalInfo?.consumedGOInLiters).isEqualTo(generalInfoEntity.consumedGOInLiters);
        assertThat(generalInfo?.consumedFuelInLiters).isEqualTo(generalInfoEntity.consumedFuelInLiters);
        assertThat(generalInfo?.distanceInNauticalMiles).isEqualTo(generalInfoEntity.distanceInNauticalMiles);
        assertThat(generalInfo?.nbrOfRecognizedVessel).isEqualTo(generalInfoEntity.nbrOfRecognizedVessel);
    }
}
