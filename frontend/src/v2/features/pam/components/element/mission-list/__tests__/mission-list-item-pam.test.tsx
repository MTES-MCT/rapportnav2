import MissionListItemPam from '../mission-list-item-pam.tsx'
import { render, screen, fireEvent } from '../../../../../../../test-utils.tsx'
import { CompletenessForStatsStatusEnum, Mission } from '@common/types/mission-types.ts'
import { MissionSourceEnum } from '@common/types/env-mission-types.ts'

describe('MissionListItemPam', () => {
  it('renders empty mission info', () => {
    const mockMission: Mission = {}
    const mockOnToggle = vi.fn()
    render(<MissionListItemPam mission={mockMission} isSelected={false} onToggle={mockOnToggle} />)

    expect(screen.getByTestId('mission-list-item-mission_number')).toHaveTextContent('Mission #--/--/----')
    expect(screen.getByTestId('mission-list-item-open_by')).toHaveTextContent('Ouverte par N/A')
    expect(screen.getByTestId('mission-list-item-start_date')).toHaveTextContent('--/--/----')
    expect(screen.getByTestId('mission-list-item-end_date')).toHaveTextContent('--/--/----')
    expect(screen.getByTestId('mission-list-item-mission_status')).toHaveTextContent('Indisponible')
    expect(screen.getByTestId('mission-list-item-mission_completeness')).toHaveTextContent('À compléter')
    expect(screen.getByTestId('mission-list-item-crew')).toHaveTextContent('--')
    expect(screen.getByTestId('mission-list-item-icon-edit')).toBeInTheDocument()
  })

  it('renders all data correctly', () => {
    const mockMission = {
      startDateTimeUtc: '2024-01-01T00:00:00Z',
      endDateTimeUtc: '2024-01-12T01:00:00Z',
      missionSource: MissionSourceEnum.MONITORENV,
      completenessForStats: {
        status: CompletenessForStatsStatusEnum.COMPLETE
      },
      generalInfo: {
        serviceId: 2
      }
    } as Mission
    const mockOnToggle = vi.fn()
    render(<MissionListItemPam mission={mockMission} isSelected={false} onToggle={mockOnToggle} />)

    expect(screen.getByTestId('mission-list-item-mission_number')).toHaveTextContent('2024-01-01')
    expect(screen.getByTestId('mission-list-item-open_by')).toHaveTextContent('Ouverte par le CACEM')
    expect(screen.getByTestId('mission-list-item-start_date')).toHaveTextContent('01/01/2024')
    expect(screen.getByTestId('mission-list-item-end_date')).toHaveTextContent('12/01/2024')
    expect(screen.getByTestId('mission-list-item-mission_status')).toHaveTextContent('Indisponible')
    expect(screen.getByTestId('mission-list-item-mission_completeness')).toHaveTextContent('Données à jour')
    expect(screen.getByTestId('mission-list-item-crew')).toHaveTextContent('B')
    expect(screen.getByTestId('mission-list-item-icon-edit')).toBeInTheDocument()
  })

  it('have the checkbox selected when prop isSelected is true', () => {
    const mockMission: Mission = { id: 1 }
    const mockOnToggle = vi.fn()
    render(<MissionListItemPam mission={mockMission} isSelected={true} onToggle={mockOnToggle} />)

    const checkbox = screen.getByRole('checkbox')
    expect(checkbox).toBeChecked()
  })

  it('triggers onToggle when checkbox is clicked', () => {
    const mockMission: Mission = { id: 1 }
    const mockOnToggle = vi.fn()
    render(<MissionListItemPam mission={mockMission} isSelected={false} onToggle={mockOnToggle} />)

    const checkbox = screen.getByRole('checkbox')
    fireEvent.click(checkbox)
    expect(mockOnToggle).toHaveBeenCalledWith(mockMission.id, true)
    expect(checkbox).not.toBeChecked()
  })
})
