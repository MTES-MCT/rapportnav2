package fr.gouv.gmampa.rapportnav.domain.use_cases.analytics.patrol

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.MissionPassengerEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.MissionPassengerOrganization
import fr.gouv.dgampa.rapportnav.domain.use_cases.analytics.patrol.ComputeInternTrainingSummary
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.Instant
import java.util.UUID

@SpringBootTest(classes = [ComputeInternTrainingSummary::class])
class ComputeInternTrainingSummaryTest {
    @Autowired
    private lateinit var  useCase: ComputeInternTrainingSummary

    private fun passenger(
        isIntern: Boolean?,
        organization: MissionPassengerOrganization?,
        start: Instant,
        end: Instant
    ): MissionPassengerEntity {
        return MissionPassengerEntity(
            id = null,
            missionId = null,
            missionIdUUID = UUID.randomUUID(),
            fullName = "Test",
            organization = organization,
            isIntern = isIntern,
            startDateTimeUtc = start,
            endDateTimeUtc = end,
        )
    }

    // -------------------------------------------------------------------------
    // TEST 1 — null passengers
    // -------------------------------------------------------------------------
    @Test
    fun `execute returns null when passengers is null`() {
        val result = useCase.execute(null)
        assertNull(result)
    }

    // -------------------------------------------------------------------------
    // TEST 2 — empty list
    // -------------------------------------------------------------------------
    @Test
    fun `execute returns null when passengers is empty`() {
        val result = useCase.execute(emptyList())
        assertNull(result)
    }

    // -------------------------------------------------------------------------
    // TEST 3 — mixed interns and non interns
    // -------------------------------------------------------------------------
    @Test
    fun `execute counts interns and total passengers`() {
        val p1 = passenger(true, MissionPassengerOrganization.AFF_MAR, Instant.now(), Instant.now())
        val p2 = passenger(false, null, Instant.now(), Instant.now())

        val result = useCase.execute(listOf(p1, p2))!!

        assertEquals(2, result["nbPassengers"])
        assertEquals(1, result["nbInterns"])
        assertEquals(1, result["nbAffMarInterns"])
    }

    // -------------------------------------------------------------------------
    // TEST 4 — organization mapping counts by category
    // -------------------------------------------------------------------------
    @Test
    fun `execute counts interns per organization`() {
        val start = Instant.parse("2024-01-01T00:00:00Z")
        val end = Instant.parse("2024-01-01T01:00:00Z")

        val interns = listOf(
            passenger(true, MissionPassengerOrganization.AFF_MAR, start, end),
            passenger(true, MissionPassengerOrganization.ESPMER, start, end),
            passenger(true, MissionPassengerOrganization.LYCEE, start, end),
            passenger(true, MissionPassengerOrganization.OTHER, start, end)
        )

        val result = useCase.execute(interns)!!

        assertEquals(1, result["nbAffMarInterns"])
        assertEquals(1, result["nbPostgraduateInterns"])
        assertEquals(1, result["nbHighSchoolInterns"])
        assertEquals(1, result["nbOtherInterns"])
    }

    // -------------------------------------------------------------------------
    // TEST 5 — total intern hours computed correctly
    // -------------------------------------------------------------------------
    @Test
    fun `execute calculates total intern duration`() {
        val p1 = passenger(
            true,
            MissionPassengerOrganization.AFF_MAR,
            Instant.parse("2024-01-01T00:00:00Z"),
            Instant.parse("2024-01-01T02:00:00Z")
        ) // 2h

        val p2 = passenger(
            true,
            MissionPassengerOrganization.AFF_MAR,
            Instant.parse("2024-01-01T00:00:00Z"),
            Instant.parse("2024-01-01T01:30:00Z")
        ) // 1.5h → durationInHours likely floors or rounds (depends on your utils)

        val result = useCase.execute(listOf(p1, p2))!!

        // We test for sum of .toInt() durations (2 + 1 = 3)
        assertEquals(3, result["totalInternDurationInHours"])
    }
}
