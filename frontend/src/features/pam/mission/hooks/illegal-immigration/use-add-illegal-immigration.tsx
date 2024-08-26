import { gql, MutationTuple, useMutation } from '@apollo/client'
import { useParams } from 'react-router-dom'
import { GET_MISSION_TIMELINE } from '../use-mission-timeline.tsx'
import { GET_ACTION_BY_ID } from '../use-action-by-id.tsx'
import { ActionIllegalImmigration } from '../../../../common/types/action-types.ts'

export const MUTATION_ADD_UPDATE_ACTION_ILLEGAL_IMMIGRATION = gql`
  mutation AddOrUpdateActionIllegalImmigration($illegalImmigrationAction: ActionIllegalImmigrationInput!) {
    addOrUpdateActionIllegalImmigration(illegalImmigrationAction: $illegalImmigrationAction) {
      id
      startDateTimeUtc
      endDateTimeUtc
      observations
      latitude
      longitude
      nbOfInterceptedVessels
      nbOfInterceptedMigrants
      nbOfSuspectedSmugglers
    }
  }
`

const useAddOrUpdateIllegalImmigration = (): MutationTuple<ActionIllegalImmigration, Record<string, any>> => {
  const { missionId } = useParams()
  const mutation = useMutation(MUTATION_ADD_UPDATE_ACTION_ILLEGAL_IMMIGRATION, {
    refetchQueries: [{ query: GET_MISSION_TIMELINE, variables: { missionId } }, GET_ACTION_BY_ID]
  })

  return mutation
}

export default useAddOrUpdateIllegalImmigration
