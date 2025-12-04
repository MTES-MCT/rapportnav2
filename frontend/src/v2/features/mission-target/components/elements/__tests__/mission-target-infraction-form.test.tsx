import { beforeEach, describe, expect, it, vi } from 'vitest'
import { fireEvent, render, screen } from '../../../../../../test-utils'
import MissionTargetInfractionForm from '../mission-target-infraction-form'
import { TargetInfraction } from '../../../../common/types/target-types'
import { ControlType } from '@common/types/control-types.ts'
import { InfractionTypeEnum } from '@common/types/env-mission-types.ts'

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

describe('MissionTargetInfractionForm', () => {
  beforeEach(() => {
    vi.clearAllMocks()
    capturedOnSubmit = undefined
  })

  it('renders summary initially', () => {
    render(
      <MissionTargetInfractionForm index={0} value={sampleValue} onDelete={mockOnDelete} onSubmit={mockOnSubmit} />
    )
    expect(screen.getByTestId('mission-infraction-summary')).toBeInTheDocument()
    expect(screen.queryByTestId('mission-infraction-form')).not.toBeInTheDocument()
  })

  it('shows form when edit button is clicked', () => {
    render(
      <MissionTargetInfractionForm index={0} value={sampleValue} onDelete={mockOnDelete} onSubmit={mockOnSubmit} />
    )
    const editButton = screen.getByRole('edit-target')
    fireEvent.click(editButton)
    expect(screen.getByTestId('mission-infraction-form')).toBeInTheDocument()
  })

  it('calls onDelete with index when delete button is clicked', () => {
    render(
      <MissionTargetInfractionForm index={5} value={sampleValue} onDelete={mockOnDelete} onSubmit={mockOnSubmit} />
    )
    const deleteButton = screen.getByRole('delete-target')
    fireEvent.click(deleteButton)
    expect(mockOnDelete).toHaveBeenCalledWith(5)
  })

  it('calls onSubmit and closes form', () => {
    render(
      <MissionTargetInfractionForm index={0} value={sampleValue} onDelete={mockOnDelete} onSubmit={mockOnSubmit} />
    )
    // open form
    fireEvent.click(screen.getByRole('edit-target'))
    expect(screen.getByTestId('mission-infraction-form')).toBeInTheDocument()

    // call captured onSubmit
    capturedOnSubmit!(sampleValue)
    expect(mockOnSubmit).toHaveBeenCalledWith(sampleValue)
  })

  it('closes form if onSubmit is called with undefined', () => {
    render(
      <MissionTargetInfractionForm index={0} value={sampleValue} onDelete={mockOnDelete} onSubmit={mockOnSubmit} />
    )
    fireEvent.click(screen.getByRole('edit-target'))
    expect(screen.getByTestId('mission-infraction-form')).toBeInTheDocument()

    capturedOnSubmit!(undefined)
    expect(mockOnSubmit).toHaveBeenCalledWith(undefined)
  })
})
