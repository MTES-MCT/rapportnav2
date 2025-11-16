import { renderHook, act, waitFor } from '../../../../../test-utils.tsx'
import { vi } from 'vitest'
import { useMissionReportExport } from '../use-mission-report-export.tsx'
import { ExportMode, ExportReportType } from '../../types/mission-export-types'
import { BLOBTYPE } from '../use-download-file'
import { logSoftError } from '@mtes-mct/monitor-ui'

// ------------------
// MOCKS
// ------------------

vi.mock('@mtes-mct/monitor-ui', async () => {
  const actual = await vi.importActual<any>('@mtes-mct/monitor-ui')
  return {
    ...actual,
    logSoftError: vi.fn()
  }
})

const mockHandleDownload = vi.fn()

vi.mock('../use-download-file', () => ({
  useDownloadFile: () => ({ handleDownload: mockHandleDownload }),
  BLOBTYPE: {
    ZIP: 'application/zip',
    ODT: 'application/vnd.oasis.opendocument.text'
  }
}))

const mockMutateAsync = vi.fn()
let mutationError: Error | null = null

vi.mock('../../services/use-mission-export.test.tsx', () => ({
  __esModule: true,
  default: () => ({
    mutateAsync: mockMutateAsync,
    error: mutationError
  })
}))

// ------------------
// TESTS
// ------------------

describe('useMissionReportExport', () => {
  beforeEach(() => {
    vi.clearAllMocks()
    mutationError = null
  })

  it('should call mutateAsync with correct arguments', async () => {
    mockMutateAsync.mockResolvedValue({ data: new Blob(['test']) })

    const { result } = renderHook(() => useMissionReportExport())

    await act(async () => {
      await result.current.exportMissionReport({
        missionIds: [1, 2],
        exportMode: ExportMode.INDIVIDUAL_MISSION,
        reportType: ExportReportType.PATROL
      })
    })

    expect(mockMutateAsync).toHaveBeenCalledWith({
      missionIds: [1, 2],
      exportMode: ExportMode.INDIVIDUAL_MISSION,
      reportType: ExportReportType.PATROL
    })
  })

  it('should download ODT when exportMode ≠ MULTIPLE_MISSIONS_ZIPPED', async () => {
    const blob = new Blob(['odt'])
    mockMutateAsync.mockResolvedValue({ data: blob })

    const { result } = renderHook(() => useMissionReportExport())

    await act(async () => {
      await result.current.exportMissionReport({
        missionIds: [1],
        exportMode: ExportMode.INDIVIDUAL_MISSION,
        reportType: ExportReportType.PATROL
      })
    })

    expect(mockHandleDownload).toHaveBeenCalledWith(BLOBTYPE.ODT, blob)
  })

  it('should download ZIP when exportMode = MULTIPLE_MISSIONS_ZIPPED', async () => {
    const blob = new Blob(['zip'])
    mockMutateAsync.mockResolvedValue({ data: blob })

    const { result } = renderHook(() => useMissionReportExport())

    await act(async () => {
      await result.current.exportMissionReport({
        missionIds: [10],
        exportMode: ExportMode.MULTIPLE_MISSIONS_ZIPPED,
        reportType: ExportReportType.PATROL
      })
    })

    expect(mockHandleDownload).toHaveBeenCalledWith(BLOBTYPE.ZIP, blob)
  })

  it('should log soft error when an error occurs', async () => {
    mutationError = new Error('Something bad happened')
    mockMutateAsync.mockResolvedValue({ data: null })

    const { result } = renderHook(() => useMissionReportExport())

    await act(async () => {
      await result.current.exportMissionReport({
        missionIds: [5],
        exportMode: ExportMode.INDIVIDUAL_MISSION,
        reportType: ExportReportType.PATROL
      })
    })

    expect(logSoftError).toHaveBeenCalledWith({
      isSideWindowError: false,
      message: 'Something bad happened',
      userMessage: expect.stringContaining(`Le rapport n'a pas pu être généré`)
    })

    expect(mockHandleDownload).not.toHaveBeenCalled()
  })

  it('should set loading state during execution', async () => {
    const blob = new Blob(['abc'])
    mockMutateAsync.mockResolvedValue({ data: blob })

    const { result } = renderHook(() => useMissionReportExport())

    act(() => {
      // do NOT await here — start execution
      result.current.exportMissionReport({
        missionIds: [1],
        exportMode: ExportMode.INDIVIDUAL_MISSION,
        reportType: ExportReportType.PATROL
      })
    })

    // 🔥 wait until loading becomes true
    await waitFor(() => {
      expect(result.current.exportIsLoading).toBe(true)
    })

    // 🔥 then wait until loading becomes false (after mutateAsync resolves)
    await waitFor(() => {
      expect(result.current.exportIsLoading).toBe(false)
    })
  })
})
