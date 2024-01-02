import { Tag, THEME } from '@mtes-mct/monitor-ui'
import { MissionSourceEnum } from '../../types/env-mission-types'
import React from "react";
import Text from '../../ui/text'


interface MissionOpenByTagProps {
  missionSource?: MissionSourceEnum
  isFake?: boolean
}

const MissionOpenByTag: React.FC<MissionOpenByTagProps> = ({missionSource, isFake}) => {
  if (!!isFake) {
    return (
      <Tag
        backgroundColor={THEME.color.gunMetal}
        color={THEME.color.white}
      >
        <Text as={"h3"} weight="medium" color={THEME.color.white}>
          Mission fictive
        </Text>
      </Tag>
    )
  }
  return (
    <Tag
      backgroundColor={
        missionSource === MissionSourceEnum.MONITORENV || missionSource === MissionSourceEnum.POSEIDON_CACEM
          ? THEME.color.mediumSeaGreen
          : missionSource === MissionSourceEnum.MONITORFISH || missionSource === MissionSourceEnum.POSEIDON_CNSP
            ? THEME.color.blueGray
            : THEME.color.gainsboro
      }
      color={missionSource === MissionSourceEnum.RAPPORTNAV ? THEME.color.charcoal : THEME.color.white}
    >
      <Text as={"h3"} weight="medium"
            color={missionSource === MissionSourceEnum.RAPPORTNAV ? THEME.color.charcoal : THEME.color.white}>
        {missionSource === MissionSourceEnum.RAPPORTNAV
          ? "Ouverte par l'unité"
          : missionSource === MissionSourceEnum.MONITORENV || missionSource === MissionSourceEnum.POSEIDON_CACEM
            ? 'Ouverte par le CACEM'
            : missionSource === MissionSourceEnum.MONITORFISH || missionSource === MissionSourceEnum.POSEIDON_CNSP
              ? 'Ouverte par le CNSP'
              : 'Ouverte par N/A'}
      </Text>
    </Tag>
  )
}

export default MissionOpenByTag
