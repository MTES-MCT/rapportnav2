import { describe, expect, test, vi } from 'vitest'
import { render, screen } from '../../../../../test-utils.tsx'

import { Mission } from '../../../../common/types/mission-types.ts'
import { CompletenessForStatsStatusEnum, MissionStatusEnum } from '../../../../common/types/mission-types.ts'
import MissionItem from './mission-item.tsx'
import { fireEvent } from '@testing-library/react'
import * as useIsMissionCompleteForStatsModule from '@features/pam/mission/hooks/use-is-mission-complete-for-stats'
import * as useAEMModule from '@features/pam/mission/hooks/export/use-lazy-mission-aem-export'
import * as useExportModule from '@features/pam/mission/hooks/export/use-lazy-mission-export'
import { hexToRgb } from '@common/utils/colors.ts'
import { THEME } from '@mtes-mct/monitor-ui'

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

const exportLazyAEMMock = vi.fn()
const exportLazyReportMock = vi.fn()

describe('Mission Item component', () => {
  beforeEach(() => {
    vi.spyOn(useAEMModule, 'default').mockReturnValue([exportLazyAEMMock, { error: undefined }])
    vi.spyOn(useExportModule, 'default').mockReturnValue([exportLazyReportMock, { error: undefined }])
  })
  afterEach(() => {
    vi.clearAllMocks()
    vi.resetAllMocks()

    exportLazyAEMMock.mockResolvedValue({
      data: {
        missionAEMExport: {
          fileName: 'test.ods',
          fileContent: 'base64EncodedContent'
        }
      },
      loading: false,
      error: null
    })

    exportLazyReportMock.mockResolvedValue({
      data: {
        missionAEMExport: {
          fileName: 'test.odt',
          fileContent: 'base64EncodedContent'
        }
      },
      loading: false,
      error: null
    })
  })

  test('should render the Exporter le rapport de mission button on mouse over if mission is complete for stats', () => {
    vi.spyOn(useIsMissionCompleteForStatsModule, 'default').mockReturnValue({ data: true, loading: false, error: null })

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
    vi.spyOn(useIsMissionCompleteForStatsModule, 'default').mockReturnValue({ data: true, loading: false, error: null })

    const { container } = render(<MissionItem mission={mission} prefetchMission={vi.fn()} />)
    const missionItemElement = container.firstChild

    fireEvent.mouseOver(missionItemElement)

    const downloadButton = screen.getByTestId('download-report-button')
    fireEvent.click(downloadButton)

    expect(exportLazyReportMock).toHaveBeenCalled()
  })

  test('should trigger getMissionAEMReport on click Télécharger les tableaux', () => {
    vi.spyOn(useIsMissionCompleteForStatsModule, 'default').mockReturnValue({ data: true, loading: false, error: null })
    vi.spyOn(useIsMissionCompleteForStatsModule, 'default').mockReturnValue({ data: true, loading: false, error: null })

    const { container } = render(<MissionItem mission={mission} prefetchMission={vi.fn()} />)
    const missionItemElement = container.firstChild

    fireEvent.mouseOver(missionItemElement)

    const downloadButton = screen.getByTestId('download-aem-button')
    fireEvent.click(downloadButton)

    expect(exportLazyAEMMock).toHaveBeenCalled()
  })

  test('should have a background color blueGray25 on ListItemHover when mouseOver', () => {
    vi.spyOn(useIsMissionCompleteForStatsModule, 'default').mockReturnValue({ data: true, loading: false, error: null })
    vi.spyOn(useIsMissionCompleteForStatsModule, 'default').mockReturnValue({ data: true, loading: false, error: null })

    const { container } = render(<MissionItem mission={mission} prefetchMission={vi.fn()} />)
    const missionItemElement = container.firstChild

    fireEvent.mouseOver(missionItemElement)
    const listItemWithHover = screen.getByTestId('list-item-with-hover')
    expect(getComputedStyle(listItemWithHover).backgroundColor).toBe(hexToRgb(THEME.color.blueGray25))

  })
})
