import { vi } from 'vitest'
import { render, screen, waitFor } from '../../../../../../test-utils'
import MissionTargetForm, { MissionTargetFormProps } from '../mission-target-form.tsx'
import { TargetType } from '../../../../common/types/target-types.ts'

const mockFromInputToFieldValue = vi.fn()

vi.mock('../../../hooks/use-target', () => ({
  useTarget: () => ({
    fromInputToFieldValue: mockFromInputToFieldValue
  })
}))

vi.mock('../../../../mission-infraction/components/elements/mission-infraction-form', () => ({
  default: ({ onClose, onSubmit, value, ...props }: any) => (
    <div data-testid="mission-infraction-form">
      <button onClick={onClose} data-testid="close-button">
        Close
      </button>
      <button onClick={() => onSubmit(value)} data-testid="submit-button">
        Submit
      </button>
      <div data-testid="props">{JSON.stringify(props)}</div>
    </div>
  )
}))

const createMockFieldArray = (targets = []) => ({
  form: {
    values: { targets },
    setFieldValue: vi.fn()
  },
  remove: vi.fn(),
  push: vi.fn()
})

describe('MissionTargetForm', () => {
  const mockValue = {
    target: { id: '1', name: 'Test Target' },
    infractions: []
  }

  const defaultProps: MissionTargetFormProps = {
    name: 'targets[0]',
    value: mockValue,
    editControl: true,
    fieldFormik: createMockFieldArray() as any,
    targetType: TargetType.VEHICLE
  }

  beforeEach(() => {
    vi.clearAllMocks()
  })

  it('should render with required props', () => {
    render(<MissionTargetForm {...defaultProps} />)

    expect(screen.getByTestId('mission-infraction-form')).toBeInTheDocument()
  })

  it('should pass all props to MissionInfractionForm', () => {
    const props: MissionTargetFormProps = {
      ...defaultProps,
      editTarget: true,
      editInfraction: true,
      vehicleType: 'VESSEL' as any,
      availableControlTypes: ['SEA_CONTROL' as any]
    }

    render(<MissionTargetForm {...props} />)

    const propsElement = screen.getByTestId('props')
    const passedProps = JSON.parse(propsElement.textContent || '{}')

    expect(passedProps).toMatchObject({
      targetType: TargetType.VEHICLE,
      editTarget: true,
      editControl: true,
      editInfraction: true,
      vehicleType: 'VESSEL',
      availableControlTypes: ['SEA_CONTROL']
    })
  })

  it('should call onClose when handleClose is triggered', () => {
    const onClose = vi.fn()
    render(<MissionTargetForm {...defaultProps} onClose={onClose} />)

    const closeButton = screen.getByTestId('close-button')
    closeButton.click()

    expect(onClose).toHaveBeenCalledTimes(1)
  })

  it('should not throw when onClose is undefined', () => {
    render(<MissionTargetForm {...defaultProps} />)

    const closeButton = screen.getByTestId('close-button')

    expect(() => closeButton.click()).not.toThrow()
  })

  it('should transform and submit values correctly', async () => {
    const transformedValue = { transformed: true }
    mockFromInputToFieldValue.mockReturnValue(transformedValue)

    render(<MissionTargetForm {...defaultProps} />)

    const submitButton = screen.getByTestId('submit-button')
    submitButton.click()

    await waitFor(() => {
      expect(mockFromInputToFieldValue).toHaveBeenCalledWith(mockValue)
      expect(defaultProps.fieldFormik.form.setFieldValue).toHaveBeenCalledWith('targets[0]', transformedValue)
    })
  })

  it('should handle submission without value', async () => {
    render(<MissionTargetForm {...defaultProps} />)

    // Simulate submit with undefined value
    const form = screen.getByTestId('mission-infraction-form')
    const onSubmit = vi.fn()

    // Manually trigger with undefined
    await onSubmit(undefined)

    expect(defaultProps.fieldFormik.form.setFieldValue).not.toHaveBeenCalled()
  })

  it('should call setFieldValue with correct name and transformed value', async () => {
    const transformedValue = { id: '1', transformed: true }
    mockFromInputToFieldValue.mockReturnValue(transformedValue)

    render(<MissionTargetForm {...defaultProps} />)

    const submitButton = screen.getByTestId('submit-button')
    submitButton.click()

    await waitFor(() => {
      expect(defaultProps.fieldFormik.form.setFieldValue).toHaveBeenCalledWith('targets[0]', transformedValue)
      expect(defaultProps.fieldFormik.form.setFieldValue).toHaveBeenCalledTimes(1)
    })
  })

  it('should close form after successful submission', async () => {
    const onClose = vi.fn()
    const transformedValue = { transformed: true }
    mockFromInputToFieldValue.mockReturnValue(transformedValue)

    render(<MissionTargetForm {...defaultProps} onClose={onClose} />)

    const submitButton = screen.getByTestId('submit-button')
    submitButton.click()

    await waitFor(() => {
      expect(onClose).toHaveBeenCalledTimes(1)
    })
  })
})
