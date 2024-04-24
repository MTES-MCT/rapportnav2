import { gql, MutationTuple, useMutation } from '@apollo/client'
import { useParams } from 'react-router-dom'
import { ActionNauticalEvent, ActionVigimer } from '../../../../types/action-types.ts'
import { GET_MISSION_TIMELINE } from '../../timeline/use-mission-timeline.tsx'
import { GET_ACTION_BY_ID } from '../../actions/use-action-by-id.tsx'

export const MUTATION_ADD_UPDATE_ACTION_VIGIMER = gql`
  mutation AddOrUpdateVigimer($vigimerAction: ActionVigimerInput!) {
      addOrUpdateActionVigimer(vigimerAction: $vigimerAction) {
        id
        startDateTimeUtc
        endDateTimeUtc
        observations
      }
    }`

const useAddOrUpdateVigimer = (): MutationTuple<ActionVigimer, Record<string, any>> => {
  const {missionId} = useParams()
  const mutation = useMutation(MUTATION_ADD_UPDATE_ACTION_VIGIMER, {
    refetchQueries: [
      {query: GET_MISSION_TIMELINE, variables: {missionId}},
      GET_ACTION_BY_ID
    ]
  })

  return mutation
}

export default useAddOrUpdateVigimer
