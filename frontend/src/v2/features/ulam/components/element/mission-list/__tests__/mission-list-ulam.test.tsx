import { MissionListItem } from 'src/v2/features/common/types/mission-types.ts'
import { expect } from 'vitest'
import { render, screen } from '../../../../../../../test-utils.tsx'
import MissionListUlam from '../mission-list-ulam.tsx'

const mockMissions: MissionListItem[] = [
  { id: 1, missionNameUlam: 'Mission #2023-12', startDateTimeUtc: '2023-12-01T10:00:00Z' },
  { id: 2, missionNameUlam: 'Mission #2023-12', startDateTimeUtc: '2023-12-05T10:00:00Z' }
]
describe('MissionListUlam component', () => {
  it('should render empty mission message', () => {
    render(<MissionListUlam missions={[]} />)
    expect(screen.getByText('Aucune mission pour cette période de temps')).toBeInTheDocument()
  })
  it('should render empty mission message', () => {
    render(<MissionListUlam missions={mockMissions} />)
    expect(screen.getByText('Mission #2023-12/1')).toBeInTheDocument()
    expect(screen.getByText('Mission #2023-12/2')).toBeInTheDocument()
  })
})
