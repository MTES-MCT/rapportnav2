import { gql, MutationTuple, useMutation } from '@apollo/client'
import { Mission } from '../../types/mission-types.ts'

export const MUTATION_UPDATE_ENV_MISSION = gql`
  mutation UpdateEnvMissionInfo($data: EnvMissionUpdateInput!) {
    updateEnvMissionInfo(data: $data) {
      id
      startDateTimeUtc
      endDateTimeUtc
    }
  }
`

export type EnvMissionUpdateInput = {
  id: number
  startDateTimeUtc: string
  endDateTimeUtc?: string
}

const useUpdateEnvMission = (): MutationTuple<Mission, Record<string, any>> => {
  const mutation = useMutation(MUTATION_UPDATE_ENV_MISSION, {})

  return mutation
}

export default useUpdateEnvMission
