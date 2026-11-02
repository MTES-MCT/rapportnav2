package fr.gouv.gmampa.rapportnav.infrastructure.api.admin

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.PamBackfillReport
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.PamBackfillSkip
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.PamBackfillSkipReason
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.generalInfo.BackfillPamResourceUsage
import fr.gouv.dgampa.rapportnav.infrastructure.api.admin.ResourceUsageAdminController
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

// Security is off here (addFilters = false); the ROLE_ADMIN rule on /api/v2/admin/** is covered by SecurityConfigTest.
@WebMvcTest(ResourceUsageAdminController::class)
@ContextConfiguration(classes = [ResourceUsageAdminController::class])
@AutoConfigureMockMvc(addFilters = false)
class ResourceUsageAdminControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var backfillPamResourceUsage: BackfillPamResourceUsage

    private fun report(dryRun: Boolean) = PamBackfillReport(
        dryRun = dryRun,
        scanned = 3,
        created = 1,
        updated = 0,
        unchanged = 1,
        skipped = listOf(
            PamBackfillSkip(generalInfoId = 7, missionId = 123, missionIdUUID = null, reason = PamBackfillSkipReason.NO_SHIP)
        )
    )

    @Test
    fun `backfill-pam is a dry run when dryRun is omitted`() {
        `when`(backfillPamResourceUsage.execute(dryRun = true)).thenReturn(report(dryRun = true))

        mockMvc.perform(post("/api/v2/admin/resource-usage/backfill-pam"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.dryRun").value(true))

        verify(backfillPamResourceUsage).execute(dryRun = true)
        verify(backfillPamResourceUsage, never()).execute(dryRun = false)
    }

    @Test
    fun `backfill-pam writes when dryRun is false and returns the report`() {
        `when`(backfillPamResourceUsage.execute(dryRun = false)).thenReturn(report(dryRun = false))

        mockMvc.perform(post("/api/v2/admin/resource-usage/backfill-pam?dryRun=false"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.dryRun").value(false))
            .andExpect(jsonPath("$.scanned").value(3))
            .andExpect(jsonPath("$.created").value(1))
            .andExpect(jsonPath("$.updated").value(0))
            .andExpect(jsonPath("$.unchanged").value(1))
            .andExpect(jsonPath("$.skipped.length()").value(1))
            .andExpect(jsonPath("$.skipped[0].generalInfoId").value(7))
            .andExpect(jsonPath("$.skipped[0].missionId").value(123))
            .andExpect(jsonPath("$.skipped[0].reason").value("NO_SHIP"))

        verify(backfillPamResourceUsage).execute(dryRun = false)
    }

    @Test
    fun `backfill-pam only accepts POST`() {
        mockMvc.perform(get("/api/v2/admin/resource-usage/backfill-pam"))
            .andExpect(status().isMethodNotAllowed)
    }
}
