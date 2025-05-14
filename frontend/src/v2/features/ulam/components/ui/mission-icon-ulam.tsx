import { Icon, THEME } from '@mtes-mct/monitor-ui'
import { FC } from 'react'
import { MissionReportTypeEnum } from '../../../common/types/mission-types.ts'

interface MissionIconUlamProps {
  missionReportType?: MissionReportTypeEnum
}

const MissionIconUlam: FC<MissionIconUlamProps> = ({ missionReportType }) => {
  const MissionIcon = missionReportType === MissionReportTypeEnum.OFFICE_REPORT ? Icon.MissionAction : Icon.ShowErsMessages
  return <MissionIcon size={28} color={THEME.color.charcoal} />
}

export default MissionIconUlam
