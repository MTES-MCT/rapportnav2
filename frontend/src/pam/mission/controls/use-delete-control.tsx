import { gql, MutationTuple, useMutation } from '@apollo/client'
import { GET_MISSION_TIMELINE } from "../timeline/use-mission-timeline.tsx";
import { GET_ACTION_BY_ID } from "../actions/use-action-by-id.tsx";

export const DELETE_ACTION_CONTROL = gql`
    mutation DeleteControl($id: String!) {
        deleteControl(id: $id)
    }
`

const useDeleteControl = (): MutationTuple<boolean, Record<string, any>> => {
    const mutation = useMutation(DELETE_ACTION_CONTROL, {
        refetchQueries: [GET_MISSION_TIMELINE, GET_ACTION_BY_ID]
    })

    return mutation
}

export default useDeleteControl
