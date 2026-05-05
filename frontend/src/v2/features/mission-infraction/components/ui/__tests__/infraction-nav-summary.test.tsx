import { ControlType } from '@common/types/control-types'
import { InfractionTypeEnum } from '@common/types/env-mission-types'
import { fireEvent } from '@testing-library/react'
import { vi } from 'vitest'
import { render, screen } from '../../../../../../test-utils.tsx'
import InfractionNavSummary from '../infraction-nav-summary.tsx'

const defaultProps = {
  index: 0,
  onEdit: vi.fn(),
  onDelete: vi.fn()
}

describe('InfractionNavSummary', () => {
  it('renders default title when no controlType is provided', () => {
    render(<InfractionNavSummary {...defaultProps} />)
    expect(screen.getByText(/Infraction contrôle de l.environnement/)).toBeInTheDocument()
  })

  it('renders the control-type-specific title when controlType is provided', () => {
    render(<InfractionNavSummary {...defaultProps} controlType={ControlType.ADMINISTRATIVE} />)
    expect(screen.getByText('Infrac. admin navire')).toBeInTheDocument()
  })

  it('renders observations from infraction', () => {
    render(
      <InfractionNavSummary
        {...defaultProps}
        infraction={{ observations: 'Pêche illégale constatée' }}
      />
    )
    expect(screen.getByText('Pêche illégale constatée')).toBeInTheDocument()
  })

  it('renders fallback text when observations is undefined', () => {
    render(<InfractionNavSummary {...defaultProps} infraction={{}} />)
    expect(screen.getByText('Aucune observation')).toBeInTheDocument()
  })

  it('renders edit and delete buttons', () => {
    render(<InfractionNavSummary {...defaultProps} />)
    expect(screen.getByRole('edit-infraction')).toBeInTheDocument()
    expect(screen.getByRole('delete-infraction')).toBeInTheDocument()
  })

  it('calls onEdit when edit button is clicked', () => {
    const onEdit = vi.fn()
    render(<InfractionNavSummary {...defaultProps} onEdit={onEdit} />)
    fireEvent.click(screen.getByRole('edit-infraction'))
    expect(onEdit).toHaveBeenCalledTimes(1)
  })

  it('calls onDelete when delete button is clicked', () => {
    const onDelete = vi.fn()
    render(<InfractionNavSummary {...defaultProps} onDelete={onDelete} />)
    fireEvent.click(screen.getByRole('delete-infraction'))
    expect(onDelete).toHaveBeenCalledTimes(1)
  })

  it('renders natinfs tag when infraction has natinfs', () => {
    render(
      <InfractionNavSummary
        {...defaultProps}
        infraction={{ natinfs: ['22206', '22207'], infractionType: InfractionTypeEnum.WITH_REPORT }}
      />
    )
    expect(screen.getByText(/22206/)).toBeInTheDocument()
  })

  it('renders NAVIGATION control type title', () => {
    render(<InfractionNavSummary {...defaultProps} controlType={ControlType.NAVIGATION} />)
    expect(screen.getByText('Infrac.règles de nav')).toBeInTheDocument()
  })
})
