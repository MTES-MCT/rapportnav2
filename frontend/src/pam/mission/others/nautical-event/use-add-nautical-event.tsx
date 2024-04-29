import { gql, MutationTuple, useMutation } from '@apollo/client'
import { useParams } from 'react-router-dom'
import { ActionNauticalEvent } from '../../../../types/action-types.ts'
import { GET_MISSION_TIMELINE } from '../../timeline/use-mission-timeline.tsx'
import { GET_ACTION_BY_ID } from '../../actions/use-action-by-id.tsx'

export const MUTATION_ADD_UPDATE_ACTION_NAUTICAL_EVENT = gql`
  mutation AddOrUpdateNauticalEvent($nauticalAction: ActionNauticalEventInput!) {
      addOrUpdateActionNauticalEvent(nauticalAction: $nauticalAction) {
        id
        startDateTimeUtc
        endDateTimeUtc
        observations
      }
    }`

const useAddOrUpdateNauticalEvent = (): MutationTuple<ActionNauticalEvent, Record<string, any>> => {
  const {missionId} = useParams()
  const mutation = useMutation(MUTATION_ADD_UPDATE_ACTION_NAUTICAL_EVENT, {
    refetchQueries: [
      {query: GET_MISSION_TIMELINE, variables: {missionId}},
      GET_ACTION_BY_ID
    ]
  })

  return mutation
}

export default useAddOrUpdateNauticalEvent
