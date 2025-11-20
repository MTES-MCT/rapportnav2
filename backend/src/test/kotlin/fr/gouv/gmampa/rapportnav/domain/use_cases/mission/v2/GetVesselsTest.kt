package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.v2

import com.neovisionaries.i18n.CountryCode
import fr.gouv.cnsp.monitorfish.infrastructure.api.outputs.VesselIdentityDataOutput
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.TargetEntity2
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IFishActionRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.target2.v2.ITargetRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetVessels
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.ProcessMissionActionTarget
import fr.gouv.gmampa.rapportnav.mocks.mission.TargetEntity2Mock
import fr.gouv.gmampa.rapportnav.mocks.mission.TargetModel2Mock
import fr.gouv.gmampa.rapportnav.mocks.mission.VesselEntityMock
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.util.UUID

@SpringBootTest(classes = [GetVessels::class])
class GetVesselsTest {
    @Autowired
    private lateinit var useCase: GetVessels

    @MockitoBean
    private lateinit var repository: IFishActionRepository

    @Test
    fun `execute should map repository output`() {
        val vessel = VesselEntityMock.create()
        val vesselOutput = VesselIdentityDataOutput(
            vesselId = 1,
            flagState = CountryCode.FR
        )
        whenever(repository.getVessels()).thenReturn(listOf(vesselOutput))

        val result = useCase.execute()

        assertEquals(vessel.vesselId, result.get(0).vesselId)
        verify(repository).getVessels()
    }
}
