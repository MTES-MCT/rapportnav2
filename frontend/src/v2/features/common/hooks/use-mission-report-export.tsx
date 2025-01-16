import { logSoftError } from '@mtes-mct/monitor-ui'
import { useState } from 'react'
import {
  MissionPatrolExportMutationArgs,
  useLazyExportMissionReports
} from '../services/use-lazy-export-mission-reports.tsx'
import { ExportMode } from '../types/mission-export-types.ts'
import { BLOBTYPE, useDownloadFile } from './use-download-file.tsx'

interface ExportMissionHook {
  exportIsLoading: boolean
  exportMissionReport: (args: MissionPatrolExportMutationArgs) => Promise<void>
}

export function useMissionReportExport(): ExportMissionHook {
  const { handleDownload } = useDownloadFile()
  const [getMissionReport] = useLazyExportMissionReports()
  const [exportIsLoading, setLoading] = useState<boolean>(false)

  const exportMissionReport = async (args: MissionPatrolExportMutationArgs) => {
    setLoading(true)
    const { data, error } = await getMissionReport({ variables: args, fetchPolicy: 'network-only' })

    if (error) {
      setLoading(false)
      logSoftError({
        isSideWindowError: false,
        message: error.message,
        userMessage: `Le rapport n'a pas pu être généré. Si l'erreur persiste, veuillez contacter l'équipe RapportNav/SNC3.`
      })
    } else if (data) {
      setLoading(false)
      const blobType = args.exportMode === ExportMode.MULTIPLE_MISSIONS_ZIPPED ? BLOBTYPE.ZIP : BLOBTYPE.ODT
      const file = (data as any)?.exportMissionReports
      await handleDownload(blobType, file as any)
    }
  }

  return { exportMissionReport, exportIsLoading }
}
