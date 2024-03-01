import { gql, MutationTuple, useMutation } from '@apollo/client'
import { GET_MISSION_TIMELINE } from "../timeline/use-mission-timeline.tsx";
import { GET_ACTION_BY_ID } from "../actions/use-action-by-id.tsx";
import { useParams } from "react-router-dom";

export const MUTATION_DELETE_INFRACTION = gql`
  mutation DeleteInfraction($id: String!) {
    deleteInfraction(id: $id)
  }
`

const useDeleteInfraction = (): MutationTuple<boolean, Record<string, any>> => {
  const {missionId} = useParams()
  const mutation = useMutation(MUTATION_DELETE_INFRACTION, {
    refetchQueries: [
      {query: GET_MISSION_TIMELINE, variables: {missionId}},
      GET_ACTION_BY_ID
    ]
  })

  return mutation
}

export default useDeleteInfraction
