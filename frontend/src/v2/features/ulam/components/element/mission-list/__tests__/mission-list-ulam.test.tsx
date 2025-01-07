import { render, screen } from '../../../../../../../test-utils.tsx'
import MissionListUlam from '../mission-list-ulam.tsx'
import { expect } from 'vitest'
import { Mission } from '@common/types/mission-types.ts'

const mockMissions: Mission[] = [
  { id: 1, name: 'Mission 1', startDateTimeUtc: '2023-12-01T10:00:00Z' },
  { id: 2, name: 'Mission 2', startDateTimeUtc: '2023-12-05T10:00:00Z' }
]
describe('MissionListUlam component', () => {
  it('should render empty mission message', () => {
    render(<MissionListUlam missions={[]} />)
    expect(screen.getByText('Aucune mission pour cette période de temps')).toBeInTheDocument()
  })
  it('should render empty mission message', () => {
    render(<MissionListUlam missions={mockMissions} />)
    expect(screen.getByText('Mission #2023-12-01')).toBeInTheDocument()
    expect(screen.getByText('Mission #2023-12-05')).toBeInTheDocument()
  })
})
