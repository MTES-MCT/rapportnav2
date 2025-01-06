import {
  ControlAdministrative,
  ControlGensDeMer,
  ControlNavigation,
  ControlSecurity
} from '@common/types/control-types'

export interface MissionActionData {
  startDateTimeUtc?: string
  endDateTimeUtc?: string
  observations?: string
  longitude?: number
  latitude?: number
  controlSecurity?: ControlSecurity
  controlGensDeMer?: ControlGensDeMer
  controlNavigation?: ControlNavigation
  controlAdministrative?: ControlAdministrative
}
