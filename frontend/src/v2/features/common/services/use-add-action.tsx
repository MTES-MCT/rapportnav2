import { gql, MutationTuple, useMutation } from '@apollo/client'

interface ActionData {
  id: string
  startDateTimeUtc: string
  endDateTimeUtc: string
  observations: string
  latitude: number
  longitude: number
  nbOfInterceptedVessels: number
  nbOfInterceptedMigrants: number
  nbOfSuspectedSmugglers: number
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

export const useAddOrUpdateActionMutation = (): MutationTuple<ActionData, Record<string, any>> => {
  const mutation = useMutation(MUTATION_ADD_UPDATE_ULAM_ACTION, {
    //refetchQueries: [{ query: GET_MISSION_TIMELINE, variables: { missionId } }, GET_ACTION_BY_ID]
  })

  return mutation
}
