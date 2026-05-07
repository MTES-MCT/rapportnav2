package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.generalInfo

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.MissionGeneralInfoEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.generalInfo.IMissionGeneralInfoRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.generalInfo.AddOrUpdateMissionGeneralInfo
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.generalInfo.MissionGeneralInfoModel
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.Mockito.never
import org.mockito.kotlin.any
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.IncorrectResultSizeDataAccessException
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.*

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [AddOrUpdateMissionGeneralInfo::class])
class AddOrUpdateMissionGeneralInfoTest {

    @MockitoBean
    private lateinit var infoRepo: IMissionGeneralInfoRepository

    private lateinit var useCase: AddOrUpdateMissionGeneralInfo

    @BeforeEach
    fun setUp() {
        useCase = AddOrUpdateMissionGeneralInfo(infoRepo)
    }

    @Test
    fun `execute should throw when missionId is null`() {
        val entity = MissionGeneralInfoEntity(missionId = null)

        assertThrows<IllegalArgumentException> {
            useCase.execute(entity)
        }
    }

    @Test
    fun `execute should create new record when none exists`() {
        val entity = MissionGeneralInfoEntity(missionId = 100, distanceInNauticalMiles = 50f)
        val savedModel = MissionGeneralInfoModel(id = 1, missionId = 100, distanceInNauticalMiles = 50f)

        `when`(infoRepo.findByMissionId(100)).thenReturn(Optional.empty())
        `when`(infoRepo.save(any())).thenReturn(savedModel)

        val result = useCase.execute(entity)

        assertThat(result).isNotNull
        assertThat(result.missionId).isEqualTo(100)
        verify(infoRepo).save(any())
    }

    @Test
    fun `execute should update existing record preserving id`() {
        val existingModel = MissionGeneralInfoModel(id = 5, missionId = 100, distanceInNauticalMiles = 30f)
        val updatedEntity = MissionGeneralInfoEntity(missionId = 100, distanceInNauticalMiles = 60f)
        val savedModel = MissionGeneralInfoModel(id = 5, missionId = 100, distanceInNauticalMiles = 60f)

        `when`(infoRepo.findByMissionId(100)).thenReturn(Optional.of(existingModel))
        `when`(infoRepo.save(any())).thenReturn(savedModel)

        val result = useCase.execute(updatedEntity)

        assertThat(result).isNotNull
        assertThat(result.distanceInNauticalMiles).isEqualTo(60f)
        verify(infoRepo).save(any())
    }

    @Test
    fun `execute should cleanup duplicates when IncorrectResultSizeDataAccessException`() {
        val entity = MissionGeneralInfoEntity(missionId = 100)
        val dup1 = MissionGeneralInfoModel(id = 1, missionId = 100)
        val dup2 = MissionGeneralInfoModel(id = 2, missionId = 100)
        val savedModel = MissionGeneralInfoModel(id = 1, missionId = 100)

        // First call throws, second call succeeds after cleanup
        `when`(infoRepo.findByMissionId(100))
            .thenThrow(IncorrectResultSizeDataAccessException(1))
            .thenReturn(Optional.of(dup1))
        `when`(infoRepo.findAllByMissionId(100)).thenReturn(listOf(dup1, dup2))
        `when`(infoRepo.save(any())).thenReturn(savedModel)

        val result = useCase.execute(entity)

        assertThat(result).isNotNull
        verify(infoRepo).findAllByMissionId(100)
        verify(infoRepo).deleteById(2) // keeps id=1 (min), deletes id=2
        verify(infoRepo, never()).deleteById(1)
    }
}