import { Infraction } from './infraction-types'

export enum ControlMethod {
  'SEA' = 'SEA',
  'AIR' = 'AIR',
  'LAND' = 'LAND'
}

export enum ControlResult {
  'YES' = 'YES',
  'NO' = 'NO',
  'NOT_CONTROLLED' = 'NOT_CONTROLLED',
  'NOT_CONCERNED' = 'NOT_CONCERNED'
}

export enum ControlType {
  'ADMINISTRATIVE' = 'ADMINISTRATIVE',
  'GENS_DE_MER' = 'GENS_DE_MER',
  'SECURITY' = 'SECURITY',
  'NAVIGATION' = 'NAVIGATION'
}

type ControlModel = {
  id: string
  amountOfControls: number
  unitShouldConfirm?: boolean
  unitHasConfirmed?: boolean
  observations?: string
  infractions?: Infraction[]
}

export type ControlAdministrative = ControlModel & {
  compliantOperatingPermit?: ControlResult
  upToDateNavigationPermit?: ControlResult
  compliantSecurityDocuments?: ControlResult
}
export type ControlGensDeMer = ControlModel & {
  staffOutnumbered?: ControlResult
  upToDateMedicalCheck?: ControlResult
  knowledgeOfFrenchLawAndLanguage?: ControlResult
}
export type ControlNavigation = ControlModel
export type ControlSecurity = ControlModel
