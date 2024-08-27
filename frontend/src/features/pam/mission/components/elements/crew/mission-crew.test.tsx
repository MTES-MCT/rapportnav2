import { vi } from 'vitest'
import { fireEvent, render, screen } from '../../../../../../test-utils.tsx'
import MissionCrew from './mission-crew.tsx'
import * as useMissionCrewModule from '@features/pam/mission/hooks/use-mission-crew'
import * as useAddModule from '@features/pam/mission/hooks/use-add-update-mission-crew.tsx'
import * as useDeleteModule from '@features/pam/mission/hooks/use-delete-mission-crew'

const mutateMock = vi.fn()
const deleteMock = vi.fn()

const crewMock = {
  id: '123',
  agent: {
    id: 'abc',
    firstName: 'firstName',
    lastName: 'lastName',
    services: []
  },
  comment: undefined,
  role: {
    id: '1',
    title: 'second mecanicien'
  }
}

describe('MissionCrew', () => {
  beforeEach(() => {
    vi.spyOn(useAddModule, 'default').mockReturnValue([mutateMock, { error: undefined }])
    vi.spyOn(useDeleteModule, 'default').mockReturnValue([deleteMock, { error: undefined }])
  })
  it('should render the no crew text', async () => {
    vi.spyOn(useMissionCrewModule, 'default').mockReturnValue({ data: undefined, loading: false, error: null })
    render(<MissionCrew />)
    expect(screen.getByText("Selectionner votre bordée, pour importer votre liste d'équipage")).toBeInTheDocument()
  })

  it('should render the crew when there is', async () => {
    vi.spyOn(useMissionCrewModule, 'default').mockReturnValue({ data: [crewMock], loading: false, error: null })
    const wrapper = render(<MissionCrew />)
    expect(wrapper.getByTestId('crew-member-agent')).toBeDefined()
  })

  it('should render the add button', async () => {
    render(<MissionCrew />)
    expect(screen.getByText('Ajouter un membre d’équipage')).toBeInTheDocument()
  })

  it('should render the modal add member to the crew', async () => {
    render(<MissionCrew />)
    const memberButton = screen.getByTestId('add-crew-member-button')
    fireEvent.click(memberButton)
    expect(screen.getByText('Ajouter un membre d’équipage')).toBeInTheDocument()
  })

  it('should render the modal update member of a crew', async () => {
    render(<MissionCrew />)
    const memberButton = screen.getByTestId('edit-crew-member-icon')
    fireEvent.click(memberButton)
    expect(screen.getByText('Mise à jour d’un membre d’équipage')).toBeInTheDocument()
  })
})
