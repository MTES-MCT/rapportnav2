import { gql, MutationTuple, useMutation } from '@apollo/client'
import { GET_MISSION_TIMELINE } from "../timeline/use-mission-timeline.tsx";
import { GET_ACTION_BY_ID } from "../actions/use-action-by-id.tsx";

export const DELETE_ACTION_NOTE = gql`
    mutation DeleteFreeNote($id: String!) {
        deleteFreeNote(id: $id)
    }
`

const useDeleteNote = (): MutationTuple<boolean, Record<string, any>> => {
    const mutation = useMutation(DELETE_ACTION_NOTE, {
        refetchQueries: [GET_MISSION_TIMELINE, GET_ACTION_BY_ID]
    })

    return mutation
}

export default useDeleteNote
