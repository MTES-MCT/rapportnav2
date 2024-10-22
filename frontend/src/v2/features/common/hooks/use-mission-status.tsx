import { MissionStatusEnum } from '@common/types/mission-types'
import { Icon, IconProps, THEME } from '@mtes-mct/monitor-ui'
import { FunctionComponent } from 'react'

type Component = {
  text: string
  color: string
  icon: FunctionComponent<IconProps>
}

type StatusComponent = { [key in MissionStatusEnum]: Component }

const MISSION_STATUS: StatusComponent = {
  [MissionStatusEnum.UPCOMING]: {
    text: 'À venir',
    icon: Icon.ClockDashed,
    color: THEME.color.babyBlueEyes
  },
  [MissionStatusEnum.IN_PROGRESS]: {
    text: 'En cours',
    icon: Icon.Clock,
    color: THEME.color.blueGray
  },
  [MissionStatusEnum.ENDED]: {
    text: 'Terminée',
    icon: Icon.Confirm,
    color: THEME.color.charcoal
  },
  [MissionStatusEnum.UNAVAILABLE]: {
    text: 'Indisponible',
    icon: Icon.Close,
    color: THEME.color.maximumRed
  }
}

export function useMissionStatus(missionStatus: MissionStatusEnum): Component {
  return MISSION_STATUS[missionStatus]
}
