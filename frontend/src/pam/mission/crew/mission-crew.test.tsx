import { vi } from 'vitest'
import { render, screen } from '../../../test-utils'
import { MissionCrew as MissionCrewType } from '../../../types/crew-types'
import MissionCrew from './mission-crew.tsx'
import useMissionCrew from './use-mission-crew.tsx'

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
    expect(screen.getByText("Aucun membre d'équipage renseigné")).toBeInTheDocument()
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

  //TODO: should show mission crew modal form
})
