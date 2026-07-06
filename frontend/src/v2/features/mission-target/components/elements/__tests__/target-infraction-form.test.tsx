import { ControlType } from '@common/types/control-types.ts'
import { InfractionTypeEnum } from '@common/types/env-mission-types.ts'
import { beforeEach, describe, expect, it, vi } from 'vitest'
import { fireEvent, render, screen } from '../../../../../../test-utils'
import { TargetInfraction } from '../../../../common/types/target-types'
import TargetInfractionForm from '../target-infraction-form'

// Mock MissionInfractionForm
let capturedOnSubmit: ((value?: TargetInfraction) => void) | undefined
vi.mock('../../../../mission-infraction/components/elements/mission-infraction-form', () => ({
  default: (props: any) => {
    capturedOnSubmit = props.onSubmit
    return <div data-testid="mission-infraction-form" />
  }
}))

const mockOnDelete = vi.fn()
const mockOnSubmit = vi.fn()

const sampleValue: TargetInfraction = {
  id: '1',
  name: 'Infraction 1',
  infraction: { natinfs: ['A'], infractionType: InfractionTypeEnum.WITH_REPORT },
  control: { controlType: ControlType.ADMINISTRATIVE }
}

describe('TargetInfractionForm', () => {
  beforeEach(() => {
    vi.clearAllMocks()
    capturedOnSubmit = undefined
  })

  it('renders summary initially', () => {
    render(<TargetInfractionForm index={0} value={sampleValue} onDelete={mockOnDelete} onSubmit={mockOnSubmit} />)
    expect(screen.getByTestId('infraction-summary')).toBeInTheDocument()
    expect(screen.queryByTestId('mission-infraction-form')).not.toBeInTheDocument()
  })

  it('shows form when edit button is clicked', () => {
    render(<TargetInfractionForm index={0} value={sampleValue} onDelete={mockOnDelete} onSubmit={mockOnSubmit} />)
    const editButton = screen.getByRole('edit-infraction')
    fireEvent.click(editButton)
    expect(screen.getByTestId('mission-infraction-form')).toBeInTheDocument()
  })

  it('calls onSubmit and closes form', () => {
    render(<TargetInfractionForm index={0} value={sampleValue} onDelete={mockOnDelete} onSubmit={mockOnSubmit} />)
    // open form
    fireEvent.click(screen.getByRole('edit-infraction'))
    expect(screen.getByTestId('mission-infraction-form')).toBeInTheDocument()

    // call captured onSubmit
    capturedOnSubmit!(sampleValue)
    expect(mockOnSubmit).toHaveBeenCalledWith(sampleValue)
  })

  it('calls onDelete when delete button is clicked', () => {
    render(<TargetInfractionForm index={0} value={sampleValue} onDelete={mockOnDelete} onSubmit={mockOnSubmit} />)
    const deleteButton = screen.getByRole('delete-infraction')
    fireEvent.click(deleteButton)
    expect(mockOnDelete).toHaveBeenCalledTimes(1)
  })

  it('closes form if onSubmit is called with undefined', () => {
    render(<TargetInfractionForm index={0} value={sampleValue} onDelete={mockOnDelete} onSubmit={mockOnSubmit} />)
    fireEvent.click(screen.getByRole('edit-infraction'))
    expect(screen.getByTestId('mission-infraction-form')).toBeInTheDocument()

    capturedOnSubmit!(undefined)
    expect(mockOnSubmit).toHaveBeenCalledWith(undefined)
  })
})
