import { gql, MutationTuple, useMutation } from '@apollo/client'
import { GET_MISSION_TIMELINE } from "../timeline/use-mission-timeline.tsx";
import { GET_ACTION_BY_ID } from "../actions/use-action-by-id.tsx";
import { ActionControl } from "../../../types/action-types.ts";

export const MUTATION_ADD_OR_UPDATE_ACTION_CONTROL = gql`
  mutation AddOrUpdateControl($controlAction: ActionControlInput!) {
    addOrUpdateControl(controlAction: $controlAction) {
      id
      startDateTimeUtc
      endDateTimeUtc
      latitude
      longitude
      controlMethod
      observations
      vesselIdentifier
      vesselType
      vesselSize
      identityControlledPerson
    }
  }
`


const useAddOrUpdateControl = (): MutationTuple<ActionControl, Record<string, any>> => {
  const mutation = useMutation(
    MUTATION_ADD_OR_UPDATE_ACTION_CONTROL,
    {
      refetchQueries: [GET_MISSION_TIMELINE, GET_ACTION_BY_ID]
    }
  )

  return mutation
}

export default useAddOrUpdateControl
