import { vi } from 'vitest'
import { render, screen, fireEvent, waitFor } from '../../../test-utils.tsx'
import MissionListPamPage from '../mission-list-pam-page.tsx'
import useAuth from '../../features/auth/hooks/use-auth.tsx'
import { useMissionList } from '../../features/common/hooks/use-mission-list.tsx'
import useMissionsQuery from '../../features/common/services/use-missions.tsx'
import { useOfflineMode } from '../../features/common/hooks/use-offline-mode.tsx'
import useExportMission from '../../features/common/services/use-mission-export.tsx'

// Mock all dependencies
vi.mock('../../features/auth/hooks/use-auth.tsx', () => ({
  default: vi.fn()
}))

vi.mock('../../features/common/hooks/use-mission-list.tsx', () => ({
  useMissionList: vi.fn()
}))

vi.mock('../../features/common/services/use-mission-export.test.tsx', () => ({
  default: vi.fn()
}))

vi.mock('../../features/common/services/use-missions.tsx', () => ({
  default: vi.fn()
}))

vi.mock('../../features/common/hooks/use-offline-mode.tsx', () => ({
  useOfflineMode: vi.fn()
}))

describe('MissionListPamPage', () => {
  beforeEach(() => {
    vi.clearAllMocks()

    // Default mock implementations
    vi.mocked(useAuth).mockReturnValue({
      isLoggedIn: () => ({ userId: 'test-user-123' })
    })

    vi.mocked(useMissionList).mockReturnValue({
      getMissionListItem: vi.fn(mission => ({ ...mission, listItem: true }))
    })

    vi.mocked(useMissionsQuery).mockReturnValue({
      isLoading: false,
      data: []
    })

    vi.mocked(useExportMission).mockReturnValue({
      mutate: vi.fn(),
      isPending: false
    })

    vi.mocked(useOfflineMode).mockReturnValue(false)
  })

  describe('rendering', () => {
    it('should render mission list with correct missions', () => {
      vi.mocked(useMissionsQuery).mockReturnValue({
        isLoading: false,
        data: [
          {
            id: 1,
            startDateTimeUtc: '2024-01-09T09:00Z',
            endDateTimeUtc: '2024-01-21T09:00Z',
            missionNamePam: 'Mission #2024-01-09'
          },
          {
            id: 2,
            startDateTimeUtc: '2024-02-09T09:00Z',
            endDateTimeUtc: '2024-02-21T09:00Z',
            missionNamePam: 'Mission #2024-02-09'
          }
        ]
      })
      render(<MissionListPamPage />)
      expect(screen.queryAllByTestId('mission-list-item')).toHaveLength(2)
      expect(screen.getByText('Mission #2024-01-09')).toBeInTheDocument()
      expect(screen.getByText('Mission #2024-02-09')).toBeInTheDocument()
    })
  })

  describe('OnlineToggle conditional rendering', () => {
    it('should render OnlineToggle when offline mode is enabled', () => {
      vi.mocked(useOfflineMode).mockReturnValue(true)
      render(<MissionListPamPage />)
      expect(screen.getByTestId('online-toggle')).toBeInTheDocument()
    })

    it('should not render OnlineToggle when offline mode is disabled', () => {
      vi.mocked(useOfflineMode).mockReturnValue(false)
      render(<MissionListPamPage />)
      expect(screen.queryByTestId('online-toggle')).not.toBeInTheDocument()
    })

    it('should not render OnlineToggle when offline mode is undefined', () => {
      vi.mocked(useOfflineMode).mockReturnValue(undefined)
      render(<MissionListPamPage />)
      expect(screen.queryByTestId('online-toggle')).not.toBeInTheDocument()
    })
  })

  describe('date range navigation', () => {
    it('should update query params when date is changed with previous button', async () => {
      render(<MissionListPamPage />)

      const updateDateBtn = screen.getByTestId('previous-button')
      fireEvent.click(updateDateBtn)

      // The date navigator should trigger the update
      await waitFor(() => {
        expect(screen.getByTestId('date-range-navigator')).toBeInTheDocument()
      })
    })
    it('should update query params when date is changed with next button', async () => {
      render(<MissionListPamPage />)

      const updateDateBtn = screen.getByTestId('next-button')
      fireEvent.click(updateDateBtn)

      // The date navigator should trigger the update
      await waitFor(() => {
        expect(screen.getByTestId('date-range-navigator')).toBeInTheDocument()
      })
    })

    it('should initialize with current year date range', () => {
      render(<MissionListPamPage />)
      const dateNavigator = screen.getByTestId('date-range-navigator')
      const d = new Date()
      expect(dateNavigator).toHaveTextContent(d.getFullYear())
    })
  })

  describe('loading states', () => {
    it('should show loading state when missions are loading', () => {
      vi.mocked(useMissionsQuery).mockReturnValue({
        isLoading: true,
        data: undefined
      })
      render(<MissionListPamPage />)
      expect(screen.getByTestId('mission-list-loader')).toBeInTheDocument()
    })

    it('should not show loading state when missions are loaded', () => {
      vi.mocked(useMissionsQuery).mockReturnValue({
        isLoading: false,
        data: []
      })
      render(<MissionListPamPage />)
      expect(screen.queryByTestId('mission-list-loader')).not.toBeInTheDocument()
    })
  })

  describe('empty states', () => {
    it('should handle empty mission list', () => {
      vi.mocked(useMissionsQuery).mockReturnValue({
        isLoading: false,
        data: []
      })
      render(<MissionListPamPage />)
      expect(screen.queryByTestId('mission-list-item')).not.toBeInTheDocument()
      expect(screen.getByText('Aucune mission pour cette période de temps.')).toBeInTheDocument()
    })

    it('should handle undefined mission list', () => {
      vi.mocked(useMissionsQuery).mockReturnValue({
        isLoading: false,
        data: undefined
      })
      render(<MissionListPamPage />)
      expect(screen.getByText('Aucune mission pour cette période de temps.')).toBeInTheDocument()
      expect(screen.queryByTestId('mission-list-item')).not.toBeInTheDocument()
    })
  })

  describe('hook integration', () => {
    it('should call all required hooks', () => {
      render(<MissionListPamPage />)

      expect(useAuth).toHaveBeenCalled()
      expect(useOfflineMode).toHaveBeenCalled()
      expect(useMissionList).toHaveBeenCalled()
      expect(useMissionsQuery).toHaveBeenCalled()
      expect(useExportMission).toHaveBeenCalled()
    })
  })
})
