import { gql, MutationTuple, useMutation } from '@apollo/client'
import { ActionRescue } from '../../../types/action-types.ts'
import { useParams } from 'react-router-dom'
import { GET_MISSION_TIMELINE } from '../timeline/use-mission-timeline.tsx'
import { GET_ACTION_BY_ID } from '../actions/use-action-by-id.tsx'

export const MUTATION_ADD_UPDATE_ACTION_RESCUE = gql`
  mutation AddOrUpdateRescue($rescueAction: ActionRescueInput!) {
    addOrUpdateActionRescue(rescueAction: $rescueAction) {
      id
      startDateTimeUtc
      endDateTimeUtc
      isVesselRescue
      isPersonRescue
      locationDescription
      observations
      longitude
      latitude
      isVesselNoticed
      isVesselTowed
      isInSRRorFollowedByCROSSMRCC
      numberPersonsRescued
      numberOfDeaths
      operationFollowsDEFREP
      isMigrationRescue
      nbAssistedVesselsReturningToShore
      nbOfVesselsTrackedWithoutIntervention
    }
  }
`

const useAddOrUpdateRescue = (): MutationTuple<ActionRescue, Record<string, any>> => {
  const { missionId } = useParams()
  const mutation = useMutation(MUTATION_ADD_UPDATE_ACTION_RESCUE, {
    refetchQueries: [{ query: GET_MISSION_TIMELINE, variables: { missionId } }, GET_ACTION_BY_ID]
  })

  return mutation
}

export default useAddOrUpdateRescue
