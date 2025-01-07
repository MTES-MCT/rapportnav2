import { Icon, THEME } from '@mtes-mct/monitor-ui'
import { MissionSourceEnum } from '@common/types/env-mission-types.ts'
import React, { FC } from 'react'

interface MissionIconUlamProps {
  missionSource?: MissionSourceEnum
}

const MissionIconUlam: FC<MissionIconUlamProps> = ({ missionSource }) => {
  const MissionIcon = missionSource === MissionSourceEnum.RAPPORTNAV ? Icon.MissionAction : Icon.ShowErsMessages

  return <MissionIcon size={28} color={THEME.color.charcoal} />
}

export default MissionIconUlam
