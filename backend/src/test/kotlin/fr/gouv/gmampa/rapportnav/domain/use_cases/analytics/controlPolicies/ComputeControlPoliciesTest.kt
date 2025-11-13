package fr.gouv.gmampa.rapportnav.domain.use_cases.analytics.controlPolicies

import fr.gouv.dgampa.rapportnav.domain.entities.analytics.ControlPolicyData
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEntity2
import fr.gouv.dgampa.rapportnav.domain.use_cases.analytics.controlPolicies.ComputeControlPolicies
import fr.gouv.dgampa.rapportnav.domain.use_cases.analytics.controlPolicies.ComputeEnvControlPolicy
import fr.gouv.dgampa.rapportnav.domain.use_cases.analytics.controlPolicies.ComputeNavControlPolicy
import fr.gouv.dgampa.rapportnav.domain.use_cases.analytics.controlPolicies.ComputeProFishingControlPolicy
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.junit.jupiter.api.assertNull
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoInteractions
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean

@SpringBootTest(classes = [ComputeControlPolicies::class])
class ComputeControlPoliciesTest {

    @Autowired
    private lateinit var useCase: ComputeControlPolicies

    @MockitoBean
    private lateinit var nav: ComputeNavControlPolicy
    @MockitoBean
    private lateinit var env: ComputeEnvControlPolicy
    @MockitoBean
    private lateinit var proFishing: ComputeProFishingControlPolicy

    @Test
    fun `returns null when mission is null`() {
        val result = useCase.execute(null)
        assertNull(result)
        verifyNoInteractions(proFishing, nav, env)
    }

    @Test
    fun `calls dependencies and aggregates results`() {
        val mission = MissionEntity2(id = 123)

        val dummyResult = ControlPolicyData()
        whenever(proFishing.computeFishingRelatedInfractions(mission)).thenReturn(dummyResult)
        whenever(proFishing.computeOtherInfractions(mission)).thenReturn(dummyResult)
        whenever(nav.execute(mission, ControlType.SECURITY)).thenReturn(dummyResult)
        whenever(nav.execute(mission, ControlType.NAVIGATION)).thenReturn(dummyResult)
        whenever(nav.execute(mission, ControlType.GENS_DE_MER)).thenReturn(dummyResult)
        whenever(nav.execute(mission, ControlType.ADMINISTRATIVE)).thenReturn(dummyResult)
        whenever(env.execute(mission)).thenReturn(dummyResult)

        val result = useCase.execute(mission)

        assertNotNull(result)
        verify(proFishing).computeFishingRelatedInfractions(mission)
        verify(proFishing).computeOtherInfractions(mission)
        verify(nav).execute(mission, ControlType.SECURITY)
        verify(nav).execute(mission, ControlType.NAVIGATION)
        verify(nav).execute(mission, ControlType.GENS_DE_MER)
        verify(nav).execute(mission, ControlType.ADMINISTRATIVE)
        verify(env).execute(mission)
    }

}
