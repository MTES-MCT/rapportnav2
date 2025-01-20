import { getMonthName } from '@common/utils/dates-for-humans'
import { describe, expect, it, vi } from 'vitest'
import { fireEvent, render, screen } from '../../../../../../../test-utils.tsx'
import { MissionListItem } from '../../../../../../features/common/types/mission-types.ts'
import MissionListPam from '../mission-list-pam.tsx'

const mockMissions: MissionListItem[] = [
  { id: 1, missionNamePam: 'Mission #2023-12-01', startDateTimeUtc: '2023-12-01T10:00:00Z' },
  { id: 2, missionNamePam: 'Mission #2023-11-15', startDateTimeUtc: '2023-11-15T14:00:00Z' },
  { id: 3, missionNamePam: 'Mission #2023-12-05', startDateTimeUtc: '2023-12-05T08:00:00Z' }
]

const defaultProps = {
  missions: mockMissions,
  selectedMissionIds: [],
  toggleOne: vi.fn()
}

describe('MissionListPam', () => {
  it('renders the component correctly', () => {
    render(<MissionListPam {...defaultProps} />)

    expect(screen.getByText('Mission #2023-12-01')).toBeInTheDocument()
    expect(screen.getByText('Mission #2023-11-15')).toBeInTheDocument()
    expect(screen.getByText('Mission #2023-12-05')).toBeInTheDocument()
  })

  it('groups missions by month and displays month headers', () => {
    render(<MissionListPam {...defaultProps} />)

    const decemberHeader = screen.getByText(getMonthName('2023-12'))
    const novemberHeader = screen.getByText(getMonthName('2023-11'))

    expect(decemberHeader).toBeInTheDocument()
    expect(novemberHeader).toBeInTheDocument()
  })

  it('renders the correct number of missions under each month header', () => {
    render(<MissionListPam {...defaultProps} />)

    const decemberHeader = screen.getByText(getMonthName('2023-12'))
    const decemberMissions = screen.getAllByText(/Mission #2023-12/)

    expect(decemberHeader).toBeInTheDocument()
    expect(decemberMissions).toHaveLength(2)

    const novemberHeader = screen.getByText(getMonthName('2023-11'))
    const novemberMissions = screen.getAllByText('Mission #2023-11-15')

    expect(novemberHeader).toBeInTheDocument()
    expect(novemberMissions).toHaveLength(1)
  })

  it('calls toggleOne with the correct mission ID when a mission is toggled', () => {
    render(<MissionListPam {...defaultProps} />)

    const missionToggle = screen.getAllByTitle('Sélectionner cette mission')[0]
    fireEvent.click(missionToggle)

    expect(defaultProps.toggleOne).toHaveBeenCalledWith(1, true)
  })

  it('marks missions as selected based on selectedMissionIds', () => {
    render(<MissionListPam {...defaultProps} selectedMissionIds={[1]} />)

    const checkboxes = screen.getAllByRole('checkbox', { hidden: true })

    expect(checkboxes[0]).toBeChecked()
    expect(checkboxes[1]).not.toBeChecked()
  })

  it('renders an empty state when no missions are provided', () => {
    render(<MissionListPam {...defaultProps} missions={[]} />)

    expect(screen.getByText('Aucune mission pour cette période de temps')).toBeInTheDocument()
  })
})
