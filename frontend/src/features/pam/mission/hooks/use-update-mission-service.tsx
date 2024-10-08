import { gql, MutationTuple, useMutation } from '@apollo/client'
import { useParams } from 'react-router-dom'
import { GET_MISSION_CREW } from './use-mission-crew.tsx'
import { GET_MISSION_EXCERPT } from './use-mission-excerpt.tsx'

export const MUTATION_UPDATE_MISSION_SERVICE = gql`
  mutation updateMissionService($service: MissionServiceInput!) {
    updateMissionService(service: $service) {
      id
      name
    }
  }
`

export type UpdateMissionServiceInput = {
  missionId: number
  serviceId: number
}

const useUpdateMissionService = (): MutationTuple<void, Record<string, any>> => {
  const { missionId } = useParams()
  const mutation = useMutation(MUTATION_UPDATE_MISSION_SERVICE, {
    refetchQueries: [
      { query: GET_MISSION_CREW, variables: { missionId } },
      { query: GET_MISSION_EXCERPT, variables: { missionId } }
    ]
  })

  return mutation
}

export default useUpdateMissionService
