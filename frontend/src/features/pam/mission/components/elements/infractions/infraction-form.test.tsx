import { fireEvent, render, screen } from '../../../../../../test-utils.tsx'
import InfractionForm, { InfractionFormData } from './infraction-form.tsx'
import { InfractionTypeEnum } from '@common/types/env-mission-types.ts'
import { vi } from 'vitest'
import { ControlType } from '@common/types/control-types.ts'

const emptyInfractionProps = {}
const someInfractionProps = (props: InfractionFormData) => ({
  controlType: props.controlType,
  infractionType: props.infractionType,
  natinfs: props.natinfs,
  observations: props.observations
})

describe('InfractionForm', () => {
  test('renders InfractionForm component', () => {
    render(<InfractionForm onChange={vi.fn()} onCancel={vi.fn()} infraction={emptyInfractionProps} />)

    // Add assertions based on your UI, e.g., check if certain elements are present
    expect(screen.getByRole('validate-infraction')).toBeInTheDocument()
  })

  describe('Toggle infractionType', () => {
    test('should equal without_report in default state', () => {
      render(<InfractionForm onChange={vi.fn()} onCancel={vi.fn()} infraction={emptyInfractionProps} />)
      const toggle = screen.getByRole('switch')
      expect(toggle).toHaveProperty('value', InfractionTypeEnum.WITHOUT_REPORT)
      expect(toggle).toHaveProperty('checked', false)
    })
    test('should have the value from props when given', () => {
      render(
        <InfractionForm
          onChange={vi.fn()}
          onCancel={vi.fn()}
          infraction={someInfractionProps({ infractionType: InfractionTypeEnum.WITH_REPORT })}
        />
      )
      const toggle = screen.getByRole('switch')
      expect(toggle).toHaveProperty('checked', true)
    })
    test('handles infractionType toggle correctly - case enabling', () => {
      const onChangeMock = vi.fn()
      render(<InfractionForm onChange={onChangeMock} onCancel={vi.fn()} />)

      const toggle = screen.getByRole('switch')
      fireEvent.click(toggle)
      expect(onChangeMock).toHaveBeenCalledWith('infractionType', InfractionTypeEnum.WITH_REPORT)
    })
    test('handles infractionType toggle correctly - case disabling', () => {
      const onChangeMock = vi.fn()
      const infraction = someInfractionProps({
        infractionType: InfractionTypeEnum.WITH_REPORT
      })
      render(<InfractionForm onChange={onChangeMock} onCancel={vi.fn()} infraction={infraction} />)

      const toggle = screen.getByRole('switch')
      fireEvent.click(toggle)
      expect(onChangeMock).toHaveBeenCalledWith('infractionType', InfractionTypeEnum.WITHOUT_REPORT)
    })
  })

  describe('Button validate', () => {
    test('should be disabled by default (no controlType, no natinfs)', () => {
      render(<InfractionForm onChange={vi.fn()} onCancel={vi.fn()} infraction={emptyInfractionProps} />)
      const button = screen.getByRole('validate-infraction')
      expect(button).toBeDisabled()
    })
    test('should be disabled when no controlType but some natinfs', () => {
      const infraction = someInfractionProps({
        natinfs: ['123']
      })
      render(<InfractionForm onChange={vi.fn()} onCancel={vi.fn()} infraction={infraction} />)
      const button = screen.getByRole('validate-infraction')
      expect(button).toBeDisabled()
    })
    test('should be disabled when some controlType but no natinfs', () => {
      const infraction = someInfractionProps({
        controlType: ControlType.ADMINISTRATIVE
      })
      render(<InfractionForm onChange={vi.fn()} onCancel={vi.fn()} infraction={infraction} />)
      const button = screen.getByRole('validate-infraction')
      expect(button).toBeDisabled()
    })
    test('should be enabled when some controlType and some natinfs', () => {
      const infraction = someInfractionProps({
        natinfs: ['123'],
        controlType: ControlType.ADMINISTRATIVE
      })
      render(<InfractionForm onChange={vi.fn()} onCancel={vi.fn()} infraction={infraction} />)
      const button = screen.getByRole('validate-infraction')
      expect(button).toBeEnabled()
    })
  })

  describe('Textarea observation', () => {
    test('changes are not reported if no blur', async () => {
      const onChangeMock = vi.fn()
      render(<InfractionForm onChange={onChangeMock} onCancel={vi.fn()} />)

      const text = 'some text'
      const textarea = screen.getByRole('observations')
      fireEvent.change(textarea, { target: { value: text } })
      expect(onChangeMock).toHaveBeenCalledWith('observations', text)
    })
  })
})
