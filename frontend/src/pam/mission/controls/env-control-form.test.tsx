import { getByRole, render, screen } from '../../../test-utils'
import { ControlAdministrative, ControlType } from '../../../types/control-types'
import EnvControlForm, { EnvControlFormProps } from './env-control-form'

const dummy_control = {
  id: '123',
  amountOfControls: 4
} as ControlAdministrative

describe('EnvControlForm', () => {
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
    it('should be disabled when shouldCompleteControl is false but there is no data', () => {
      const props: EnvControlFormProps = {
        data: undefined,
        shouldCompleteControl: false,
        controlType: ControlType.ADMINISTRATIVE
      }

      render(<EnvControlForm {...props} />)
      const nbInput = screen.getByLabelText('Nb contrôles')
      expect(nbInput).toBeDisabled()
      const obsInput = screen.getByLabelText('Observations (hors infraction)')
      expect(obsInput).toBeDisabled()
    })
  })
})
