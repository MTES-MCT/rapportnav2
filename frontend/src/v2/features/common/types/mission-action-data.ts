import { Target } from './target-types'

export interface MissionActionData {
  startDateTimeUtc?: string
  endDateTimeUtc?: string
  observations?: string
  longitude?: number
  latitude?: number
  targets?: Target[]
}
