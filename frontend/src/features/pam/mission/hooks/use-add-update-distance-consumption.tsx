import { gql, MutationTuple, useMutation } from '@apollo/client'
import { useParams } from 'react-router-dom'
import { MissionGeneralInfo } from '../../../common/types/mission-types.ts'
import { GET_MISSION_TIMELINE } from './use-mission-timeline.tsx'
import { GET_MISSION_EXCERPT } from './use-mission-excerpt.tsx'

export const MUTATION_ADD_OR_UPDATE_GENERAL_INFO = gql`
  mutation UpdateMissionGeneralInfo($info: MissionGeneralInfoInput!) {
    updateMissionGeneralInfo(info: $info) {
      id
      distanceInNauticalMiles
      consumedGOInLiters
      consumedFuelInLiters
      nbrOfRecognizedVessel
    }
  }
`

const useAddOrUpdateGeneralInfo = (): MutationTuple<MissionGeneralInfo, Record<string, any>> => {
  const { missionId } = useParams()
  const mutation = useMutation(MUTATION_ADD_OR_UPDATE_GENERAL_INFO, {
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

export default useAddOrUpdateGeneralInfo
