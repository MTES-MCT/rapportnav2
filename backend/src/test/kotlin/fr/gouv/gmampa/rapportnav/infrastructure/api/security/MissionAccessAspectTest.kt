package fr.gouv.gmampa.rapportnav.infrastructure.api.security

import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.AssertMissionAccess
import fr.gouv.dgampa.rapportnav.infrastructure.api.security.ChildAccessGuard
import fr.gouv.dgampa.rapportnav.infrastructure.api.security.CheckMissionAccess
import fr.gouv.dgampa.rapportnav.infrastructure.api.security.MissionAccessAspect
import fr.gouv.dgampa.rapportnav.infrastructure.api.security.MissionAccessResource
import fr.gouv.dgampa.rapportnav.infrastructure.api.security.MissionChildResource
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory
import org.springframework.web.bind.annotation.PathVariable

/**
 * Unit-tests [MissionAccessAspect] in isolation (no Spring container) by applying it to a sample target
 * with [AspectJProxyFactory], proving the aspect resolves the id from each annotation shape and delegates
 * to [AssertMissionAccess].
 */
class MissionAccessAspectTest {

    data class ExportBody(val missionIds: List<Int>)

    data class CreateBody(val id: String?)

    /** Stand-in for a controller; `open` so CGLIB can proxy it (controllers are opened by kotlin-spring). */
    open class SampleApi {
        @CheckMissionAccess(param = "ownerId")
        open fun byOwner(@PathVariable(name = "ownerId") ownerId: String): String = "ok"

        @CheckMissionAccess(param = "missionId")
        open fun byBareParam(missionId: String): String = "ok"

        @CheckMissionAccess(param = "inquiryId", resource = MissionAccessResource.INQUIRY)
        open fun byInquiry(inquiryId: String): String = "ok"

        @CheckMissionAccess(param = "request.missionIds", collection = true)
        open fun byExport(request: ExportBody): String = "ok"

        @CheckMissionAccess(param = "ownerId", child = "actionId", childResource = MissionChildResource.NAV_ACTION)
        open fun byChild(
            @PathVariable(name = "ownerId") ownerId: String,
            @PathVariable(name = "actionId") actionId: String,
        ): String = "ok"

        // Mirrors createAction: the child id lives in the request body (often null on create).
        @CheckMissionAccess(param = "ownerId", child = "body.id", childResource = MissionChildResource.NAV_ACTION)
        open fun byChildFromBody(
            @PathVariable(name = "ownerId") ownerId: String,
            body: CreateBody,
        ): String = "ok"
    }

    /** A [ChildAccessGuard] that records its call and optionally throws, for the NAV_ACTION resource. */
    private class RecordingChildGuard(private val onCall: (String, String) -> Unit = { _, _ -> }) :
        ChildAccessGuard {
        override val resource = MissionChildResource.NAV_ACTION
        override fun assertBelongsToOwner(childId: String, ownerId: String) = onCall(childId, ownerId)
    }

    private fun proxyFor(
        assert: AssertMissionAccess,
        childGuards: List<ChildAccessGuard> = emptyList(),
    ): SampleApi {
        val factory = AspectJProxyFactory(SampleApi())
        factory.isProxyTargetClass = true
        factory.addAspect(MissionAccessAspect(assert, childGuards))
        return factory.getProxy()
    }

    @Test
    fun `resolves the explicit path-variable name and delegates to execute`() {
        val assert = mock<AssertMissionAccess>()
        proxyFor(assert).byOwner("42")
        verify(assert).execute("42")
    }

    @Test
    fun `resolves a bare kotlin parameter name`() {
        val assert = mock<AssertMissionAccess>()
        proxyFor(assert).byBareParam("7")
        verify(assert).execute("7")
    }

