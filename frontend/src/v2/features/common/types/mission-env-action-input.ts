import { InfractionByTarget } from '@common/types/infraction-types'
import { MissionActionData } from './mission-action-data'

export interface MissionEnvActionDataInput extends MissionActionData {
  infractions: InfractionByTarget[]
}
