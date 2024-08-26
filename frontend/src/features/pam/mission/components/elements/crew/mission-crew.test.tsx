import { vi } from 'vitest'
import { fireEvent, render, screen } from '../../../../../../test-utils.tsx'
import { MissionCrew as MissionCrewType } from '../../../../../common/types/crew-types.ts'
import MissionCrew from './mission-crew.tsx'
import useMissionCrew from '../../../hooks/use-mission-crew.tsx'

const mutateMock = vi.fn()
const deleteMock = vi.fn()

vi.mock('./use-mission-crew.tsx', async importOriginal => {
  const actual = await importOriginal()
  return {
    ...actual,
    default: vi.fn()
  }
})
vi.mock('./use-add-update-mission-crew', () => ({
  default: () => [mutateMock, { error: undefined }]
}))
vi.mock('./use-delete-mission-crew', () => ({
  default: () => [deleteMock, { error: undefined }]
}))

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

const mockedQueryResult = (crew?: MissionCrewType[], loading: boolean = false, error: any = undefined) => ({
  data: crew,
  loading,
  error
})

const handleAction = (id: string) => console.log(id)

describe('MissionCrew', () => {
  it('should render the no crew text', async () => {
    ;(useMissionCrew as any).mockReturnValue(mockedQueryResult(undefined))
    render(<MissionCrew />)
    expect(screen.getByText("Selectionner votre bordée, pour importer votre liste d'équipage")).toBeInTheDocument()
  })

  it('should render the crew when there is', async () => {
    ;(useMissionCrew as any).mockReturnValue(mockedQueryResult([crewMock]))
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
