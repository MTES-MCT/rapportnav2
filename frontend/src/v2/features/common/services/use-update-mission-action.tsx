import { gql, MutationTuple, useMutation } from '@apollo/client'
import { GET_ACTION } from './use-mission-action'
import { GET_ACTION_LIST } from './use-mission-action-list'

export const MUTATION_UPDATE_MISSION_ACTION = gql`
  mutation UpdateMissionAction($input: MissionActionInput!) {
    updateMissionAction(action: $input) {
      id
      missionId
      source
      summaryTags
      actionType
      status
      controlsToComplete
      isCompleteForStats
      completenessForStats {
        status
        sources
      }
      sourcesOfMissingDataForStats
    }
  }
`

const useUpdateMissionActionMutation = (
  actionId: string,
  missionId?: number
): MutationTuple<unknown, Record<string, any>> => {
  const mutation = useMutation(MUTATION_UPDATE_MISSION_ACTION, {
    refetchQueries: [
      { query: GET_ACTION_LIST, variables: { missionId } },
      { query: GET_ACTION, variables: { actionId, missionId } }
    ]
  })

  return mutation
}

export default useUpdateMissionActionMutation
