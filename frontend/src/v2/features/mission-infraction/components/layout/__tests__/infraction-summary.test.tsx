import { fireEvent } from '@testing-library/react'
import { vi } from 'vitest'
import { render, screen } from '../../../../../../test-utils.tsx'
import InfractionSummary from '../infraction-summary.tsx'

const defaultProps = {
  index: 0,
  tags: <span>tag-content</span>
}

describe('InfractionSummary', () => {
  it('renders the component with testid', () => {
    render(<InfractionSummary {...defaultProps} />)
    expect(screen.getByTestId('infraction-summary')).toBeInTheDocument()
  })

  it('renders the title when provided', () => {
    render(<InfractionSummary {...defaultProps} title="Mon infraction" />)
    expect(screen.getByText('Mon infraction')).toBeInTheDocument()
  })

  it('renders the tags', () => {
    render(<InfractionSummary {...defaultProps} />)
    expect(screen.getByText('tag-content')).toBeInTheDocument()
  })

  it('renders observations when provided', () => {
    render(<InfractionSummary {...defaultProps} observations="Observation de test" />)
    expect(screen.getByText('Observation de test')).toBeInTheDocument()
  })

  it('renders fallback text when observations is undefined', () => {
    render(<InfractionSummary {...defaultProps} />)
    expect(screen.getByText('Aucune observation')).toBeInTheDocument()
  })

  it('renders footerTag when provided', () => {
    render(<InfractionSummary {...defaultProps} footerTag={<span>footer-tag</span>} />)
    expect(screen.getByText('footer-tag')).toBeInTheDocument()
  })

  it('renders edit button when onEdit is provided', () => {
    render(<InfractionSummary {...defaultProps} onEdit={vi.fn()} />)
    expect(screen.getByRole('edit-infraction')).toBeInTheDocument()
  })

  it('does not render edit button when onEdit is not provided', () => {
    render(<InfractionSummary {...defaultProps} />)
    expect(screen.queryByRole('edit-infraction')).not.toBeInTheDocument()
  })

  it('renders delete button when onDelete is provided', () => {
    render(<InfractionSummary {...defaultProps} onDelete={vi.fn()} />)
    expect(screen.getByRole('delete-infraction')).toBeInTheDocument()
  })

  it('does not render delete button when onDelete is not provided', () => {
    render(<InfractionSummary {...defaultProps} />)
    expect(screen.queryByRole('delete-infraction')).not.toBeInTheDocument()
  })

  it('calls onEdit when edit button is clicked', () => {
    const onEdit = vi.fn()
    render(<InfractionSummary {...defaultProps} onEdit={onEdit} />)
    fireEvent.click(screen.getByRole('edit-infraction'))
    expect(onEdit).toHaveBeenCalledTimes(1)
  })

  it('calls onDelete when delete button is clicked', () => {
    const onDelete = vi.fn()
    render(<InfractionSummary {...defaultProps} onDelete={onDelete} />)
    fireEvent.click(screen.getByRole('delete-infraction'))
    expect(onDelete).toHaveBeenCalledTimes(1)
  })

  it('disables edit button when isActionDisabled is true', () => {
    render(<InfractionSummary {...defaultProps} onEdit={vi.fn()} isActionDisabled={true} />)
    expect(screen.getByRole('edit-infraction')).toBeDisabled()
  })

  it('disables delete button when isActionDisabled is true', () => {
    render(<InfractionSummary {...defaultProps} onDelete={vi.fn()} isActionDisabled={true} />)
    expect(screen.getByRole('delete-infraction')).toBeDisabled()
  })
})
