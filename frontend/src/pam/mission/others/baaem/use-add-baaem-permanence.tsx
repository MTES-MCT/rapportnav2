import { gql, MutationTuple, useMutation } from '@apollo/client'
import { useParams } from "react-router-dom";
import { ActionBAAEMPermanence } from '../../../../types/action-types.ts'
import { GET_MISSION_TIMELINE } from '../../timeline/use-mission-timeline.tsx'
import { GET_ACTION_BY_ID } from '../../actions/use-action-by-id.tsx'

export const MUTATION_ADD_OR_UPDATE_ACTION_BAAEM_PERMANENCE = gql`
  mutation AddOrUpdateBAAEMPermanence($baaemPermanenceAction: ActionBAAEMPermanenceInput!) {
    addOrUpdateActionBAAEMPermanence(baaemPermanenceAction: $baaemPermanenceAction) {
      id
      startDateTimeUtc
      observations
    }
  }
`


const useAddOrUpdateBAAEMPermanence = (): MutationTuple<ActionBAAEMPermanence, Record<string, any>> => {
  const {missionId} = useParams()
  const mutation = useMutation(MUTATION_ADD_OR_UPDATE_ACTION_BAAEM_PERMANENCE, {
    refetchQueries: [
      {query: GET_MISSION_TIMELINE, variables: {missionId}},
      GET_ACTION_BY_ID
    ]
  })

  return mutation
}

export default useAddOrUpdateBAAEMPermanence
