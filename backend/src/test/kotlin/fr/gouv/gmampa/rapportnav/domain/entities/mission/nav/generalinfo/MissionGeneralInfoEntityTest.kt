package fr.gouv.gmampa.rapportnav.domain.entities.mission.nav.generalinfo

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.GeneralInfoEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.JdpTypeEnum
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.generalInfo.GeneralInfoModel
import fr.gouv.gmampa.rapportnav.mocks.mission.crew.ServiceEntityMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.*

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [GeneralInfoModel::class])
class MissionGeneralInfoEntityTest {

    @Test
    fun `execute should retrieve mission general info entity`() {
        val missionIdUUID = UUID.randomUUID()
        val generalInfoEntity = GeneralInfoEntity.fromGeneralInfoModel(
            GeneralInfoModel(
                id = 1,
                missionId = 1,
                service = ServiceEntityMock.create(id = 3).toServiceModel(),
                consumedGOInLiters = 2.5f,
                consumedFuelInLiters = 2.7f,
                distanceInNauticalMiles = 1.9f,
                nbrOfRecognizedVessel = 9,
                jdpType = JdpTypeEnum.DOCKED,
                missionIdUUID = missionIdUUID
            )
        )

        assertThat(generalInfoEntity).isNotNull();
        assertThat(generalInfoEntity.id).isEqualTo(1);
        assertThat(generalInfoEntity.missionId).isEqualTo(1);
        assertThat(generalInfoEntity.service?.id).isEqualTo(3);
        assertThat(generalInfoEntity.consumedGOInLiters).isEqualTo(2.5f);
        assertThat(generalInfoEntity.consumedFuelInLiters).isEqualTo(2.7f);
        assertThat(generalInfoEntity.distanceInNauticalMiles).isEqualTo(1.9f);
        assertThat(generalInfoEntity.nbrOfRecognizedVessel).isEqualTo(9);
        assertThat(generalInfoEntity.jdpType).isEqualTo(JdpTypeEnum.DOCKED);
        assertThat(generalInfoEntity.missionIdUUID).isEqualTo(missionIdUUID);
    }

    @Test
    fun `execute should retrieve mission general modal from entity`() {
        val missionIdUUID = UUID.randomUUID()
        val generalInfoEntity = GeneralInfoEntity(
            id = 1,
            missionId = 1,
            service = ServiceEntityMock.create(id = 3),
            consumedGOInLiters = 2.5f,
            consumedFuelInLiters = 2.7f,
            distanceInNauticalMiles = 1.9f,
            nbrOfRecognizedVessel = 9,
            jdpType = JdpTypeEnum.DOCKED,
            missionIdUUID = missionIdUUID
        );
        val generalInfoModel = generalInfoEntity.toGeneralInfoModel()
        assertThat(generalInfoModel).isNotNull();
        assertThat(generalInfoModel.id).isEqualTo(generalInfoEntity.id);
        assertThat(generalInfoModel.missionId).isEqualTo(generalInfoEntity.missionId);
        assertThat(generalInfoModel.service?.id).isEqualTo(generalInfoEntity.service?.id);
        assertThat(generalInfoModel.consumedGOInLiters).isEqualTo(generalInfoEntity.consumedGOInLiters);
        assertThat(generalInfoModel.consumedFuelInLiters).isEqualTo(generalInfoEntity.consumedFuelInLiters);
        assertThat(generalInfoModel.distanceInNauticalMiles).isEqualTo(generalInfoEntity.distanceInNauticalMiles);
        assertThat(generalInfoModel.nbrOfRecognizedVessel).isEqualTo(generalInfoEntity.nbrOfRecognizedVessel);
        assertThat(generalInfoEntity.jdpType).isEqualTo(JdpTypeEnum.DOCKED);
        assertThat(generalInfoEntity.missionIdUUID).isEqualTo(missionIdUUID);
    }
}
