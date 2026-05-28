import { vi } from 'vitest'
import { render, screen, fireEvent, waitFor } from '../../../test-utils.tsx'
import MissionListUlamPage from '../mission-list-ulam-page.tsx'
import { useMissionList } from '../../features/common/hooks/use-mission-list.tsx'
import useMissionsQuery from '../../features/common/services/use-missions.tsx'

vi.mock('../../features/common/hooks/use-mission-list.tsx', () => ({
  useMissionList: vi.fn()
}))

vi.mock('../../features/common/services/use-missions.tsx', () => ({
  default: vi.fn()
}))

vi.mock('../../features/ulam/components/element/mission-create-dialog.tsx', () => ({
  default: ({ isOpen, onClose }: { isOpen: boolean; onClose: () => void }) =>
    isOpen ? (
      <div data-testid="create-dialog">
        <button onClick={onClose}>close</button>
      </div>
    ) : null
}))

describe('MissionListUlamPage', () => {
  beforeEach(() => {
    vi.clearAllMocks()

    vi.mocked(useMissionList).mockReturnValue({
      getMissionListItem: vi.fn((m) => ({ ...m, listItem: true }))
    })

    vi.mocked(useMissionsQuery).mockReturnValue({
      isLoading: false,
      data: []
    } as any)
  })

  it('should render page title', () => {
    render(<MissionListUlamPage />)
    expect(screen.getByText('Mes rapports journaliers')).toBeInTheDocument()
  })

  it('should render create mission button', () => {
    render(<MissionListUlamPage />)
    expect(screen.getByText('Créer un rapport de mission')).toBeInTheDocument()
  })

  it('should open create dialog when button is clicked', async () => {
    render(<MissionListUlamPage />)
    expect(screen.queryByTestId('create-dialog')).not.toBeInTheDocument()

    fireEvent.click(screen.getByText('Créer un rapport de mission'))

    await waitFor(() => {
      expect(screen.getByTestId('create-dialog')).toBeInTheDocument()
    })
  })

  it('should show empty state when no missions', () => {
    render(<MissionListUlamPage />)
    expect(screen.getByText('Aucune mission pour cette période de temps.')).toBeInTheDocument()
  })

  it('should show loading state', () => {
    vi.mocked(useMissionsQuery).mockReturnValue({
      isLoading: true,
      data: undefined
    } as any)
    render(<MissionListUlamPage />)
    expect(screen.getByTestId('mission-list-loader')).toBeInTheDocument()
  })

  it('should render date range navigator with month timeframe', () => {
    render(<MissionListUlamPage />)
    expect(screen.getByTestId('date-range-navigator')).toBeInTheDocument()
  })
})
