package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.control

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlAdministrativeEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlResult
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.control.IControlAdministrativeRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.control.IControlGensDeMerRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.control.IControlNavigationRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.control.IControlSecurityRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.control.AddOrUpdateControl
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.control.GetControlByActionId
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.control.ControlAdministrativeModel
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.*


@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [AddOrUpdateControl::class])
class AddOrUpdateControlTest {

    @MockBean
    private lateinit var controlAdministrativeRepo: IControlAdministrativeRepository

    @MockBean
    private lateinit var controlSecurityRepo: IControlSecurityRepository

    @MockBean
    private lateinit var controlNavigationRepo: IControlNavigationRepository

    @MockBean
    private lateinit var controlGensDeMerRepo: IControlGensDeMerRepository

    @MockBean
    private lateinit var getControlByActionId: GetControlByActionId

    @Autowired
    private lateinit var addOrUpdateControl: AddOrUpdateControl

    @Captor
    lateinit var controlArgumentCaptor: ArgumentCaptor<ControlAdministrativeEntity>

    @Test
    fun `should add new ControlAdministrativeEntity when no existing control`() {
        // Arrange
        val controlAdministrativeEntity = ControlAdministrativeEntity(
            id = UUID.randomUUID(),
            missionId = 1,
            actionControlId = "action-123",
            amountOfControls = 5,
            unitShouldConfirm = false,
            unitHasConfirmed = null,
            compliantOperatingPermit = ControlResult.NOT_CONCERNED
        )

        whenever(getControlByActionId.getControlAdministrative(controlAdministrativeEntity.actionControlId)).thenReturn(
            null
        )
        whenever(controlAdministrativeRepo.save(any())).thenAnswer {
            ControlAdministrativeModel.fromControlAdministrativeEntity(
                it.arguments[0] as ControlAdministrativeEntity
            )
        }

        // Act
        val result = addOrUpdateControl.addOrUpdateControlAdministrative(controlAdministrativeEntity)

        // Assert
        assertEquals(controlAdministrativeEntity.actionControlId, result.actionControlId)
        verify(controlAdministrativeRepo).save(controlAdministrativeEntity)
    }

    @Test
    fun `should update existing ControlAdministrativeEntity when control exists`() {
        // Arrange
        val existingControl = ControlAdministrativeEntity(
            id = UUID.randomUUID(),
            missionId = 1,
            actionControlId = "action-123",
            amountOfControls = 5,
            unitShouldConfirm = true,
            unitHasConfirmed = false, // Existing control has not been confirmed
            compliantOperatingPermit = ControlResult.NOT_CONCERNED
        )

        val controlToUpdate = ControlAdministrativeEntity(
            id = UUID.randomUUID(), // New ID, this will be ignored
            missionId = 1,
            actionControlId = "action-123", // Same actionControlId as the existing one
            amountOfControls = 10,
            unitShouldConfirm = true,
            unitHasConfirmed = null, // This will trigger the toggle
            compliantOperatingPermit = ControlResult.NOT_CONCERNED
        )

        whenever(getControlByActionId.getControlAdministrative(controlToUpdate.actionControlId)).thenReturn(
            existingControl
        )
        whenever(controlAdministrativeRepo.save(any())).thenAnswer {
            ControlAdministrativeModel.fromControlAdministrativeEntity(
                it.arguments[0] as ControlAdministrativeEntity
            )
        }

        // Act
        val result = addOrUpdateControl.addOrUpdateControlAdministrative(controlToUpdate)

        // Assert
        assertEquals(existingControl.id, result.id) // The existing ID should be used
        assertEquals(true, result.unitHasConfirmed) // `shouldToggleOnUnitHasConfirmed` should set this to true
        verify(controlAdministrativeRepo).save(any()) // Verify save was called
    }


}
