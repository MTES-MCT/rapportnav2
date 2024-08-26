import { gql, MutationTuple, useMutation } from '@apollo/client'
import { useParams } from 'react-router-dom'
import { GET_MISSION_TIMELINE } from '../use-mission-timeline.tsx'
import { GET_ACTION_BY_ID } from '../use-action-by-id.tsx'

export const DELETE_ACTION_RESCUE = gql`
  mutation DeleteRescue($id: String!) {
    deleteRescue(id: $id)
  }
`

const useDeleteRescue = (): MutationTuple<boolean, Record<string, any>> => {
  const { missionId } = useParams()
  const mutation = useMutation(DELETE_ACTION_RESCUE, {
    refetchQueries: [{ query: GET_MISSION_TIMELINE, variables: { missionId } }, GET_ACTION_BY_ID]
  })

  return mutation
}

export default useDeleteRescue
