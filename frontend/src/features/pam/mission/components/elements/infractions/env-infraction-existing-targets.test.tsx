import { render, screen, fireEvent } from '../../../../../../test-utils.tsx'
import {
  ActionTargetTypeEnum,
  InfractionTypeEnum,
  VesselTypeEnum
} from '../../../../../common/types/env-mission-types.ts'
import EnvInfractionExistingTargets from './env-infraction-existing-targets.tsx'
import { vi } from 'vitest'
import { Infraction, InfractionByTarget } from '../../../../../common/types/infraction-types.ts'
import { ControlType } from '../../../../../common/types/control-types.ts'
import * as useAddModule from '@features/pam/mission/hooks/use-add-update-infraction-env'
import * as useDeleteModule from '@features/pam/mission/hooks/use-delete-infraction'

const mutateMock = vi.fn()
const deleteMock = vi.fn()

const infractionMock = {
  id: '123',
  controlType: ControlType.ADMINISTRATIVE,
  infractionType: InfractionTypeEnum.WITHOUT_REPORT,
  natinfs: ['123'],
  observations: undefined,
  target: undefined
}
const infractionMockEnv = {
  id: '456',
  controlType: null,
  infractionType: InfractionTypeEnum.WITHOUT_REPORT,
  natinfs: ['123'],
  observations: undefined,
  target: undefined
}
const infractionByTargetMock = (infractions: Infraction[], targetAddedByUnit: boolean = false) => ({
  vesselIdentifier: '123',
  vesselType: VesselTypeEnum.COMMERCIAL,
  infractions,
  controlTypesWithInfraction: [ControlType.ADMINISTRATIVE],
  targetAddedByUnit
})
const props = (infractionsByTarget?: InfractionByTarget[]) => ({
  availableControlTypesForInfraction: [ControlType.ADMINISTRATIVE],
  infractionsByTarget,
  actionTargetType: ActionTargetTypeEnum.VEHICLE
})

describe('EnvInfractionExistingTargets', () => {
  beforeEach(() => {
    vi.spyOn(useAddModule, 'default').mockReturnValue([mutateMock, { error: undefined }])
    vi.spyOn(useDeleteModule, 'default').mockReturnValue([deleteMock, { error: undefined }])
  })
  describe('testing rendering', () => {
    it('should not render anything when no infractionsByTarget', async () => {
      const { container } = render(<EnvInfractionExistingTargets {...props(undefined)} />)
      expect(container.firstElementChild).toBeNull()
    })
    it('should show the summary', async () => {
      render(<EnvInfractionExistingTargets {...props([infractionByTargetMock([infractionMockEnv])])} />)
      expect(screen.getByText('Infraction contrôle de l’environnement')).toBeInTheDocument()
    })
    it('should not show errors', async () => {
      render(<EnvInfractionExistingTargets {...props([infractionByTargetMock([infractionMockEnv])])} />)
      expect(screen.queryByTestId('mutation-error')).toBeNull()
    })
    it('should show the summary and then the form when clicking add infraction', async () => {
      render(<EnvInfractionExistingTargets {...props([infractionByTargetMock([infractionMockEnv])])} />)
      expect(screen.queryByTestId('env-infraction-form')).not.toBeInTheDocument()
      const button = screen.getByRole('add-infraction')
      fireEvent.click(button)
      expect(screen.getByTestId('env-infraction-form')).toBeInTheDocument()
    })
    it('should show the summary and then the form when clicking edit infraction', async () => {
      render(<EnvInfractionExistingTargets {...props([infractionByTargetMock([infractionMock])])} />)
      expect(screen.queryByTestId('env-infraction-form')).not.toBeInTheDocument()
      const button = screen.getByRole('edit-infraction')
      fireEvent.click(button)
      expect(screen.getByTestId('env-infraction-form')).toBeInTheDocument()
    })
    it('should show the summary when clicking on cancel on the form (env)', async () => {
      render(<EnvInfractionExistingTargets {...props([infractionByTargetMock([infractionMockEnv])])} />)
      expect(screen.queryByTestId('env-infraction-form')).not.toBeInTheDocument()
      const button = screen.getByRole('add-infraction')
      fireEvent.click(button)
      expect(screen.getByTestId('env-infraction-form')).toBeInTheDocument()
      const cancel = screen.getByRole('cancel-infraction')
      fireEvent.click(cancel)
      expect(screen.queryByTestId('env-infraction-form')).not.toBeInTheDocument()
    })
    it('should show the summary when clicking on cancel on the form (nav)', async () => {
      render(<EnvInfractionExistingTargets {...props([infractionByTargetMock([infractionMock], true)])} />)
      expect(screen.queryByTestId('env-infraction-form')).not.toBeInTheDocument()
      const button = screen.getByRole('add-infraction')
      fireEvent.click(button)
      expect(screen.getByTestId('env-infraction-form')).toBeInTheDocument()
      const cancel = screen.getByRole('cancel-infraction')
      fireEvent.click(cancel)
      expect(screen.queryByTestId('env-infraction-form')).not.toBeInTheDocument()
    })
  })

  describe('The Infraction Form', () => {
    it('should handle form changes', async () => {
      render(<EnvInfractionExistingTargets {...props([infractionByTargetMock([infractionMock], true)])} />)
      expect(screen.queryByTestId('env-infraction-form')).not.toBeInTheDocument()
      const button = screen.getByRole('edit-infraction')
      fireEvent.click(button)
      expect(screen.getByTestId('env-infraction-form')).toBeInTheDocument()

      const field = screen.getByRole('identityControlledPerson')
      const value = 'test'
      fireEvent.change(field, { target: { value } })
      fireEvent.blur(field)

      const obsField = screen.getByRole('observations')
      const observations = 'some observations'
      fireEvent.change(obsField, { target: { value: observations } })
      fireEvent.blur(obsField)
    })
  })

  describe('The update mutation', () => {
    it('should be called when changing clicking on the delete button', async () => {
      render(<EnvInfractionExistingTargets {...props([infractionByTargetMock([infractionMock])])} />)
      const button = screen.getByRole('edit-infraction')
      fireEvent.click(button)
      const submit = screen.getByRole('validate-infraction')
      fireEvent.click(submit)
      expect(mutateMock).toHaveBeenCalled()
    })
  })
  describe('The delete mutation', () => {
    it('should be called when changing clicking on the delete button', async () => {
      render(<EnvInfractionExistingTargets {...props([infractionByTargetMock([infractionMock])])} />)
      const button = screen.getByRole('delete-infraction')
      fireEvent.click(button)
      expect(deleteMock).toHaveBeenCalled()
    })
  })
})
