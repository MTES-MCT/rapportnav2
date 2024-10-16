import { gql, MutationTuple, useMutation } from '@apollo/client'
import { ActionTypeEnum } from '@common/types/env-mission-types.ts'

interface ActionDataInput {
  data: unknown
  missionId: number
  type: ActionTypeEnum
}

export const MUTATION_ADD_UPDATE_ULAM_ACTION = gql`
  mutation AddOrUpdateAction($action: ActionDataInput!) {
    addOrUpdateAction(action: $action) {
      id
      startDateTimeUtc
      endDateTimeUtc
      observations
      latitude
      longitude
      nbOfInterceptedVessels
      nbOfInterceptedMigrants
      nbOfSuspectedSmugglers
    }
  }
`

export const useAddOrUpdateActionMutation = (): MutationTuple<ActionDataInput, Record<string, any>> => {
  const mutation = useMutation(MUTATION_ADD_UPDATE_ULAM_ACTION, {
    //refetchQueries: [{ query: GET_MISSION_TIMELINE, variables: { missionId } }, GET_ACTION_BY_ID]
  })

  return mutation
}
