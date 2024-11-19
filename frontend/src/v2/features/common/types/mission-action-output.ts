import { ActionStatusType } from '@common/types/action-types'
import {
  ControlAdministrative,
  ControlGensDeMer,
  ControlNavigation,
  ControlSecurity,
  ControlType
} from '@common/types/control-types'
import { ActionTypeEnum, MissionSourceEnum } from '@common/types/env-mission-types'
import { CompletenessForStats } from '@common/types/mission-types'

export interface MissionActionDataOutput {
  startDateTimeUtc?: string
  endDateTimeUtc?: string
  observations?: string
  controlSecurity?: ControlSecurity
  controlGensDeMer?: ControlGensDeMer
  controlNavigation?: ControlNavigation
  controlAdministrative?: ControlAdministrative
}

export interface MissionActionOutput {
  id: string
  missionId: number
  source: MissionSourceEnum
  actionType: ActionTypeEnum
  status: ActionStatusType
  summaryTags: string[]
  isCompleteForStats: boolean
  controlsToComplete: ControlType[]
  completenessForStats: CompletenessForStats
  sourcesOfMissingDataForStats: MissionSourceEnum[]
  data: MissionActionDataOutput
}
