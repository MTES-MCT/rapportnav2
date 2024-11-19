import {
  ControlAdministrative,
  ControlGensDeMer,
  ControlNavigation,
  ControlSecurity
} from '@common/types/control-types'

export interface MissionActionDataOutput {
  startDateTimeUtc: string
  endDateTimeUtc: string
  controlSecurity: ControlSecurity
  controlGensDeMer: ControlGensDeMer
  controlNavigation: ControlNavigation
  controlAdministrative: ControlAdministrative
}
