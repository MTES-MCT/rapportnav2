import { MissionEnvActionData, MissionFishActionData, MissionNavActionData } from '../../common/types/mission-action'
import { MissionActionData } from '../../common/types/mission-action-data'
import { RescueType } from '../../common/types/rescue-type'

export type ActionGenericDateObservation = {
  id: string
  startDateTimeUtc: string
  endDateTimeUtc: string
  observations: string
}

export type ActionGenericDateObservationInput =  MissionActionData

export type ActionAntiPollutionInput = {
  geoCoords: (number | undefined)[]
} & MissionNavActionData

export type ActionIllegalImmigrationInput = {
  isMissionFinished?: boolean
  geoCoords: (number | undefined)[]
} & MissionNavActionData

export type ActionFreeNoteInput = MissionNavActionData

export type ActionRescueInput = {
  rescueType: RescueType
  isMissionFinished?: boolean
  geoCoords: (number | undefined)[]
} & MissionNavActionData

export type ActionStatusInput = MissionNavActionData

export type ActionSurveillanceInput = MissionEnvActionData

export type ActionNavControlInput = {
  isMissionFinished: boolean
  geoCoords: (number | undefined)[]
} & MissionNavActionData

export type ActionFishControlInput = {
  isMissionFinished: boolean
  geoCoords: (number | undefined)[]
} & MissionFishActionData

export type ActionEnvControlInput = {
  isMissionFinished: boolean
  geoCoords: [number?, number?]
} & MissionEnvActionData