    @Test
    fun `routes the INQUIRY resource to executeForInquiry`() {
        val assert = mock<AssertMissionAccess>()
        val id = "11111111-1111-1111-1111-111111111111"
        proxyFor(assert).byInquiry(id)
        verify(assert).executeForInquiry(id)
    }

    @Test
    fun `checks each distinct id of a collection read from a request body`() {
        val assert = mock<AssertMissionAccess>()
        proxyFor(assert).byExport(ExportBody(listOf(1, 1, 2)))
        verify(assert).execute("1")
        verify(assert).execute("2")
    }

    @Test
    fun `propagates a denial thrown by the use case and skips the method body`() {
        val assert = mock<AssertMissionAccess>()
        whenever(assert.execute("42")).thenAnswer {
            throw BackendUsageException(
                code = BackendUsageErrorCode.USER_NOT_ALLOWED_TO_PERFORM_EXCEPTION,
                message = "denied",
            )
        }
        val ex = assertThrows<BackendUsageException> { proxyFor(assert).byOwner("42") }
        assertEquals(BackendUsageErrorCode.USER_NOT_ALLOWED_TO_PERFORM_EXCEPTION, ex.code)
    }

    @Test
    fun `runs the containment guard with the child and owner ids after the access check`() {
        val assert = mock<AssertMissionAccess>()
        val calls = mutableListOf<Pair<String, String>>()
        val guard = RecordingChildGuard { childId, ownerId -> calls.add(childId to ownerId) }

        proxyFor(assert, listOf(guard)).byChild(ownerId = "owner-1", actionId = "action-9")

        verify(assert).execute("owner-1")
        assertEquals(listOf("action-9" to "owner-1"), calls)
    }

    @Test
    fun `runs the containment guard for a child id carried in the request body`() {
        val assert = mock<AssertMissionAccess>()
        val calls = mutableListOf<Pair<String, String>>()
        val guard = RecordingChildGuard { childId, ownerId -> calls.add(childId to ownerId) }

        proxyFor(assert, listOf(guard)).byChildFromBody(ownerId = "owner-1", body = CreateBody(id = "action-9"))

        verify(assert).execute("owner-1")
        assertEquals(listOf("action-9" to "owner-1"), calls)
    }

    @Test
    fun `skips the containment guard when the body carries no id (create with a server-minted id)`() {
        val assert = mock<AssertMissionAccess>()
        val calls = mutableListOf<Pair<String, String>>()
        val guard = RecordingChildGuard { childId, ownerId -> calls.add(childId to ownerId) }

        val result = proxyFor(assert, listOf(guard)).byChildFromBody(ownerId = "owner-1", body = CreateBody(id = null))

        // owner access is still enforced, but there is no existing child to contain, so the guard is skipped
        verify(assert).execute("owner-1")
        assertEquals(emptyList<Pair<String, String>>(), calls)
        assertEquals("ok", result)
    }

    @Test
    fun `propagates a denial thrown by the containment guard and skips the method body`() {
        val assert = mock<AssertMissionAccess>()
        val guard = RecordingChildGuard { _, _ ->
            throw BackendUsageException(
                code = BackendUsageErrorCode.USER_NOT_ALLOWED_TO_PERFORM_EXCEPTION,
                message = "child does not belong to owner",
            )
        }

        val ex = assertThrows<BackendUsageException> {
            proxyFor(assert, listOf(guard)).byChild(ownerId = "owner-1", actionId = "action-9")
        }
        assertEquals(BackendUsageErrorCode.USER_NOT_ALLOWED_TO_PERFORM_EXCEPTION, ex.code)
    }

    @Test
    fun `fails when a child check is declared but no matching guard is registered`() {
        val assert = mock<AssertMissionAccess>()

        val ex = assertThrows<BackendUsageException> {
            proxyFor(assert, emptyList()).byChild(ownerId = "owner-1", actionId = "action-9")
        }
        assertEquals(BackendUsageErrorCode.INVALID_PARAMETERS_EXCEPTION, ex.code)
    }
}
