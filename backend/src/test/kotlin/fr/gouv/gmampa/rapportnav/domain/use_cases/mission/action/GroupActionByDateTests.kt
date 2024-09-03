package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.MissionActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ExtendedEnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ExtendedFishActionEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.GroupActionByDate
import fr.gouv.gmampa.rapportnav.mocks.mission.action.EnvActionControlMock
import fr.gouv.gmampa.rapportnav.mocks.mission.action.EnvActionSurveillanceMock
import fr.gouv.gmampa.rapportnav.mocks.mission.action.FishActionControlMock
import fr.gouv.gmampa.rapportnav.mocks.mission.action.NavActionStatusMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.Instant
import java.time.LocalDate

@SpringBootTest(classes = [GroupActionByDate::class])
class GroupActionByDateTest {

    @Autowired
    private lateinit var groupActionByDate: GroupActionByDate

    @Test
    fun `execute should return null when actions is null`() {
        val result = groupActionByDate.execute(null)
        assertThat(result).isNull()
    }

    @Test
    fun `execute should group actions by day`() {
        // Mock mission actions
        val actionEnvControl1 = MissionActionEntity.EnvAction(
            ExtendedEnvActionEntity.fromEnvActionEntity(
                EnvActionControlMock.create(
                    actionStartDateTimeUtc = Instant.parse("2022-01-01T12:00:01Z"),
                )
            )
        )
        val actionEnvSurveillance1 = MissionActionEntity.EnvAction(
            ExtendedEnvActionEntity.fromEnvActionEntity(
                EnvActionSurveillanceMock.create(
                    actionStartDateTimeUtc = Instant.parse("2022-01-01T12:00:01Z"),
                )
            )
        )

        val action2 = MissionActionEntity.FishAction(
            ExtendedFishActionEntity.fromMissionAction(
                FishActionControlMock.create(
                    missionId = 1,
                    actionDatetimeUtc = Instant.parse("2022-01-01T12:00:01Z"),
                )
            )
        )

        val action3 = MissionActionEntity.NavAction(
            NavActionStatusMock.createActionStatusEntity().toNavActionEntity()
        )

        val actions = listOf(actionEnvControl1, actionEnvSurveillance1, action2, action3)

        val result = groupActionByDate.execute(actions)

        assertThat(result).isNotNull()
        assertThat(result!!.size).isEqualTo(2)
        assertThat(result[LocalDate.of(2022, 1, 1)]?.size).isEqualTo(3)
        assertThat(result[LocalDate.of(2022, 1, 2)]?.size).isEqualTo(1)
    }


}
