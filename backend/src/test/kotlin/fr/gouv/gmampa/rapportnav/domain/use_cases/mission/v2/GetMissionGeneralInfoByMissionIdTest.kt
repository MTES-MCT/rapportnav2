package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.JdpTypeEnum
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.generalInfo.IGeneralInfoRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.generalInfo.GetMissionGeneralInfoByMissionId
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.generalInfo.GeneralInfoModel
import fr.gouv.gmampa.rapportnav.mocks.mission.GeneralInfoEntityMock
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.util.*

@SpringBootTest(classes = [GetMissionGeneralInfoByMissionId::class])
class GetMissionGeneralInfoByMissionIdTest {

    @Autowired
    private lateinit var getMissionGeneralInfoByMissionId: GetMissionGeneralInfoByMissionId

    @MockitoBean
    private lateinit var infoRepo: IGeneralInfoRepository

    @Test
    fun `execute should generalInfo by missionId`() {
        val missionId = 761
        val model = getMissionGeneralInfoModel(missionId = missionId)
        `when`(infoRepo.findByMissionId(missionId)).thenReturn(Optional.of(model))
        getMissionGeneralInfoByMissionId = GetMissionGeneralInfoByMissionId(infoRepo = infoRepo)

        // When
        getMissionGeneralInfoByMissionId.execute(missionId)

        // Then
        verify(infoRepo, times(1)).findByMissionId(missionId)
    }

    @Test
    fun `execute should return general info by uuid`() {
        val missionIdUUID = UUID.randomUUID()
        val model = getMissionGeneralInfoModel(missionIdUUID = missionIdUUID)

        // When
        getMissionGeneralInfoByMissionId.execute(missionIdUUID)

        // Then
        verify(infoRepo, times(1)).findByMissionIdUUID(missionIdUUID)
    }

    private fun getMissionGeneralInfoModel(missionId: Int? = null, missionIdUUID: UUID? = null): GeneralInfoModel {
        val model = GeneralInfoEntityMock.create(
            id = 1,
            missionId = missionId,
            consumedGOInLiters = 2.5f,
            consumedFuelInLiters = 2.7f,
            distanceInNauticalMiles = 1.9f,
            nbrOfRecognizedVessel = 9,
            jdpType = JdpTypeEnum.DOCKED,
            missionIdUUID = missionIdUUID
        ).toGeneralInfoModel()
        return model
    }
}
