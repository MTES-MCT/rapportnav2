import { logSoftError } from '@mtes-mct/monitor-ui'
import { useState } from 'react'
import { ExportMode, ExportReportType } from '../types/mission-export-types.ts'
import { BLOBTYPE, useDownloadFile } from './use-download-file.tsx'
import useExportMission from '../services/use-mission-export.tsx'

interface ExportMissionHookProps {
  missionIds: number[]
  exportMode: ExportMode
  reportType: ExportReportType
}
interface ExportMissionHook {
  exportIsLoading: boolean
  exportMissionReport: (args: ExportMissionHookProps) => Promise<void>
}

export function useMissionReportExport2(): ExportMissionHook {
  const { handleDownload } = useDownloadFile()
  const { mutateAsync, error } = useExportMission()
  const [exportIsLoading, setLoading] = useState<boolean>(false)

  const exportMissionReport = async ({ missionIds, exportMode, reportType }: ExportMissionHookProps) => {
    setLoading(true)
    const { data } = await mutateAsync({
      missionIds: missionIds,
      exportMode: exportMode,
      reportType: reportType
    })
    debugger

    if (error) {
      debugger
      setLoading(false)
      logSoftError({
        isSideWindowError: false,
        message: error.message,
        userMessage: `Le rapport n'a pas pu être généré. Si l'erreur persiste, veuillez contacter l'équipe RapportNav/SNC3.`
      })
    } else if (data) {
      debugger
      setLoading(false)
      const blobType = exportMode === ExportMode.MULTIPLE_MISSIONS_ZIPPED ? BLOBTYPE.ZIP : BLOBTYPE.ODT
      await handleDownload(blobType, data)
    }
  }

  return { exportMissionReport, exportIsLoading }
}
