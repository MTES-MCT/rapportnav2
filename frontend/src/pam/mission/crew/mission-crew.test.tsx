import { render, screen, fireEvent } from '../../../test-utils'
import { MissionCrew as MissionCrewType } from '../../../types/crew-types'
import MissionCrew from './mission-crew.tsx'
import { vi } from 'vitest'
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
  role: undefined
}

const mockedQueryResult = (crew?: MissionCrewType[], loading: boolean = false, error: any = undefined) => ({
  data: crew,
  loading,
  error
})

describe('MissionCrew', () => {
  describe('Testing rendering', () => {
    it('should render the no crew text', async () => {
      ;(useMissionCrew as any).mockReturnValue(mockedQueryResult(undefined))
      render(<MissionCrew />)
      expect(screen.getByText("Aucun membre d'équipage renseigné")).toBeInTheDocument()
    })
    it('should render the crew when there is', async () => {
      ;(useMissionCrew as any).mockReturnValue(mockedQueryResult([crewMock]))
      render(<MissionCrew />)
      expect(screen.getByText('Identité')).toBeInTheDocument()
    })
    it('should render the add button', async () => {
      render(<MissionCrew />)
      expect(screen.getByText('Ajouter un membre d’équipage')).toBeInTheDocument()
    })
    it('should hide the add button after having clicked on it', async () => {
      render(<MissionCrew />)
      expect(screen.queryAllByText('Identité')).toHaveLength(1)
      fireEvent.click(screen.getByText('Ajouter un membre d’équipage'))
      expect(screen.queryAllByText('Identité')).toHaveLength(2)
    })
  })
})
