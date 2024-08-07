import { describe, expect, test, vi } from 'vitest'
import { mockQueryResult, render, screen } from '../../test-utils'

import {Mission} from '../../types/mission-types.ts'
import { CompletenessForStatsStatusEnum, MissionStatusEnum } from '../../types/mission-types.ts'
import MissionItem from './mission-item.tsx'
import { fireEvent } from '@testing-library/react'
import useIsMissionCompleteForStats from './use-is-mission-complete-for-stats.tsx'

const mission = {
  id: 3,
  startDateTimeUtc: '2022-08-07T12:00:00Z',
  endDateTimeUtc: '2022-08-19T12:00:00Z',
  status: MissionStatusEnum.ENDED,
  completenessForStats: {
    status: CompletenessForStatsStatusEnum.COMPLETE
  }
} as unknown as Mission

const missionNotComplete = {
  id: 3,
  startDateTimeUtc: '2022-08-07T12:00:00Z',
  endDateTimeUtc: '2022-08-19T12:00:00Z',
  status: MissionStatusEnum.ENDED,
  completenessForStats: {
    status: CompletenessForStatsStatusEnum.INCOMPLETE
  }
} as unknown as Mission



vi.mock('./use-is-mission-complete-for-stats', async importOriginal => {
  const actual = await importOriginal()
  return {
    ...actual,
    default: vi.fn()
  }
})

const exportLazyAEMMock = vi.fn()
const exportLazyReportMock = vi.fn()
vi.mock('./export/use-lazy-mission-aem-export.tsx', () => ({
  default: () => [exportLazyAEMMock, { }]
}))

vi.mock('./export/use-lazy-mission-export.tsx', () => ({
  default: () => [exportLazyReportMock, { }]
}))


describe('Mission Item component', () => {
  afterEach(() => {
    vi.clearAllMocks()
    vi.resetAllMocks()

    exportLazyAEMMock.mockResolvedValue({
      data: {
        missionAEMExport: {
          fileName: "test.ods",
          fileContent: "base64EncodedContent"
        }
      },
      loading: false,
      error: null
    })

    exportLazyReportMock.mockResolvedValue({
      data: {
        missionAEMExport: {
          fileName: "test.odt",
          fileContent: "base64EncodedContent"
        }
      },
      loading: false,
      error: null
    })
  })

  test('should render the Exporter le rapport de mission button on mouse over if mission is complete for stats', () => {
    ;(useIsMissionCompleteForStats as any).mockReturnValue(mockQueryResult(true))

    const { container } = render(<MissionItem mission={mission} prefetchMission={vi.fn()} />)
    const missionItemElement = container.firstChild

    expect(screen.queryByText('Exporter le rapport de la mission', { exact: false })).not.toBeInTheDocument()
    fireEvent.mouseOver(missionItemElement)
    expect(screen.getByText('Exporter le rapport de la mission', { exact: false })).toBeInTheDocument()
  })

  test('should not render the Exporter le rapport de mission button on mouse over if the mission is not complete for stats', () => {
    const { container } = render(<MissionItem mission={missionNotComplete} prefetchMission={vi.fn()} />)
    const missionItemElement = container.firstChild
    fireEvent.mouseOver(missionItemElement)
    expect(screen.queryByText('Exporter le rapport de la mission', { exact: true })).not.toBeInTheDocument()
  })

  test('should trigger getMissionReport on click button Exporter le rapport de mission', () => {
    ;(useIsMissionCompleteForStats as any).mockReturnValue(mockQueryResult(true))

    const { container } = render(<MissionItem mission={mission} prefetchMission={vi.fn()} />)
    const missionItemElement = container.firstChild

    fireEvent.mouseOver(missionItemElement)

    const downloadButton = screen.getByTestId('download-report-button');
    fireEvent.click(downloadButton);

    expect(exportLazyReportMock).toHaveBeenCalled()
  })

  test('should trigger getMissionAEMReport on click Télécharger les tableaux', () => {
    ;(useIsMissionCompleteForStats as any).mockReturnValue(mockQueryResult(true))

    const { container } = render(<MissionItem mission={mission} prefetchMission={vi.fn()} />)
    const missionItemElement = container.firstChild

    fireEvent.mouseOver(missionItemElement)

    const downloadButton = screen.getByTestId('download-aem-button');
    fireEvent.click(downloadButton);

    expect(exportLazyAEMMock).toHaveBeenCalled()
  })
})


