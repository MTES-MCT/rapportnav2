import { gql, MutationTuple, useMutation } from '@apollo/client'
import { useParams } from "react-router-dom";
import { GET_MISSION_TIMELINE } from '../../timeline/use-mission-timeline.tsx'
import { GET_ACTION_BY_ID } from '../../actions/use-action-by-id.tsx'

export const DELETE_ACTION_REPRESENTATION = gql`
  mutation DeleteRepresentation($id: String!) {
    deleteRepresentation(id: $id)
  }
`

const useRepresentation = (): MutationTuple<boolean, Record<string, any>> => {
  const {missionId} = useParams()
  const mutation = useMutation(DELETE_ACTION_REPRESENTATION, {
    refetchQueries: [
      {query: GET_MISSION_TIMELINE, variables: {missionId}},
      GET_ACTION_BY_ID]
  })

  return mutation
}

export default useRepresentation
