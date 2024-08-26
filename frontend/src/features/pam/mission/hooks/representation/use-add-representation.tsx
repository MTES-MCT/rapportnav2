import { gql, MutationTuple, useMutation } from '@apollo/client'
import { useParams } from 'react-router-dom'
import { ActionNauticalEvent, ActionRepresentation } from '../../../../common/types/action-types.ts'
import { GET_MISSION_TIMELINE } from '../use-mission-timeline.tsx'
import { GET_ACTION_BY_ID } from '../use-action-by-id.tsx'

export const MUTATION_ADD_UPDATE_ACTION_REPRESENTATION = gql`
  mutation AddOrUpdateRepresentation($representationAction: ActionRepresentationInput!) {
    addOrUpdateActionRepresentation(representationAction: $representationAction) {
      id
      startDateTimeUtc
      endDateTimeUtc
      observations
    }
  }
`

const useAddOrUpdateRepresentation = (): MutationTuple<ActionRepresentation, Record<string, any>> => {
  const { missionId } = useParams()
  const mutation = useMutation(MUTATION_ADD_UPDATE_ACTION_REPRESENTATION, {
    refetchQueries: [{ query: GET_MISSION_TIMELINE, variables: { missionId } }, GET_ACTION_BY_ID]
  })

  return mutation
}

export default useAddOrUpdateRepresentation
