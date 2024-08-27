import { gql, MutationTuple, useMutation } from '@apollo/client'
import { GET_MISSION_TIMELINE } from './use-mission-timeline.tsx'
import { GET_ACTION_BY_ID } from './use-action-by-id.tsx'
import { InfractionByTarget } from '../../../common/types/infraction-types.ts'
import { useParams } from 'react-router-dom'

export const MUTATION_ADD_OR_UPDATE_INFRACTION_ENV = gql`
  mutation AddOrUpdateInfractionForEnvTarget($infraction: InfractionWithNewTargetInput!) {
    addOrUpdateInfractionForEnvTarget(infraction: $infraction) {
      id
      controlType
      infractionType
      natinfs
      observations
      target {
        id
        identityControlledPerson
        vesselIdentifier
        vesselSize
        vesselType
      }
    }
  }
`

const useAddOrUpdateInfractionEnv = (): MutationTuple<InfractionByTarget, Record<string, any>> => {
  const { missionId } = useParams()
  const mutation = useMutation(MUTATION_ADD_OR_UPDATE_INFRACTION_ENV, {
    refetchQueries: [{ query: GET_MISSION_TIMELINE, variables: { missionId } }, GET_ACTION_BY_ID]
  })

  return mutation
}

export default useAddOrUpdateInfractionEnv
