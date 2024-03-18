package fr.gouv.gmampa.rapportnav.infrastructure.bff.model

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.infrastructure.bff.model.Mission
import fr.gouv.dgampa.rapportnav.infrastructure.bff.model.MissionStatusEnum
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.ZonedDateTime


class MissionTests {

    @Nested
    inner class CalculateMissionStatus {
        @Test
        fun `should return CLOSED when isClosed is true`() {
            val mission = Mission(
                id = 1,
                isClosed = true,
                missionSource = MissionSourceEnum.RAPPORTNAV,
                startDateTimeUtc = ZonedDateTime.now(),
                endDateTimeUtc = ZonedDateTime.now(),
                actions = emptyList(),
                openBy = "someUser"
            )

            val result = mission.calculateMissionStatus()

            assertThat(result).isEqualTo(MissionStatusEnum.CLOSED)
        }
    }

    @Test
    fun `should return UPCOMING when startDateTimeUtc is after compareDate`() {
        val mission = Mission(
            id = 1,
            isClosed = false,
            missionSource = MissionSourceEnum.RAPPORTNAV,
            startDateTimeUtc = ZonedDateTime.now().plusDays(1),
            endDateTimeUtc = ZonedDateTime.now().plusDays(2),
            actions = emptyList(),
            openBy = "someUser"
        )

        val result = mission.calculateMissionStatus()

        assertThat(result).isEqualTo(MissionStatusEnum.UPCOMING)
    }

    @Test
    fun `should return ENDED when endDateTimeUtc is before compareDate`() {
        // Test case for when mission has already ended
        val mission = Mission(
            id = 1,
            isClosed = false,
            missionSource = MissionSourceEnum.RAPPORTNAV,
            startDateTimeUtc = ZonedDateTime.now().minusDays(2),
            endDateTimeUtc = ZonedDateTime.now().minusDays(1),
            actions = emptyList(),
            openBy = "someUser"
        )

        val result = mission.calculateMissionStatus()

        assertThat(result).isEqualTo(MissionStatusEnum.ENDED)
    }

    @Test
    fun `should return PENDING when either startDateTimeUtc or endDateTimeUtc are before compareDate`() {
        // Test case for when mission is neither upcoming nor ended
        val mission = Mission(
            id = 1,
            isClosed = false,
            missionSource = MissionSourceEnum.RAPPORTNAV,
            startDateTimeUtc = ZonedDateTime.now().minusDays(2),
            endDateTimeUtc = null,
            actions = emptyList(),
            openBy = "someUser"
        )

        val result = mission.calculateMissionStatus()

        assertThat(result).isEqualTo(MissionStatusEnum.PENDING)
    }

    @Test
    fun `should return IN_PROGRESS when after startDateTimeUtc but before endDateTimeUtc`() {
        // Test case for when mission is neither upcoming nor ended
        val mission = Mission(
            id = 1,
            isClosed = false,
            missionSource = MissionSourceEnum.RAPPORTNAV,
            startDateTimeUtc = ZonedDateTime.now().minusDays(2),
            endDateTimeUtc = ZonedDateTime.now().plusDays(2),
            actions = emptyList(),
            openBy = "someUser"
        )

        val result = mission.calculateMissionStatus()

        assertThat(result).isEqualTo(MissionStatusEnum.IN_PROGRESS)
    }

}
