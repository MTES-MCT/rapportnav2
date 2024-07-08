import { gql, MutationTuple, useMutation } from '@apollo/client'
import { useParams } from 'react-router-dom'
import { GET_MISSION_EXCERPT } from './general-info/use-mission-excerpt'

export const MUTATION_PATCH_MISSION_ENV = gql`
  mutation patchMissionEnv($mission: MissionEnvInput!) {
    patchMissionEnv(mission: $mission) {
      id
    }
  }
`

export type PatchMissionEnvInput = {
  missionId: number
  observationByUnit?: string
}

const usePatchMissionEnv = (): MutationTuple<void, Record<string, any>> => {
  const { missionId } = useParams()
  const mutation = useMutation(MUTATION_PATCH_MISSION_ENV, {
    refetchQueries: [{ query: GET_MISSION_EXCERPT, variables: { missionId } }]
  })
  return mutation
}

export default usePatchMissionEnv
