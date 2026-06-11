import { render, screen } from '../../../../../test-utils.tsx'
import { describe, it, expect } from 'vitest'
import { MissionCrewMemberText } from '../mission-crew-list'
import { Agent } from '../../../common/types/crew-type'

describe('MissionCrewMemberText', () => {
  it('renders agent firstName and lastName', () => {
    const agent: Agent = {
      id: 1,
      firstName: 'Jean',
      lastName: 'Dupont',
      services: []
    }
    render(<MissionCrewMemberText agent={agent} />)
    expect(screen.getByText('Jean Dupont')).toBeInTheDocument()
  })

  it('prioritizes agent over fullName when both are provided', () => {
    const agent: Agent = {
      id: 1,
      firstName: 'Jean',
      lastName: 'Dupont',
      services: []
    }
    render(<MissionCrewMemberText agent={agent} fullName="Other Name" />)
    expect(screen.getByText('Jean Dupont')).toBeInTheDocument()
    expect(screen.queryByText('Other Name')).not.toBeInTheDocument()
  })
})
