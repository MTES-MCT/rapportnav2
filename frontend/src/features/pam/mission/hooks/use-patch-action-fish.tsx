import { gql, MutationTuple, useMutation } from '@apollo/client'
import { useParams } from 'react-router-dom'
import { GET_MISSION_TIMELINE } from './use-mission-timeline.tsx'
import { GET_ACTION_BY_ID } from './use-action-by-id.tsx'

export const MUTATION_PATCH_ACTION_FISH = gql`
  mutation PatchActionFish($action: ActionFishInput!) {
    patchActionFish(action: $action) {
      id
      startDateTimeUtc
      endDateTimeUtc
      observationsByUnit
    }
  }
`

export type PatchActionFishInput = {
  missionId?: number
  actionId?: string
  observationsByUnit?: string
  startDateTimeUtc?: string
  endDateTimeUtc?: string
}

const usePatchActionFish = (): MutationTuple<void, Record<string, any>> => {
  const { missionId } = useParams()

  const mutation = useMutation(MUTATION_PATCH_ACTION_FISH, {
    refetchQueries: [{ query: GET_MISSION_TIMELINE, variables: { missionId } }, GET_ACTION_BY_ID]
  })
  return mutation
}

export default usePatchActionFish
