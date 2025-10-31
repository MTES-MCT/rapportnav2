import { useMutation, UseMutationResult } from '@tanstack/react-query'

import axios from '../../../../query-client/axios'
import { ExportMode, ExportReportType, MissionExport } from '../types/mission-export-types.ts'

type ExportMissionBody = {
  missionIds?: number[]
  exportMode: ExportMode
  reportType: ExportReportType
}

const exportMission = (body: ExportMissionBody): Promise<void> => axios.post(`missions/export`, body)

const useExportMission = (): UseMutationResult<MissionExport, Error, ExportMissionBody, unknown> => {
  const mutation = useMutation({
    mutationFn: exportMission
  })
  return mutation
}

export default useExportMission
