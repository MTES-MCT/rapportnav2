import { MissionSourceEnum } from '@common/types/env-mission-types.ts'
import { CompletenessForStatsStatusEnum, Mission } from '@common/types/mission-types.ts'
import { MissionListItem } from 'src/v2/features/common/types/mission-types.ts'
import { fireEvent, render, screen } from '../../../../../../../test-utils.tsx'
import MissionListItemPam from '../mission-list-item-pam.tsx'
import { describe } from 'vitest'

describe('MissionListItemPam', () => {
  it.skip('renders empty mission info', () => {
    const mockMission: Mission = {}
    const mockOnToggle = vi.fn()
    render(<MissionListItemPam mission={mockMission} isSelected={false} onToggle={mockOnToggle} />)

    //expect(screen.getByTestId('mission-list-item-mission_number')).toHaveTextContent('Mission #--/--/----')
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
      id: 1,
      startDateTimeUtc: '2024-01-01T00:00:00Z',
      endDateTimeUtc: '2024-01-12T01:00:00Z',
      startDateTimeUtcText: '01/01/2024',
      endDateTimeUtcText: '12/01/2024',
      crewNumber: 'B',
      missionNamePam: 'Mission #2024-01-01',
      missionSource: MissionSourceEnum.MONITORENV,
      completenessForStats: {
        status: CompletenessForStatsStatusEnum.COMPLETE
      },
      generalInfo: {
        serviceId: 2
      }
    } as MissionListItem
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

  describe('the inter-services tag', () => {
    it('should not render the inter-services tag when less than two controlUnits', () => {
      const mockMission = { controlUnits: [{}] } as MissionListItem
      render(<MissionListItemPam mission={mockMission} isSelected={false} onToggle={vi.fn()} />)
      expect(screen.queryByText('Inter-services')).toBeNull()
    })
    it('should render the inter-services tag when more than one controlUnit', () => {
      const mockMission = { controlUnits: [{}, {}] } as MissionListItem
      render(<MissionListItemPam mission={mockMission} isSelected={false} onToggle={vi.fn()} />)
      expect(screen.getByText('Inter-services')).toBeInTheDocument()
    })
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
