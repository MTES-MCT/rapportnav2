import { gql, MutationTuple, useMutation } from '@apollo/client'
import { useParams } from 'react-router-dom'
import { GET_MISSION_TIMELINE } from '../../timeline/use-mission-timeline.tsx'
import { GET_ACTION_BY_ID } from '../../actions/use-action-by-id.tsx'
import { ActionPublicOrder } from '../../../../types/action-types.ts'

export const MUTATION_ADD_UPDATE_ACTION_PUBLIC_ORDER = gql`
  mutation AddOrUpdateActionPublicOrder($publicOrderAction: ActionPublicOrderInput!) {
      addOrUpdateActionPublicOrder(publicOrderAction: $publicOrderAction) {
        id
        startDateTimeUtc
        endDateTimeUtc
        observations
      }
    }`

const useAddOrUpdatePublicOrder = (): MutationTuple<ActionPublicOrder, Record<string, any>> => {
  const {missionId} = useParams()
  const mutation = useMutation(MUTATION_ADD_UPDATE_ACTION_PUBLIC_ORDER, {
    refetchQueries: [
      {query: GET_MISSION_TIMELINE, variables: {missionId}},
      GET_ACTION_BY_ID
    ]
  })

  return mutation
}

export default useAddOrUpdatePublicOrder
