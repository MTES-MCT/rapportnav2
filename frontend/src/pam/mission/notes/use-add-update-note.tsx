import { gql, MutationTuple, useMutation } from '@apollo/client'
import { GET_MISSION_TIMELINE } from "../timeline/use-mission-timeline.tsx";
import { GET_ACTION_BY_ID } from "../actions/use-action-by-id.tsx";
import { ActionStatus } from "../../../types/action-types.ts";

export const MUTATION_ADD_OR_UPDATE_ACTION_NOTE = gql`
  mutation AddOrUpdateNote($noteAction: ActionFreeNoteInput!) {
    addOrUpdateFreeNote(noteAction: $noteAction) {
      id
      startDateTimeUtc
      observations
    }
  }
`


const useAddOrUpdateNote = (): MutationTuple<ActionStatus, Record<string, any>> => {
  const mutation = useMutation(MUTATION_ADD_OR_UPDATE_ACTION_NOTE, {
    refetchQueries: [GET_MISSION_TIMELINE, GET_ACTION_BY_ID]
  })

  return mutation
}

export default useAddOrUpdateNote
