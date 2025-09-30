import { render, screen } from '@testing-library/react'
import { describe, it, expect, vi, beforeEach } from 'vitest'
import { useParams } from 'react-router-dom'
import { startOfYear, endOfYear } from 'date-fns'
import { UTCDate } from '@date-fns/utc'
import { useGlobalRoutes } from '@router/use-global-routes.tsx'
import MissionPamPage from '../mission-pam-page.tsx'
import { OwnerType } from '../../features/common/types/owner-type.ts'
import useAuth from '../../features/auth/hooks/use-auth.tsx'

// Mock all the dependencies
vi.mock('react-router-dom', () => ({
  useParams: vi.fn()
}))

vi.mock('@router/use-global-routes.tsx', () => ({
  useGlobalRoutes: vi.fn()
}))

vi.mock('../../features/auth/hooks/use-auth.tsx', () => ({
  __esModule: true,
  default: vi.fn()
}))

vi.mock('../../features/common/components/layout/page-wrapper.tsx', () => ({
  default: ({ header, generalInformations, timeline, action, footer }: any) => (
    <div data-testid="page-wrapper">
      <div data-testid="header">{header}</div>
      <div data-testid="general-info">{generalInformations}</div>
      <div data-testid="timeline">{timeline}</div>
      <div data-testid="action">{action}</div>
      <div data-testid="footer">{footer}</div>
    </div>
  )
}))

vi.mock('../../features/common/components/ui/mission-page-footer.tsx', () => ({
  default: ({ exitMission, missionId, type }: any) => (
    <div data-testid="mission-footer">
      <button onClick={exitMission} data-testid="exit-button">
        Exit
      </button>
      <span data-testid="mission-id">{missionId}</span>
      <span data-testid="type">{type}</span>
    </div>
  )
}))

vi.mock('../../features/pam/components/element/general-info/mission-general-information-pam.tsx', () => ({
  default: ({ missionId }: any) => <div data-testid="general-info-pam">Mission: {missionId}</div>
}))

vi.mock('../../features/pam/components/element/mission-action-pam.tsx', () => ({
  default: ({ missionId, actionId }: any) => (
    <div data-testid="mission-action-pam">
      Mission: {missionId}, Action: {actionId}
    </div>
  )
}))

vi.mock('../../features/pam/components/element/mission-header-pam.tsx', () => ({
  default: ({ onClickClose, missionId }: any) => (
    <div data-testid="mission-header-pam">
      <button onClick={onClickClose} data-testid="close-button">
        Close
      </button>
      <span>Mission: {missionId}</span>
    </div>
  )
}))

vi.mock('../../features/pam/components/element/mission-timeline-pam.tsx', () => ({
  default: ({ missionId }: any) => <div data-testid="mission-timeline-pam">Timeline: {missionId}</div>
}))

vi.mock('../../features/pam/components/ui/offline-dialog.tsx', () => ({
  default: () => <div data-testid="offline-dialog">Offline Dialog</div>
}))

// Mock date-fns functions
vi.mock('date-fns', () => ({
  startOfYear: vi.fn(),
  endOfYear: vi.fn()
}))

vi.mock('@date-fns/utc', () => ({
  UTCDate: {
    now: vi.fn()
  }
}))

describe('MissionPamPage', () => {
  const mockGetUrl = vi.fn()
  const mockNavigateAndResetCache = vi.fn()
  const mockUseParams = useParams as any
  const mockUseGlobalRoutes = useGlobalRoutes as any
  const mockUseAuth = useAuth as unknown as vi.Mock
  const mockStartOfYear = startOfYear as any
  const mockEndOfYear = endOfYear as any
  const mockUTCDateNow = UTCDate.now as any

  beforeEach(() => {
    vi.clearAllMocks()

    mockUseGlobalRoutes.mockReturnValue({ getUrl: mockGetUrl })
    mockUseAuth.mockReturnValue({ navigateAndResetCache: mockNavigateAndResetCache })

    // Mock date functions
    const mockNow = new Date('2025-06-15T12:00:00Z')
    const startOfYearDate = new Date('2025-01-01T00:00:00Z')
    const endOfYearDate = new Date('2025-12-31T23:59:59Z')

    mockUTCDateNow.mockReturnValue(mockNow)
    mockStartOfYear.mockReturnValue(startOfYearDate)
    mockEndOfYear.mockReturnValue(endOfYearDate)
    mockGetUrl.mockReturnValue('/missions?startDateTimeUtc=2025-01-01&endDateTimeUtc=2025-12-31')
  })

  describe('exitMission function', () => {
    it('calls exitMission when close button in header is clicked', () => {
      mockUseParams.mockReturnValue({ missionId: '123', actionId: '456' })

      render(<MissionPamPage />)

      const closeButton = screen.getByTestId('close-button')
      closeButton.click()

      expect(mockGetUrl).toHaveBeenCalledWith(OwnerType.MISSION)
      expect(mockNavigateAndResetCache).toHaveBeenCalled()
    })

    it('calls exitMission when exit button in footer is clicked', () => {
      mockUseParams.mockReturnValue({ missionId: '123', actionId: '456' })

      render(<MissionPamPage />)

      const exitButton = screen.getByTestId('exit-button')
      exitButton.click()

      expect(mockGetUrl).toHaveBeenCalledWith(OwnerType.MISSION)
      expect(mockNavigateAndResetCache).toHaveBeenCalled()
    })
  })

  describe('Hook usage', () => {
    it('calls useGlobalRoutes hook', () => {
      mockUseParams.mockReturnValue({ missionId: '123' })
      render(<MissionPamPage />)
      expect(mockUseGlobalRoutes).toHaveBeenCalled()
    })

    it('calls useParams hook', () => {
      mockUseParams.mockReturnValue({ missionId: '123' })
      render(<MissionPamPage />)
      expect(mockUseParams).toHaveBeenCalled()
    })

    it('calls useAuth hook', () => {
      mockUseParams.mockReturnValue({ missionId: '123' })
      render(<MissionPamPage />)
      expect(mockUseAuth).toHaveBeenCalled()
    })
  })
})
