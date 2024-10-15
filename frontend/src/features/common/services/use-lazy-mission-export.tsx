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

export const GET_MISSION_EXPORT: DocumentNode = gql`
  query GetMissionExport($missionId: ID) {
    missionExport(missionId: $missionId) {
      fileName
      fileContent
    }
  }
`

type LazyQueryExecFunction<TData, TVariables extends OperationVariables> = (
  options?: Partial<LazyQueryHookExecOptions<TData, TVariables>>
) => Promise<QueryResult<TData, TVariables>>

export const useLazyMissionExportMutation = (): LazyQueryResultTuple<MissionExport, { missionId?: string }> => {
  const [execute, result] = useLazyQuery<MissionExport, { missionId?: string }>(GET_MISSION_EXPORT)

  const getMissionReport: LazyQueryExecFunction<MissionExport, { missionId?: string }> = async options => {
    try {
      return await execute({ ...options, fetchPolicy: 'cache-and-network' })
    } catch (error) {
      console.error('Mission export error: ', error)
      Sentry.captureException(error)
      throw error
    }
  }

  return [getMissionReport, result]
}
