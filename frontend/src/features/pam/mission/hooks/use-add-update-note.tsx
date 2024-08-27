import { gql, MutationTuple, useMutation } from '@apollo/client'
import { GET_MISSION_TIMELINE } from './use-mission-timeline.tsx'
import { GET_ACTION_BY_ID } from './use-action-by-id.tsx'
import { ActionStatus } from '../../../common/types/action-types.ts'
import { useParams } from 'react-router-dom'

export const MUTATION_ADD_OR_UPDATE_ACTION_NOTE = gql`
  mutation AddOrUpdateNote($freeNoteAction: ActionFreeNoteInput!) {
    addOrUpdateFreeNote(freeNoteAction: $freeNoteAction) {
      id
      startDateTimeUtc
      observations
    }
  }
`

const useAddOrUpdateNote = (): MutationTuple<ActionStatus, Record<string, any>> => {
  const { missionId } = useParams()
  const mutation = useMutation(MUTATION_ADD_OR_UPDATE_ACTION_NOTE, {
    refetchQueries: [{ query: GET_MISSION_TIMELINE, variables: { missionId } }, GET_ACTION_BY_ID]
  })

  return mutation
}

export default useAddOrUpdateNote
