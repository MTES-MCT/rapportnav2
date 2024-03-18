import { Tag, THEME } from '@mtes-mct/monitor-ui'
import { MissionSourceEnum } from '../../types/env-mission-types.ts'
import React from "react";
import Text from '../../ui/text.tsx'


interface MissionOpenByTagProps {
  missionSource?: MissionSourceEnum
  isFake?: boolean
}

const getTagBackgroundColor = (missionSource?: MissionSourceEnum): string => {
  switch (missionSource) {
    case MissionSourceEnum.MONITORENV:
    case MissionSourceEnum.POSEIDON_CACEM:
      return THEME.color.mediumSeaGreen;
    case MissionSourceEnum.MONITORFISH:
    case MissionSourceEnum.POSEIDON_CNSP:
      return THEME.color.blueGray;
    default:
      return THEME.color.gunMetal;
  }
};

const getTagTextColor = (missionSource?: MissionSourceEnum): string => {
  return missionSource === MissionSourceEnum.RAPPORTNAV ? THEME.color.white : THEME.color.white;
};

const getTagBorderColor = (missionSource?: MissionSourceEnum): string => {
  return missionSource === MissionSourceEnum.RAPPORTNAV ? THEME.color.white : 'transparent';
};

const getTagTextContent = (missionSource?: MissionSourceEnum): string => {
  switch (missionSource) {
    case MissionSourceEnum.RAPPORTNAV:
      return "Ouverte par l'unité";
    case MissionSourceEnum.MONITORENV:
    case MissionSourceEnum.POSEIDON_CACEM:
      return 'Ouverte par le CACEM';
    case MissionSourceEnum.MONITORFISH:
    case MissionSourceEnum.POSEIDON_CNSP:
      return 'Ouverte par le CNSP';
    default:
      return 'Ouverte par N/A';
  }
};

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
    );
  }

  const backgroundColor = getTagBackgroundColor(missionSource);
  const textColor = getTagTextColor(missionSource);
  const borderColor = getTagBorderColor(missionSource);
  const textContent = getTagTextContent(missionSource);

  return (
    <Tag
      backgroundColor={backgroundColor}
      borderColor={borderColor}
      color={textColor}
    >
      <Text as={"h3"} weight="medium" color={textColor}>
        {textContent}
      </Text>
    </Tag>
  );
};


export default MissionOpenByTag
