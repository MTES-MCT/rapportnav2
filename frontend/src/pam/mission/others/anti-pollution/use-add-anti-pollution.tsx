import { gql, MutationTuple, useMutation } from '@apollo/client'
import { useParams } from 'react-router-dom'
import { GET_MISSION_TIMELINE } from '../../timeline/use-mission-timeline.tsx'
import { GET_ACTION_BY_ID } from '../../actions/use-action-by-id.tsx'
import { ActionAntiPollution } from '../../../../types/action-types.ts'

export const MUTATION_ADD_UPDATE_ACTION_ANTI_POLLUTION = gql`
  mutation AddOrUpdateActionAntiPollution($antiPollutionAction: ActionAntiPollutionInput!) {
    addOrUpdateActionAntiPollution(antiPollutionAction: $antiPollutionAction) {
      id
      startDateTimeUtc
      endDateTimeUtc
      observations
      latitude
      longitude
      detectedPollution
      pollutionObservedByAuthorizedAgent
      diversionCarriedOut
    }
  }
`

const useAddOrUpdateAntiPollution = (): MutationTuple<ActionAntiPollution, Record<string, any>> => {
  const { missionId } = useParams()
  const mutation = useMutation(MUTATION_ADD_UPDATE_ACTION_ANTI_POLLUTION, {
    refetchQueries: [{ query: GET_MISSION_TIMELINE, variables: { missionId } }, GET_ACTION_BY_ID]
  })

  return mutation
}

export default useAddOrUpdateAntiPollution
