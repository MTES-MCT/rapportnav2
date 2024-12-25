import { ActionStatusType } from '@common/types/action-types'
import { ControlType } from '@common/types/control-types'
import { ActionTypeEnum, MissionSourceEnum } from '@common/types/env-mission-types'
import { CompletenessForStats } from '@common/types/mission-types'
import { MissionActionData } from './mission-action-data'

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
  data: MissionActionData
}
