import { gql, MutationTuple, useMutation } from '@apollo/client'
import { useParams } from 'react-router-dom'
import { GET_MISSION_TIMELINE } from '../timeline/use-mission-timeline.tsx'
import { GET_ACTION_BY_ID } from './use-action-by-id.tsx'

export const MUTATION_PATCH_ACTION_ENV = gql`
  mutation patchActionEnv($action: ActionEnvInput!) {
    patchActionEnv(action: $action) {
      id
      startDateTimeUtc
      endDateTimeUtc
      observationsByUnit
    }
  }
`

export type PatchActionEnvInput = {
  missionId?: string
  actionId?: string
  observationsByUnit?: string
  startDateTimeUtc?: string
  endDateTimeUtc?: string
}

const usePatchActionEnv = (): MutationTuple<void, Record<string, any>> => {
  const { missionId } = useParams()
  const mutation = useMutation(MUTATION_PATCH_ACTION_ENV, {
    refetchQueries: [{ query: GET_MISSION_TIMELINE, variables: { missionId } }, GET_ACTION_BY_ID]
  })
  return mutation
}

export default usePatchActionEnv
