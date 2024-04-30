import { gql, MutationTuple, useMutation } from '@apollo/client'
import { GET_MISSION_EXCERPT } from './use-mission-excerpt.tsx'
import { MissionGeneralInfo } from '../../../types/mission-types.ts'
import { useParams } from 'react-router-dom'
import { GET_MISSION_TIMELINE } from '../timeline/use-mission-timeline.tsx'

export const MUTATION_ADD_OR_UPDATE_DISTANCE_CONSUMPTION = gql`
  mutation UpdateMissionGeneralInfo($info: MissionGeneralInfoInput!) {
    updateMissionGeneralInfo(info: $info) {
      id
      distanceInNauticalMiles
      consumedGOInLiters
      consumedFuelInLiters
    }
  }
`

const useAddOrUpdateDistanceConsumption = (): MutationTuple<MissionGeneralInfo, Record<string, any>> => {
  const { missionId } = useParams()
  const mutation = useMutation(MUTATION_ADD_OR_UPDATE_DISTANCE_CONSUMPTION, {
    refetchQueries: [
      {
        query: GET_MISSION_EXCERPT,
        variables: { missionId }
      },
      {
        query: GET_MISSION_TIMELINE,
        variables: { missionId }
      }
    ]
  })

  return mutation
}

export default useAddOrUpdateDistanceConsumption
