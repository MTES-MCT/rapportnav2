package fr.gouv.gmampa.rapportnav.infrastructure.database.model.mission.generalinfo

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.MissionGeneralInfoEntity
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.generalInfo.MissionGeneralInfoModel
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [MissionGeneralInfoModel::class])
class MissionGeneralInfoModelTest {

    @Test
    fun `execute should retrieve mission general info entity`() {
        val generalInfoEntity =
            MissionGeneralInfoModel(
                id = 1,
                missionId = 1,
                serviceId = 3,
                consumedGOInLiters = 2.5f,
                consumedFuelInLiters = 2.7f,
                distanceInNauticalMiles = 1.9f
            ).toMissionGeneralInfoEntity();

        assertThat(generalInfoEntity).isNotNull();
        assertThat(generalInfoEntity.id).isEqualTo(1);
        assertThat(generalInfoEntity.missionId).isEqualTo(1);
        assertThat(generalInfoEntity.serviceId).isEqualTo(3);
        assertThat(generalInfoEntity.consumedGOInLiters).isEqualTo(2.5f);
        assertThat(generalInfoEntity.consumedFuelInLiters).isEqualTo(2.7f);
        assertThat(generalInfoEntity.distanceInNauticalMiles).isEqualTo(1.9f);
    }

    @Test
    fun `execute should retrieve mission general modal from entity`() {
        val generalInfoEntity = MissionGeneralInfoEntity(
            id = 1,
            missionId = 1,
            serviceId = 3,
            consumedGOInLiters = 2.5f,
            consumedFuelInLiters = 2.7f,
            distanceInNauticalMiles = 1.9f
        );
        val generalInfoModel = MissionGeneralInfoModel.fromMissionGeneralInfoEntity(generalInfoEntity);
        assertThat(generalInfoModel).isNotNull();
        assertThat(generalInfoModel.id).isEqualTo(generalInfoEntity.id);
        assertThat(generalInfoModel.missionId).isEqualTo(generalInfoEntity.missionId);
        assertThat(generalInfoModel.serviceId).isEqualTo(generalInfoEntity.serviceId);
        assertThat(generalInfoModel.consumedGOInLiters).isEqualTo(generalInfoEntity.consumedGOInLiters);
        assertThat(generalInfoModel.consumedFuelInLiters).isEqualTo(generalInfoEntity.consumedFuelInLiters);
        assertThat(generalInfoModel.distanceInNauticalMiles).isEqualTo(generalInfoEntity.distanceInNauticalMiles);
    }
}
