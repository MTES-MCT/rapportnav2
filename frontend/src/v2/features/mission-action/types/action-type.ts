import { MissionActionData } from '../../common/types/mission-action-data'
import { MissionEnvActionDataOutput } from '../../common/types/mission-env-action-output'
import { MissionFishActionDataOutput } from '../../common/types/mission-fish-action-output'
import { MissionNavActionDataOutput } from '../../common/types/mission-nav-action-output'
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
} & MissionNavActionDataOutput

export type ActionIllegalImmigrationInput = {
  dates: Date[]
  isMissionFinished?: boolean
  geoCoords: (number | undefined)[]
} & MissionNavActionDataOutput

export type ActionFreeNoteInput = {
  date: Date
} & MissionNavActionDataOutput

export type ActionRescueInput = {
  dates: Date[]
  rescueType: RescueType
  isMissionFinished?: boolean
  geoCoords: (number | undefined)[]
} & MissionNavActionDataOutput

export type ActionStatusInput = {
  date: Date
} & MissionNavActionDataOutput

export type ActionSurveillanceInput = {
  dates: Date[]
} & MissionEnvActionDataOutput

export type ActionNavControlInput = {
  dates: Date[]
  isMissionFinished: boolean
  geoCoords: (number | undefined)[]
} & MissionNavActionDataOutput

export type ActionFishControlInput = {
  dates: Date[]
  isMissionFinished: boolean
  geoCoords: (number | undefined)[]
} & MissionFishActionDataOutput

export type ActionEnvControlInput = {
  dates: Date[]
  isMissionFinished: boolean
  geoCoords: [number?, number?]
} & MissionEnvActionDataOutput
