import { gql, MutationTuple, useMutation } from '@apollo/client'
import { useParams } from 'react-router-dom'
import { GET_MISSION_TIMELINE } from '../use-mission-timeline.tsx'
import { GET_ACTION_BY_ID } from '../use-action-by-id.tsx'

export const DELETE_ACTION_ILLEGAL_IMMIGRATION = gql`
  mutation DeleteIllegalImmigration($id: String!) {
    deleteIllegalImmigration(id: $id)
  }
`

const useIllegalImmigration = (): MutationTuple<boolean, Record<string, any>> => {
  const { missionId } = useParams()
  const mutation = useMutation(DELETE_ACTION_ILLEGAL_IMMIGRATION, {
    refetchQueries: [{ query: GET_MISSION_TIMELINE, variables: { missionId } }, GET_ACTION_BY_ID]
  })

  return mutation
}

export default useIllegalImmigration
