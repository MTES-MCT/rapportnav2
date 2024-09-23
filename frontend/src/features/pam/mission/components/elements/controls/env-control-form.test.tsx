import { ControlAdministrative, ControlType } from '@common/types/control-types.ts'
import { vi } from 'vitest'
import { fireEvent, render, screen, waitFor } from '../../../../../../test-utils.tsx'
import EnvControlForm, { EnvControlFormProps } from './env-control-form.tsx'

import { act } from 'react'
import * as useControlHook from '../../../hooks/control/use-control'

const updateControlMock = vi.fn()
const toogleControlMock = vi.fn()
const controlChangedMock = vi.fn()

const dummy_control = {
  id: '123',
  amountOfControls: 4
} as ControlAdministrative

describe('EnvControlForm', () => {
  afterEach(() => {
    vi.clearAllMocks()
    vi.resetAllMocks()
  })
  describe('the Checkbox in the title', () => {
    it('should be checked when Control has data', () => {
      const props: EnvControlFormProps = {
        data: dummy_control,
        shouldCompleteControl: false,
        controlType: ControlType.ADMINISTRATIVE
      }

      render(<EnvControlForm {...props} />)
      const checkbox = screen.getByRole('checkbox')
      expect(checkbox).toBeChecked()
    })
    it('should be checked when shouldCompleteControl is true', () => {
      const props: EnvControlFormProps = {
        data: undefined,
        shouldCompleteControl: true,
        controlType: ControlType.ADMINISTRATIVE
      }

      render(<EnvControlForm {...props} />)
      const checkbox = screen.getByRole('checkbox')
      expect(checkbox).toBeChecked()
    })
    it('should not be checked when shouldCompleteControl is false and no data', () => {
      const props: EnvControlFormProps = {
        data: undefined,
        shouldCompleteControl: false,
        controlType: ControlType.ADMINISTRATIVE
      }

      render(<EnvControlForm {...props} />)
      const checkbox = screen.getByRole('checkbox')
      expect(checkbox).not.toBeChecked()
    })
  })

  describe('the red dot in the title', () => {
    it('should be shown when no data but shouldCompleteControl', () => {
      const props: EnvControlFormProps = {
        data: undefined,
        shouldCompleteControl: true,
        controlType: ControlType.ADMINISTRATIVE
      }

      render(<EnvControlForm {...props} />)
      const redDot = screen.queryByText('●')
      expect(redDot).not.toBeNull()
    })
    it('should not be shown when data and shouldCompleteControl', () => {
      const props: EnvControlFormProps = {
        data: dummy_control,
        shouldCompleteControl: true,
        controlType: ControlType.ADMINISTRATIVE
      }

      render(<EnvControlForm {...props} />)
      const redDot = screen.queryByText('●')
      expect(redDot).toBeNull()
    })
    it('should not be shown when shouldCompleteControl is false', () => {
      const props: EnvControlFormProps = {
        data: dummy_control,
        shouldCompleteControl: false,
        controlType: ControlType.ADMINISTRATIVE
      }

      render(<EnvControlForm {...props} />)
      const redDot = screen.queryByText('●')
      expect(redDot).toBeNull()
    })
  })

  describe('the form inputs', () => {
    it('should be enabled when shouldCompleteControl', () => {
      const props: EnvControlFormProps = {
        data: undefined,
        shouldCompleteControl: true,
        controlType: ControlType.ADMINISTRATIVE
      }

      render(<EnvControlForm {...props} />)
      const nbInput = screen.getByLabelText('Nb contrôles')
      expect(nbInput).not.toBeDisabled()
      const obsInput = screen.getByLabelText('Observations (hors infraction)')
      expect(obsInput).not.toBeDisabled()
    })
    it('should be enabled when shouldCompleteControl is false but there is data', () => {
      const props: EnvControlFormProps = {
        data: dummy_control,
        shouldCompleteControl: false,
        controlType: ControlType.ADMINISTRATIVE
      }

      render(<EnvControlForm {...props} />)
      const nbInput = screen.getByLabelText('Nb contrôles')
      expect(nbInput).not.toBeDisabled()
      // const obsInput = screen.getByLabelText('Observations (hors infraction)')
      // expect(obsInput).not.toBeDisabled()
    })
  })

  it('should not trigger call if amount of control is more than maxAmountsOfControls', async () => {
    const useControlSpy = vi.spyOn(useControlHook, 'useControl')
    useControlSpy.mockReturnValue({
      isRequired: false,
      controlIsChecked: false,
      updateControl: updateControlMock,
      toggleControl: toogleControlMock,
      controlChanged: vi.fn(),
      controlEnvChanged: controlChangedMock
    })

    const props: EnvControlFormProps = {
      shouldCompleteControl: false,
      controlType: ControlType.ADMINISTRATIVE,
      maxAmountOfControls: 6
    }

    const wrapper = render(<EnvControlForm {...props} />)
    const input = wrapper.getByLabelText('Nb contrôles')
    act(() => {
      fireEvent.change(input, { target: { value: 7 } })
      expect(controlChangedMock).not.toHaveBeenCalled()
    })

    waitFor(() => {
      const error =
        screen.getByText(`Attention, le chiffre renseigné ne peut pas être supérieur au nombre total de contrôles
    effectués`)
      expect(error).toBeInTheDocument()
    })
  })
})
