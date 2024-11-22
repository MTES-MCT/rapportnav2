import { gql, MutationTuple, useMutation } from '@apollo/client'
import { ControlMethod } from '@common/types/control-types'
import { VesselSizeEnum, VesselTypeEnum } from '@common/types/env-mission-types'
import { useParams } from 'react-router-dom'

interface ControlData {
  id: string
  startDateTimeUtc: string
  endDateTimeUtc: string
  observations: string
  latitude: number
  longitude: number
  controlMethod: ControlMethod
  vesselIdentifier: string
  vesselType: VesselTypeEnum
  vesselSize: VesselSizeEnum
  identityControlledPerson: string
}

export const MUTATION_ADD_OR_UPDATE_ACTION_CONTROL = gql`
  mutation AddOrUpdateControl($controlAction: ActionControlInput!) {
    addOrUpdateControl(controlAction: $controlAction) {
      id
      startDateTimeUtc
      endDateTimeUtc
      latitude
      longitude
      controlMethod
      observations
      vesselIdentifier
      vesselType
      vesselSize
      identityControlledPerson
    }
  }
`

const useAddOrUpdateControlMutation = (): MutationTuple<ControlData, Record<string, any>> => {
  const { missionId } = useParams()
  const mutation = useMutation(MUTATION_ADD_OR_UPDATE_ACTION_CONTROL, {
    //refetchQueries: [{ query: GET_MISSION_TIMELINE, variables: { missionId } }, GET_ACTION_BY_ID]
  })

  return mutation
}

export default useAddOrUpdateControlMutation
