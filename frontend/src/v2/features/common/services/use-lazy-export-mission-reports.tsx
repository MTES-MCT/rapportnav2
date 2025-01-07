import {
  DocumentNode,
  gql,
  LazyQueryHookExecOptions,
  LazyQueryResultTuple,
  OperationVariables,
  QueryResult,
  useLazyQuery
} from '@apollo/client'
import { MissionExport } from '@common/types/mission-types'
import * as Sentry from '@sentry/react'
import { ExportMode, ExportReportType } from '../types/mission-export-types.ts'

export type MissionPatrolExportMutationArgs = {
  missionIds?: number[]
  exportMode: ExportMode
  reportType: ExportReportType
}

export const EXPORT_MISSION_REPORTS: DocumentNode = gql`
  query ExportMissionReports($missionIds: [ID], $exportMode: ExportMode, $reportType: ExportReportType) {
    exportMissionReports(missionIds: $missionIds, exportMode: $exportMode, reportType: $reportType) {
      fileName
      fileContent
    }
  }
`

type LazyQueryExecFunction<TData, TVariables extends OperationVariables> = (
  options?: Partial<LazyQueryHookExecOptions<TData, TVariables>>
) => Promise<QueryResult<TData, TVariables>>

// Use this hook to download files like Rapport de Patrouille or Tableaux AEM
export const useLazyExportMissionReports = (): LazyQueryResultTuple<MissionExport, MissionPatrolExportMutationArgs> => {
  const [execute, result] = useLazyQuery<MissionExport, MissionPatrolExportMutationArgs>(EXPORT_MISSION_REPORTS)

  const getMissionReport: LazyQueryExecFunction<MissionExport, MissionPatrolExportMutationArgs> = async options => {
    try {
      return await execute({ ...options })
    } catch (error) {
      console.error('Mission export error: ', error)
      Sentry.captureException(error)
      throw error
    }
  }

  return [getMissionReport, result]
}
