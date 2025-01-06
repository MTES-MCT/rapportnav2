import { MissionEnvActionData, MissionFishActionData, MissionNavActionData } from '../../common/types/mission-action'
import { MissionActionData } from '../../common/types/mission-action-data'
import { RescueType } from '../../common/types/rescue-type'

export type ActionGenericDateObservation = {
  id: string
  startDateTimeUtc: string
  endDateTimeUtc: string
  observations: string
}

export type ActionGenericDateObservationInput = {
  dates: Date[]
} & MissionActionData

export type ActionAntiPollutionInput = {
  dates: Date[]
  geoCoords: (number | undefined)[]
} & MissionNavActionData

export type ActionIllegalImmigrationInput = {
  dates: Date[]
  isMissionFinished?: boolean
  geoCoords: (number | undefined)[]
} & MissionNavActionData

export type ActionFreeNoteInput = {
  date: Date
} & MissionNavActionData

export type ActionRescueInput = {
  dates: Date[]
  rescueType: RescueType
  isMissionFinished?: boolean
  geoCoords: (number | undefined)[]
} & MissionNavActionData

export type ActionStatusInput = {
  date: Date
} & MissionNavActionData

export type ActionSurveillanceInput = {
  dates: Date[]
} & MissionEnvActionData

export type ActionNavControlInput = {
  dates: Date[]
  isMissionFinished: boolean
  geoCoords: (number | undefined)[]
} & MissionNavActionData

export type ActionFishControlInput = {
  dates: Date[]
  isMissionFinished: boolean
  geoCoords: (number | undefined)[]
} & MissionFishActionData

export type ActionEnvControlInput = {
  dates: Date[]
  isMissionFinished: boolean
  geoCoords: [number?, number?]
} & MissionEnvActionData
