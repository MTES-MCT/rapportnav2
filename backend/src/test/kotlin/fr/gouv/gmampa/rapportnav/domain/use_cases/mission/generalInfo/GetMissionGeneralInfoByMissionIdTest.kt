package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.generalInfo

import fr.gouv.dgampa.rapportnav.domain.repositories.mission.generalInfo.IMissionGeneralInfoRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.generalInfo.GetMissionGeneralInfoByMissionId
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.generalInfo.MissionGeneralInfoModel
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.*

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [GetMissionGeneralInfoByMissionId::class])
class GetMissionGeneralInfoByMissionIdTest {

    @MockitoBean
    private lateinit var infoRepo: IMissionGeneralInfoRepository

    @Test
    fun `execute with missionId should return entity when found`() {
        val model = MissionGeneralInfoModel(id = 1, missionId = 100, distanceInNauticalMiles = 50f)
        `when`(infoRepo.findByMissionId(100)).thenReturn(Optional.of(model))

        val useCase = GetMissionGeneralInfoByMissionId(infoRepo)
        val result = useCase.execute(missionId = 100)

        assertThat(result).isNotNull
        assertThat(result?.missionId).isEqualTo(100)
        assertThat(result?.distanceInNauticalMiles).isEqualTo(50f)
        verify(infoRepo).findByMissionId(100)
    }

    @Test
    fun `execute with missionId should return null when not found`() {
        `when`(infoRepo.findByMissionId(999)).thenReturn(Optional.empty())

        val useCase = GetMissionGeneralInfoByMissionId(infoRepo)
        val result = useCase.execute(missionId = 999)

        assertThat(result).isNull()
    }

    @Test
    fun `execute with missionIdUUID should return entity when found`() {
        val uuid = UUID.randomUUID()
        val model = MissionGeneralInfoModel(id = 2, missionId = 200)
        `when`(infoRepo.findByMissionIdUUID(uuid)).thenReturn(Optional.of(model))

        val useCase = GetMissionGeneralInfoByMissionId(infoRepo)
        val result = useCase.execute(missionIdUUID = uuid)

        assertThat(result).isNotNull
        assertThat(result?.missionId).isEqualTo(200)
        verify(infoRepo).findByMissionIdUUID(uuid)
    }

    @Test
    fun `execute with missionIdUUID should return null when not found`() {
        val uuid = UUID.randomUUID()
        `when`(infoRepo.findByMissionIdUUID(uuid)).thenReturn(Optional.empty())

        val useCase = GetMissionGeneralInfoByMissionId(infoRepo)
        val result = useCase.execute(missionIdUUID = uuid)

        assertThat(result).isNull()
    }
}