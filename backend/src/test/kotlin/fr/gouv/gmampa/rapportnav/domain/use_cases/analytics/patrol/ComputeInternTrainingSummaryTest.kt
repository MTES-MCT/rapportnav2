package fr.gouv.gmampa.rapportnav.domain.use_cases.analytics.patrol

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.PassengerEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.PassengerOrganization
import fr.gouv.dgampa.rapportnav.domain.use_cases.analytics.patrol.ComputeInternTrainingSummary
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDate
import java.util.UUID

@SpringBootTest(classes = [ComputeInternTrainingSummary::class])
class ComputeInternTrainingSummaryTest {
    @Autowired
    private lateinit var  useCase: ComputeInternTrainingSummary

    private fun passenger(
        isIntern: Boolean?,
        organization: PassengerOrganization?,
        start: LocalDate,
        end: LocalDate
    ): PassengerEntity {
        return PassengerEntity(
            id = null,
            missionId = null,
            missionIdUUID = UUID.randomUUID(),
            fullName = "Test",
            organization = organization,
            isIntern = isIntern,
            startDate = start,
            endDate = end,
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
        val p1 = passenger(true, PassengerOrganization.AFF_MAR, LocalDate.now(), LocalDate.now())
        val p2 = passenger(false, null, LocalDate.now(), LocalDate.now())

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
        val start = LocalDate.parse("2024-01-01")
        val end = LocalDate.parse("2024-01-01")

        val interns = listOf(
            passenger(true, PassengerOrganization.AFF_MAR, start, end),
            passenger(true, PassengerOrganization.ESPMER, start, end),
            passenger(true, PassengerOrganization.LYCEE, start, end),
            passenger(true, PassengerOrganization.OTHER, start, end)
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
            PassengerOrganization.AFF_MAR,
            LocalDate.parse("2024-01-01"),
            LocalDate.parse("2024-01-01")
        )

        val p2 = passenger(
            true,
            PassengerOrganization.AFF_MAR,
            LocalDate.parse("2024-01-01"),
            LocalDate.parse("2024-01-01")
        )


        val p3 = passenger(
            true,
            PassengerOrganization.AFF_MAR,
            LocalDate.parse("2024-01-01"),
            LocalDate.parse("2024-01-02")
        )

        val result = useCase.execute(listOf(p1, p2, p3))!!

        assertEquals(4, result["totalInternDurationInDays"])
    }
}
