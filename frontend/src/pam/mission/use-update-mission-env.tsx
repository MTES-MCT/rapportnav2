import { gql, MutationTuple, useMutation } from '@apollo/client'
import { useParams } from 'react-router-dom'
import { GET_MISSION_EXCERPT } from './general-info/use-mission-excerpt'

export const MUTATION_UPDATE_MISSION_ENV = gql`
  mutation updateMissionEnv($mission: MissionEnvInput!) {
    updateMissionEnv(mission: $mission) {
      id
    }
  }
`

export type UpdateMissionEnvInput = {
  missionId: number
  observationByUnit?: string
}

const useUpdateMissionEnv = (): MutationTuple<void, Record<string, any>> => {
  const { missionId } = useParams()
  const mutation = useMutation(MUTATION_UPDATE_MISSION_ENV, {
    refetchQueries: [{ query: GET_MISSION_EXCERPT, variables: { missionId } }]
  })
  return mutation
}

export default useUpdateMissionEnv
