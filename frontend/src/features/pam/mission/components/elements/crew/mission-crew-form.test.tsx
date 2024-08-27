import { vi } from 'vitest'
import { fireEvent, render, screen, waitFor } from '../../../../../../test-utils.tsx'
import MissionCrewForm from './mission-crew-form.tsx'
import * as useAgentRolesModule from '../../../hooks/use-agent-roles.tsx'

const handleClose = vi.fn()
const handleSubmitForm = vi.fn()

const crewList = [
  {
    id: '1',
    agent: {
      id: 'agent1',
      firstName: 'Ivan',
      lastName: 'Lapierre',
      services: []
    },
    comment: 'My very very long comment',
    role: {
      id: '2',
      title: 'Agent de pont'
    }
  },
  {
    id: '2',
    agent: {
      id: 'agent2',
      firstName: 'Joseph',
      lastName: 'Dupont',
      services: []
    },
    comment: undefined,
    role: {
      id: '1',
      title: 'second mecanicien'
    }
  }
]

const agentRoles = [
  {
    id: '1',
    title: 'second mecanicien'
  },
  { id: '2', title: 'Agent de pont' }
]

describe('MissionCrewForm', () => {
  beforeEach(() => {
    vi.spyOn(useAgentRolesModule, 'default').mockReturnValue({ data: agentRoles, loading: false, error: null })
  })

  it('should render ajout for adding new member', async () => {
    render(<MissionCrewForm crewList={crewList} handleClose={handleClose} handleSubmitForm={handleSubmitForm} />)
    expect(screen.getByText('Ajouter un membre')).toBeInTheDocument()
    expect(screen.getByText('Ajout d’un membre d’équipage du DCS')).toBeInTheDocument()
  })

  it('should render update for modifying a member', async () => {
    render(
      <MissionCrewForm crewId={'1'} crewList={crewList} handleClose={handleClose} handleSubmitForm={handleSubmitForm} />
    )
    expect(screen.getByText('Mettre à jour un membre')).toBeInTheDocument()
    expect(screen.getByText('Mise à jour d’un membre d’équipage')).toBeInTheDocument()
  })

  it('should render close icon and trigger handle close method', async () => {
    const closeIcon = render(
      <MissionCrewForm crewList={crewList} handleClose={handleClose} handleSubmitForm={handleSubmitForm} />
    ).getByTestId('close-crew-form-icon')
    fireEvent.click(closeIcon)
    expect(handleClose).toHaveBeenCalledTimes(1)
  })

  it('should render submit button and trigger submit method', async () => {
    const wrapper = render(
      <MissionCrewForm crewId={'2'} crewList={crewList} handleClose={handleClose} handleSubmitForm={handleSubmitForm} />
    )
    const submitButton = wrapper.getByTestId('submit-crew-form-button')
    fireEvent.click(submitButton)
    await waitFor(() => {
      expect(handleSubmitForm).toHaveBeenCalledTimes(1)
    })
  })

  it('should render error when not validated', async () => {
    const wrapper = render(
      <MissionCrewForm crewList={crewList} handleClose={handleClose} handleSubmitForm={handleSubmitForm} />
    )
    const submitButton = wrapper.getByTestId('submit-crew-form-button')
    fireEvent.click(submitButton)
    await waitFor(() => {
      expect(wrapper.getAllByText('Fonction requise.').length).toEqual(1)
      expect(wrapper.getAllByText('Identité requise.').length).toEqual(1)
    })
  })

  it('should render error for a comment with more than 23', async () => {
    const wrapper = render(
      <MissionCrewForm crewId={'1'} crewList={crewList} handleClose={handleClose} handleSubmitForm={handleSubmitForm} />
    )
    const submitButton = wrapper.getByTestId('submit-crew-form-button')
    fireEvent.click(submitButton)
    await waitFor(() => {
      expect(wrapper.getAllByText('Maximum 23 caractères.').length).toEqual(1)
    })
  })
})
