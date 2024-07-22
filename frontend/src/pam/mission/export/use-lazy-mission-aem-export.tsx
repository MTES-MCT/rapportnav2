import {
  gql,
  LazyQueryResultTuple,
  DocumentNode,
  OperationVariables,
  useLazyQuery,
  QueryResult,
  LazyQueryHookExecOptions
} from '@apollo/client'
import * as Sentry from '@sentry/react'
import { MissionExport } from '../../../types/mission-types.ts'

export const GET_MISSION_AEM_EXPORT: DocumentNode = gql`
  query GetMissionAEMExport($missionId: ID) {
    exportAEM(missionId: $missionId) {
      fileName
      fileContent
    }
  }
`

type LazyQueryExecFunction<TData, TVariables extends OperationVariables> = (
  options?: Partial<LazyQueryHookExecOptions<TData, TVariables>>
) => Promise<QueryResult<TData, TVariables>>

const useLazyMissionAEMExport = (): LazyQueryResultTuple<MissionExport, { missionId?: string }> => {
  const [execute, result] = useLazyQuery<MissionExport, { missionId?: string }>(GET_MISSION_AEM_EXPORT)

  const getMissionAEMReport: LazyQueryExecFunction<MissionExport, { missionId?: string }> = async options => {
    try {
      return await execute({ ...options, fetchPolicy: 'cache-and-network' })
    } catch (error) {
      console.error('Mission AEM export error: ', error)
      Sentry.captureException(error)
      throw error
    }
  }

  return [getMissionAEMReport, result]
}

export default useLazyMissionAEMExport
