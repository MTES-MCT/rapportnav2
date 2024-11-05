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

interface MissionExportAEMInput {
  ids: number[]
}
export const GET_MISSION_AEM_EXPORT: DocumentNode = gql`
  query GetMissionAEMExport($missionExportAEMExportInput: MissionAEMExportInput) {
    missionAEMExportV2(missionExportAEMExportInput: $missionExportAEMExportInput) {
      fileName
      fileContent
    }
  }
`

type LazyQueryExecFunction<TData, TVariables extends OperationVariables> = (
  options?: Partial<LazyQueryHookExecOptions<TData, TVariables>>
) => Promise<QueryResult<TData, TVariables>>

export const useLazyMissionAEMExportMutation = (): LazyQueryResultTuple<MissionExport, { missionExportAEMExportInput?: MissionExportAEMInput }> => {
  const [execute, result] = useLazyQuery<MissionExport, { missionExportAEMExportInput?: MissionExportAEMInput }>(GET_MISSION_AEM_EXPORT)

  const getMissionAEM: LazyQueryExecFunction<MissionExport, { missionExportAEMExportInput?: MissionExportAEMInput }> = async options => {
    try {
      return await execute({ ...options, fetchPolicy: 'cache-and-network' })
    } catch (error) {
      console.error('Mission AEM export error: ', error)
      Sentry.captureException(error)
      throw error
    }
  }

  return [getMissionAEM, result]
}
