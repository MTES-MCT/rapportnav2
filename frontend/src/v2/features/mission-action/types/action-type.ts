import { MissionEnvActionData, MissionFishActionData, MissionNavActionData } from '../../common/types/mission-action'
import { MissionActionData } from '../../common/types/mission-action-data'
import { RescueType } from '../../common/types/rescue-type'

export type ActionGenericDateObservationInput = {
  dates: [Date?, Date?]
} & MissionActionData

export type ActionAntiPollutionInput = {
  dates: [Date?, Date?]
  geoCoords: (number | undefined)[]
} & MissionNavActionData

export type ActionIllegalImmigrationInput = {
  dates: [Date?, Date?]
  geoCoords: (number | undefined)[]
} & MissionNavActionData

export type ActionFreeNoteInput = { date?: Date } & MissionNavActionData

export type ActionRescueInput = {
  dates: [Date?, Date?]
  rescueType: RescueType
  geoCoords: (number | undefined)[]
} & MissionNavActionData

export type ActionStatusInput = MissionNavActionData

export type ActionSurveillanceInput = { dates: [Date?, Date?] } & MissionEnvActionData

export type ActionNavControlInput = {
  dates: [Date?, Date?]
  geoCoords: (number | undefined)[]
} & MissionNavActionData

export type ActionFishControlInput = {
  dates: [Date?, Date?]
  geoCoords: (number | undefined)[]
} & MissionFishActionData

export type ActionEnvControlInput = {
  dates: [Date?, Date?]
  geoCoords: [number?, number?]
} & MissionEnvActionData

export type ActionInquiryInput = {} & MissionNavActionData

export type ActionControlInput = {
  dates: [Date?, Date?]
  geoCoords: [number?, number?]
} & MissionNavActionData
