import {
  ActionAntiPollution,
  ActionControl,
  ActionFreeNote,
  ActionIllegalImmigration,
  ActionRescue,
  ActionStatus
} from '@common/types/action-types'
import { EnvActionControl, EnvActionSurveillance } from '@common/types/env-mission-types'
import { FishAction } from '@common/types/fish-mission-types'
import { RescueType } from '../../common/types/rescue-type'

export type ActionGenericDateObservation = {
  id: string
  startDateTimeUtc: string
  endDateTimeUtc: string
  observations: string
}

export type ActionGenericDateObservationInput = {
  dates: Date[]
} & ActionGenericDateObservation

export type ActionAntiPollutionInput = {
  dates: Date[]
  geoCoords: (number | undefined)[]
} & ActionAntiPollution

export type ActionIllegalImmigrationInput = {
  dates: Date[]
  isMissionFinished?: boolean
  geoCoords: (number | undefined)[]
} & ActionIllegalImmigration

export type ActionFreeNoteInput = {
  date: Date
} & ActionFreeNote

export type ActionRescueInput = {
  dates: Date[]
  rescueType: RescueType
  isMissionFinished?: boolean
  geoCoords: (number | undefined)[]
} & ActionRescue

export type ActionStatusInput = {
  date: Date
} & ActionStatus

export type ActionSurveillanceInput = {
  dates: Date[]
} & EnvActionSurveillance

export type ActionNavControlInput = {
  dates: Date[]
  isMissionFinished: boolean
  geoCoords: (number | undefined)[]
} & ActionControl

export type ActionFishControlInput = {
  dates: Date[]
  isMissionFinished: boolean
  geoCoords: (number | undefined)[]
} & FishAction

export type ActionEnvControlInput = {
  dates: Date[]
  isMissionFinished: boolean
  geoCoords: [number?, number?]
} & EnvActionControl
