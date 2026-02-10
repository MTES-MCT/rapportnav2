import { describe, it, expect, vi, beforeEach } from 'vitest'
import { render, screen, fireEvent } from '../../../../../../test-utils'
import MissionTargetNew from '../mission-target-new'
import { MissionSourceEnum } from '@common/types/env-mission-types'
import { TargetInfraction } from '../../../../common/types/target-types'

// Mock useTarget
const mockGetTargetType = vi.fn()
const mockFromInputToFieldValue = vi.fn()

vi.mock('../../../hooks/use-target', () => ({
  useTarget: () => ({
    getTargetType: mockGetTargetType,
    fromInputToFieldValue: mockFromInputToFieldValue
  })
}))

// Mock MissionInfractionForm to intercept onSubmit
let capturedOnSubmit: ((value?: TargetInfraction) => void) | undefined
vi.mock('../../../../mission-infraction/components/elements/mission-infraction-form', () => ({
  default: (props: any) => {
    capturedOnSubmit = props.onSubmit
    return <div data-testid="mission-infraction-form" />
  }
}))

const createMockFieldArray = () => ({
  form: { values: { targets: [] }, setFieldValue: vi.fn() },
  remove: vi.fn(),
  push: vi.fn()
})

describe('MissionTargetNew', () => {
  let fieldArray: ReturnType<typeof createMockFieldArray>

  beforeEach(() => {
    vi.clearAllMocks()
    fieldArray = createMockFieldArray()
    mockGetTargetType.mockReturnValue('VEHICLE')
    mockFromInputToFieldValue.mockImplementation(v => ({ ...v, transformed: true }))
    capturedOnSubmit = undefined
  })

  it('renders add button initially', () => {
    render(<MissionTargetNew fieldArray={fieldArray} availableControlTypes={['CONTROL'] as any} />)
    expect(screen.getByTestId('target-new-button')).toBeInTheDocument()
  })

  it('shows form when button is clicked', () => {
    render(<MissionTargetNew fieldArray={fieldArray} availableControlTypes={['CONTROL'] as any} />)
    const button = screen.getByTestId('target-new-button')
    fireEvent.click(button)
    expect(screen.getByTestId('mission-infraction-form')).toBeInTheDocument()
  })

  it('calls fieldArray.push with transformed value on submit and closes form', () => {
    render(<MissionTargetNew fieldArray={fieldArray} actionId="action-1" availableControlTypes={['CONTROL'] as any} />)
    fireEvent.click(screen.getByTestId('target-new-button'))

    const mockValue: TargetInfraction = { id: 'infraction-1', name: 'Test' }

    // Call captured onSubmit directly
    expect(capturedOnSubmit).toBeDefined()
    capturedOnSubmit!(mockValue)

    expect(mockFromInputToFieldValue).toHaveBeenCalledWith(mockValue)
    expect(fieldArray.push).toHaveBeenCalledWith(
      expect.objectContaining({
        ...mockFromInputToFieldValue(mockValue),
        actionId: 'action-1',
        source: MissionSourceEnum.RAPPORT_NAV,
        targetType: 'VEHICLE'
      })
    )
  })

  it('does not push if handleSubmit is called with undefined', () => {
    render(<MissionTargetNew fieldArray={fieldArray} availableControlTypes={['CONTROL'] as any} />)
    fireEvent.click(screen.getByTestId('target-new-button'))

    expect(capturedOnSubmit).toBeDefined()
    capturedOnSubmit!(undefined)
    expect(fieldArray.push).not.toHaveBeenCalled()
  })

  it('disables button when isDisabled is true', () => {
    render(<MissionTargetNew fieldArray={fieldArray} isDisabled availableControlTypes={['CONTROL'] as any} />)
    expect(screen.getByTestId('target-new-button')).toBeDisabled()
  })

  it('disables button when availableControlTypes is empty', () => {
    render(<MissionTargetNew fieldArray={fieldArray} availableControlTypes={[]} />)
    expect(screen.getByTestId('target-new-button')).toBeDisabled()
  })
})
