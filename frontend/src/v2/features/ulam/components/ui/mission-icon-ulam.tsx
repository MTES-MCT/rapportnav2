import { Icon, THEME } from '@mtes-mct/monitor-ui'
import { FC } from 'react'
import { MissionReportTypeEnum } from '../../../common/types/mission-types.ts'

interface MissionIconUlamProps {
  missionReportType?: MissionReportTypeEnum
}

const MissionIconUlam: FC<MissionIconUlamProps> = ({ missionReportType }) => {

  const iconMap = {
    [MissionReportTypeEnum.OFFICE_REPORT]: Icon.MissionAction,
    [MissionReportTypeEnum.EXTERNAL_REINFORCEMENT_TIME_REPORT]: Icon.Logout,
    [MissionReportTypeEnum.FIELD_REPORT]: Icon.ShowErsMessages,
  };

  const MissionIcon = iconMap[missionReportType!!] || Icon.ShowErsMessages;
  return <MissionIcon size={28} color={THEME.color.charcoal} />;
}

export default MissionIconUlam
