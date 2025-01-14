package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.action


import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.ControlPlansEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.EnvActionControlPlanEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.GetAllControlPlans
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.MapEnvActionControlPlans
import fr.gouv.gmampa.rapportnav.mocks.mission.action.ControlPlanMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean


@SpringBootTest(classes = [MapEnvActionControlPlans::class])
class MapEnvActionControlPlansTest {

    @Autowired
    private lateinit var mapEnvActionControlPlans: MapEnvActionControlPlans

    @MockitoBean
    private lateinit var getAllControlPlans: GetAllControlPlans

    private val controlPlanFromEnvAction1 = EnvActionControlPlanEntity(
        themeId = 1,
        subThemeIds = listOf(1),
        tagIds = listOf(1),
    )


    private val controlPlanFromEnvAction2 = EnvActionControlPlanEntity(
        themeId = 2,
        subThemeIds = listOf(2, 3),
        tagIds = listOf(2),
    )


    private val controlPlanFromEnvAction3 = EnvActionControlPlanEntity(
        themeId = 3,
        subThemeIds = listOf(2, 3),
        tagIds = listOf(2),
    )

    private val allControlPlans =
        listOf(controlPlanFromEnvAction1, controlPlanFromEnvAction2, controlPlanFromEnvAction3)

    @Test
    fun `execute returns null when getEnvActionControlPlans returns null`() {
        Mockito.`when`(getAllControlPlans.execute()).thenReturn(null)
        val result = mapEnvActionControlPlans.execute(allControlPlans)
        assertThat(result).isNull()
    }

    @Test
    fun `execute returns null when input is null`() {
        Mockito.`when`(getAllControlPlans.execute()).thenReturn(ControlPlanMock().createFullList())
        val result = mapEnvActionControlPlans.execute(null)
        assertThat(result).isNull()
    }

    @Test
    fun `execute returns null when input is empty`() {
        Mockito.`when`(getAllControlPlans.execute()).thenReturn(ControlPlanMock().createFullList())
        val result = mapEnvActionControlPlans.execute(listOf())
        assertThat(result).isNull()
    }

    @Test
    fun `execute returns one `() {
        Mockito.`when`(getAllControlPlans.execute()).thenReturn(ControlPlanMock().createFullList())
        val result = mapEnvActionControlPlans.execute(listOf(controlPlanFromEnvAction1))
        val expected = ControlPlansEntity(
            themes = listOf(ControlPlanMock().theme1),
            subThemes = listOf(ControlPlanMock().subTheme1),
            tags = listOf(ControlPlanMock().tag1)
        )
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `execute returns two `() {
        Mockito.`when`(getAllControlPlans.execute()).thenReturn(ControlPlanMock().createFullList())
        val result = mapEnvActionControlPlans.execute(listOf(controlPlanFromEnvAction2, controlPlanFromEnvAction3))
        val expected = ControlPlansEntity(
            themes = listOf(ControlPlanMock().theme2, ControlPlanMock().theme3),
            subThemes = listOf(ControlPlanMock().subTheme2, ControlPlanMock().subTheme3),
            tags = listOf(ControlPlanMock().tag2)
        )
        assertThat(result).isEqualTo(expected)
    }

}
