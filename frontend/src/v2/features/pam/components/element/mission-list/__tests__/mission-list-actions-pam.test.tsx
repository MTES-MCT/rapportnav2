import { describe, it, vi, expect } from 'vitest'
import { render, screen, fireEvent } from '../../../../../../../test-utils.tsx'
import MissionListActionsPam from '../mission-list-actions-pam.tsx'
import { ExportReportType } from '../../../../../common/types/mission-export-types'

const mockMissions = [
  { id: 1, name: 'Mission 1' },
  { id: 2, name: 'Mission 2' }
]

const defaultProps = {
  missions: mockMissions,
  selectedMissionIds: [],
  toggleDialog: vi.fn(),
  toggleAll: vi.fn()
}

describe('MissionListActionsPam', () => {
  it('renders the component correctly', () => {
    render(<MissionListActionsPam {...defaultProps} />)

    expect(screen.getByTitle('Tout sélectionner')).toBeInTheDocument()
    expect(screen.getByTitle('Export tableau AEM')).toBeInTheDocument()
    expect(screen.getByTitle('Export rapport de patrouille')).toBeInTheDocument()
  })

  it('calls toggleAll when the select-all checkbox is clicked', () => {
    render(<MissionListActionsPam {...defaultProps} />)

    const checkbox = screen.getByTitle('Tout sélectionner')
    fireEvent.click(checkbox)

    expect(defaultProps.toggleAll).toHaveBeenCalled()
  })

  it('disables buttons when no missions are selected', () => {
    render(<MissionListActionsPam {...defaultProps} />)

    const aemButton = screen.getByTitle('Export tableau AEM')
    const patrolButton = screen.getByTitle('Export rapport de patrouille')

    expect(aemButton).toBeDisabled()
    expect(patrolButton).toBeDisabled()
  })

  it('enables buttons when missions are selected', () => {
    render(<MissionListActionsPam {...defaultProps} selectedMissionIds={[1]} />)

    const aemButton = screen.getByTitle('Export tableau AEM')
    const patrolButton = screen.getByTitle('Export rapport de patrouille')

    expect(aemButton).not.toBeDisabled()
    expect(patrolButton).not.toBeDisabled()
  })

  it('calls toggleDialog with correct argument when AEM button is clicked', () => {
    render(<MissionListActionsPam {...defaultProps} selectedMissionIds={[1]} />)

    const aemButton = screen.getByTitle('Export tableau AEM')
    fireEvent.click(aemButton)

    expect(defaultProps.toggleDialog).toHaveBeenCalledWith(ExportReportType.AEM)
  })

  it('calls toggleDialog with correct argument when Patrol button is clicked', () => {
    render(<MissionListActionsPam {...defaultProps} selectedMissionIds={[1]} />)

    const patrolButton = screen.getByTitle('Export rapport de patrouille')
    fireEvent.click(patrolButton)

    expect(defaultProps.toggleDialog).toHaveBeenCalledWith(ExportReportType.PATROL)
  })

  it('select-all checkbox is checked when all missions are selected', () => {
    render(<MissionListActionsPam {...defaultProps} selectedMissionIds={[1, 2]} />)

    const checkbox = screen.getByRole('checkbox', { hidden: true })
    expect(checkbox).toBeChecked()
  })

  it('select-all checkbox is unchecked when not all missions are selected', () => {
    render(<MissionListActionsPam {...defaultProps} selectedMissionIds={[1]} />)

    const checkbox = screen.getByRole('checkbox', { hidden: true })
    expect(checkbox).not.toBeChecked()
  })
})
