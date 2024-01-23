import { gql, MutationTuple, useMutation } from '@apollo/client'
import { GET_MISSION_TIMELINE } from "../timeline/use-mission-timeline.tsx";
import { GET_ACTION_BY_ID } from "../actions/use-action-by-id.tsx";

export const MUTATION_DELETE_INFRACTION = gql`
    mutation DeleteInfraction($id: String!) {
        deleteInfraction(id: $id)
    }
`

const useDeleteInfraction = (): MutationTuple<boolean, Record<string, any>> => {
    const mutation = useMutation(MUTATION_DELETE_INFRACTION, {
        refetchQueries: [GET_MISSION_TIMELINE, GET_ACTION_BY_ID]
    })

    return mutation
}

export default useDeleteInfraction
