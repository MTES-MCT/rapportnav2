package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavMissionActionRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.AssertNavActionBelongsToOwner
import fr.gouv.gmampa.rapportnav.mocks.mission.action.MissionActionModelMock
import org.assertj.core.api.Assertions.assertThatCode
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.util.Optional
import java.util.UUID

class AssertNavActionBelongsToOwnerTest {

    private val navActionRepository: INavMissionActionRepository = mock()
    private val assertNavActionBelongsToOwner = AssertNavActionBelongsToOwner(navActionRepository)

    private val owner = UUID.randomUUID()
    private val otherOwner = UUID.randomUUID()
    private val actionId = UUID.randomUUID()

    @Test
    fun `allows when the action belongs to the owner`() {
        whenever(navActionRepository.findById(actionId))
            .thenReturn(Optional.of(MissionActionModelMock.create(id = actionId, ownerId = owner)))

        assertThatCode { assertNavActionBelongsToOwner.execute(actionId.toString(), owner.toString()) }
            .doesNotThrowAnyException()
    }

    @Test
    fun `allows when the action does not exist yet (mutation will create under the authorized owner)`() {
        whenever(navActionRepository.findById(actionId)).thenReturn(Optional.empty())

        assertThatCode { assertNavActionBelongsToOwner.execute(actionId.toString(), owner.toString()) }
            .doesNotThrowAnyException()
    }

    @Test
    fun `allows a non-UUID action id (env or fish) without querying the repository`() {
        assertThatCode { assertNavActionBelongsToOwner.execute("5", owner.toString()) }
            .doesNotThrowAnyException()

        // A non-nav action id is short-circuited: no DB lookup, nothing to contain.
        verify(navActionRepository, never()).findById(any())
    }

    @Test
    fun `denies when the action belongs to another owner`() {
        whenever(navActionRepository.findById(actionId))
            .thenReturn(Optional.of(MissionActionModelMock.create(id = actionId, ownerId = otherOwner)))

        assertThatThrownBy { assertNavActionBelongsToOwner.execute(actionId.toString(), owner.toString()) }
            .isInstanceOf(BackendUsageException::class.java)
            .extracting("code")
            .isEqualTo(BackendUsageErrorCode.USER_NOT_ALLOWED_TO_PERFORM_EXCEPTION)
    }
}
