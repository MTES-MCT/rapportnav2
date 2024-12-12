import { describe, it, expect } from 'vitest'
import { render, screen } from '../../../../../../../test-utils.tsx'
import MissionListHeaderPam from '../mission-list-header-pam.tsx'

describe('MissionListHeaderPam', () => {
  it('renders the component correctly', () => {
    render(<MissionListHeaderPam />)

    expect(screen.getByText('Date de début')).toBeInTheDocument()
    expect(screen.getByText('Date de fin')).toBeInTheDocument()
    expect(screen.getByText('Bordée')).toBeInTheDocument()
    expect(screen.getByText('Statut mission')).toBeInTheDocument()
    expect(screen.getByText('Statut du rapport')).toBeInTheDocument()
  })
})
