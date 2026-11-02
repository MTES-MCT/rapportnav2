import { vi } from 'vitest'
import { render, screen, fireEvent, waitFor } from '../../../test-utils.tsx'
import MissionListPamPage from '../mission-list-pam-page.tsx'
import useAuth from '../../features/auth/hooks/use-auth.tsx'
import { useMissionList } from '../../features/common/hooks/use-mission-list.tsx'
import useMissionsQuery from '../../features/common/services/use-missions.tsx'
import { useOnlineManager } from '../../features/common/hooks/use-online-manager.tsx'
import { useOfflineMode } from '../../features/common/hooks/use-offline-mode.tsx'
import { useMissionReportExport } from '../../features/common/hooks/use-mission-report-export.tsx'
import { ExportMode, ExportReportType } from '../../features/common/types/mission-export-types.ts'

// Mock all dependencies
vi.mock('../../features/auth/hooks/use-auth.tsx', () => ({
  default: vi.fn()
}))

vi.mock('../../features/common/hooks/use-mission-list.tsx', () => ({
  useMissionList: vi.fn()
}))

vi.mock('../../features/common/hooks/use-mission-report-export.tsx', () => ({
  useMissionReportExport: vi.fn()
}))

vi.mock('../../features/common/services/use-missions.tsx', () => ({
  default: vi.fn(),
  MISSION_LIST_PAGE_SIZE: 15
}))

vi.mock('../../features/common/hooks/use-offline-mode.tsx', () => ({
  useOfflineMode: vi.fn()
}))

vi.mock('../../features/common/hooks/use-online-manager.tsx', () => ({
  useOnlineManager: vi.fn()
}))

describe('MissionListPamPage', () => {
  let mockExportMissionReport: any
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
      missions: [],
      isError: false,
      hasNextPage: false,
      isFetchingNextPage: false,
      fetchNextPage: vi.fn()
    })

    vi.mocked(useMissionReportExport).mockReturnValue({
      mutate: vi.fn(),
      isPending: false
    })
    vi.mocked(useMissionReportExport).mockReturnValue({
      exportMissionReport: mockExportMissionReport,
      exportIsLoading: false
    })

    vi.mocked(useOfflineMode).mockReturnValue(false)

    vi.mocked(useOnlineManager).mockReturnValue({ isOffline: false } as any)
  })

  describe('rendering', () => {
    it('should render mission list with correct missions', () => {
      vi.mocked(useMissionsQuery).mockReturnValue({
        isLoading: false,
        missions: [
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

  describe('date range filter', () => {
    it('should render the period dropdown (no date default)', () => {
      render(<MissionListPamPage />)
      // no date default anymore: the "Période" dropdown renders with no preset range selected
      expect(screen.getByText('Période')).toBeInTheDocument()
    })

    it('should render the status + completeness filters but not the report-type filter (hidden on PAM)', () => {
      render(<MissionListPamPage />)
      expect(screen.getByText('Statut de la mission')).toBeInTheDocument()
      expect(screen.getByText('État des données')).toBeInTheDocument()
      expect(screen.queryByText('Type de rapport')).not.toBeInTheDocument()
    })
  })

  describe('loading states', () => {
    it('should show loading state when missions are loading', () => {
      vi.mocked(useMissionsQuery).mockReturnValue({
        isLoading: true,
        missions: undefined
      })
      render(<MissionListPamPage />)
      expect(screen.getByTestId('mission-list-loader')).toBeInTheDocument()
    })

    it('should not show loading state when missions are loaded', () => {
      vi.mocked(useMissionsQuery).mockReturnValue({
        isLoading: false,
        missions: []
      })
      render(<MissionListPamPage />)
      expect(screen.queryByTestId('mission-list-loader')).not.toBeInTheDocument()
    })
  })

  describe('empty states', () => {
    it('should handle empty mission list', () => {
      vi.mocked(useMissionsQuery).mockReturnValue({
        isLoading: false,
        missions: []
      })
      render(<MissionListPamPage />)
      expect(screen.queryByTestId('mission-list-item')).not.toBeInTheDocument()
      expect(screen.getByText('Aucune mission pour cette période de temps.')).toBeInTheDocument()
    })

    it('should handle empty mission list (offline version)', () => {
      vi.mocked(useOnlineManager).mockReturnValue({ isOffline: true } as any)
      vi.mocked(useMissionsQuery).mockReturnValue({
        isLoading: false,
        missions: []
      })
      render(<MissionListPamPage />)
      expect(screen.queryByTestId('mission-list-item')).not.toBeInTheDocument()
      expect(screen.getByText('Veuillez repasser en ligne pour resynchroniser.')).toBeInTheDocument()
    })

    it('should handle undefined mission list', () => {
      vi.mocked(useMissionsQuery).mockReturnValue({
        isLoading: false,
        missions: undefined
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
      expect(useMissionReportExport).toHaveBeenCalled()
    })
  })

  describe('export reports', () => {
    beforeEach(() => {
      mockExportMissionReport = vi.fn(() => Promise.resolve())

      vi.mocked(useMissionReportExport).mockReturnValue({
        exportMissionReport: mockExportMissionReport,
        exportIsLoading: false
      })

      vi.mocked(useMissionsQuery).mockReturnValue({
        isLoading: false,
        missions: [
          {
            id: 1,
            startDateTimeUtc: '2024-02-09T09:00Z',
            endDateTimeUtc: '2024-02-21T09:00Z',
            missionNamePam: 'Mission #2024-01-09'
          },
          {
            id: 2,
            startDateTimeUtc: '2024-01-09T09:00Z',
            endDateTimeUtc: '2024-01-21T09:00Z',
            missionNamePam: 'Mission #2024-02-09'
          }
        ]
      })
    })
    it('should export immediately when exactly 1 mission is selected', async () => {
      render(<MissionListPamPage />)

      // Select mission #1
      fireEvent.click(screen.getAllByTitle('Sélectionner cette mission')[0])

      // Click export button (variant comes from button)
      fireEvent.click(screen.getByTestId('export-patrol'))

      await waitFor(() => {
        expect(screen.queryByTestId('export-dialog')).not.toBeInTheDocument()
        expect(mockExportMissionReport).toHaveBeenCalledWith({
          missionIds: [1],
          exportMode: ExportMode.COMBINED_MISSIONS_IN_ONE,
          reportType: ExportReportType.PATROL
        })
      })
    })

    it('should show export modal when more than 1 mission is selected', async () => {
      render(<MissionListPamPage />)

      const missionItems = screen.getAllByTitle('Sélectionner cette mission')
      fireEvent.click(missionItems[0])
      fireEvent.click(missionItems[1])

      fireEvent.click(screen.getByTestId('export-aem'))

      await waitFor(() => {
        expect(screen.getByTestId('export-dialog')).toBeInTheDocument()
        expect(mockExportMissionReport).not.toHaveBeenCalled()
      })
    })

    it('should show export modal when more than 1 mission is selected', async () => {
      render(<MissionListPamPage />)

      fireEvent.click(screen.getByTitle('Tout sélectionner'))
      fireEvent.click(screen.getByTestId('export-aem'))

      await waitFor(() => {
        expect(screen.getByTestId('export-dialog')).toBeInTheDocument()
        expect(mockExportMissionReport).not.toHaveBeenCalled()
      })
    })

    it('should not export when no mission is selected', async () => {
      render(<MissionListPamPage />)

      fireEvent.click(screen.getByTestId('export-patrol'))

      expect(mockExportMissionReport).not.toHaveBeenCalled()
      expect(screen.queryByTestId('export-dialog')).not.toBeInTheDocument()
    })
  })
})
