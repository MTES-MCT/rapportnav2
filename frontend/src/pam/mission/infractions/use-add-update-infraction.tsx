import { gql, MutationTuple, useMutation } from '@apollo/client'
import { GET_MISSION_TIMELINE } from "../timeline/use-mission-timeline.tsx";
import { GET_ACTION_BY_ID } from "../actions/use-action-by-id.tsx";
import { Infraction } from "../../../types/infraction-types.ts";
import { useParams } from "react-router-dom";

export const MUTATION_ADD_OR_UPDATE_INFRACTION = gql`
  mutation AddOrUpdateInfraction($infraction: InfractionInput!) {
    addOrUpdateInfraction(infraction: $infraction) {
      id
      controlType
      infractionType
      natinfs
      observations
    }
  }
`


const useAddOrUpdateInfraction = (): MutationTuple<Infraction[], Record<string, any>> => {
  const {missionId} = useParams()
  const mutation = useMutation(MUTATION_ADD_OR_UPDATE_INFRACTION, {
    refetchQueries: [
      {query: GET_MISSION_TIMELINE, variables: {missionId}},
      GET_ACTION_BY_ID
    ]
  })

  return mutation
}

export default useAddOrUpdateInfraction
