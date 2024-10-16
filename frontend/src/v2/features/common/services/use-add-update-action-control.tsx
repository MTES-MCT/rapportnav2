import { gql, MutationTuple, useMutation } from '@apollo/client'
import { useParams } from 'react-router-dom'
import { ActionControl } from '../../../common/types/action-types.ts'

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

const useAddOrUpdateControlMutation = (): MutationTuple<ActionControl, Record<string, any>> => {
  const { missionId } = useParams()
  const mutation = useMutation(MUTATION_ADD_OR_UPDATE_ACTION_CONTROL, {
    //refetchQueries: [{ query: GET_MISSION_TIMELINE, variables: { missionId } }, GET_ACTION_BY_ID]
  })

  return mutation
}

export default useAddOrUpdateControlMutation
