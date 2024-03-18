import { FC } from 'react'
import { Icon, Tag, THEME } from '@mtes-mct/monitor-ui'
import { MissionStatusEnum } from "../../types/mission-types.ts";
import Text from "../../ui/text.tsx";

interface MissionStatusTagProps {
  status?: MissionStatusEnum
}

const MissionStatusTag: FC<MissionStatusTagProps> = ({status}) => {
  let iconColor, IconComponent, text;

  switch (status) {
    case MissionStatusEnum.UPCOMING:
    case MissionStatusEnum.PENDING:
      iconColor = THEME.color.babyBlueEyes;
      IconComponent = Icon.More;
      text = "À venir";
      break;
    case MissionStatusEnum.IN_PROGRESS:
      iconColor = THEME.color.blueGray;
      IconComponent = Icon.Clock;
      text = "En cours";
      break;
    case MissionStatusEnum.ENDED:
      iconColor = THEME.color.charcoal;
      IconComponent = Icon.Confirm;
      text = "Terminée";
      break;
    case MissionStatusEnum.CLOSED:
      iconColor = THEME.color.maximumRed;
      IconComponent = Icon.Reject;
      text = "Clôturée";
      break;
    case MissionStatusEnum.UNAVAILABLE:
    default:
      iconColor = THEME.color.maximumRed;
      IconComponent = Icon.Close;
      text = "Indisponible";
  }

  return (
    <Tag iconColor={iconColor} backgroundColor={THEME.color.cultured} color={THEME.color.charcoal} Icon={IconComponent}>
      <Text as="h3" weight="medium" color={THEME.color.charcoal}>
        {text}
      </Text>
    </Tag>
  );
};

export default MissionStatusTag
