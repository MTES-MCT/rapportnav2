package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.domain.repositories.mission.generalInfo.IMissionGeneralInfoRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.DeleteGeneralInfos
import org.junit.jupiter.api.Test
import org.mockito.Mockito.verify
import org.mockito.kotlin.doNothing
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean

@SpringBootTest(classes = [DeleteGeneralInfos::class])
class DeleteGeneralInfoTest {

    @Autowired
    private lateinit var deleteGeneralInfos: DeleteGeneralInfos

    @MockitoBean
    private lateinit var generalInfoRepository: IMissionGeneralInfoRepository

    @Test
    fun `execute should delete general infos with id`() {
        // Given
        val id = 123

        // When
        doNothing().`when`(generalInfoRepository).deleteById(id = id)
        deleteGeneralInfos.execute(id = id)

        // Then
        verify(generalInfoRepository).deleteById(id = id)
    }
}
