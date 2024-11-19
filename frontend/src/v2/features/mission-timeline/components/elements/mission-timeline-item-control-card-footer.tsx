import { MissionSourceEnum } from '@common/types/env-mission-types'
import { FC } from 'react'
import TextByCacem from '../../../../features/common/components/ui/text-by-cacem'
import TextByCnsp from '../../../../features/common/components/ui/text-by-cnsp'
import { MissionTimelineAction } from '../../types/mission-timeline-output'

const MissionTimelineItemControlCardFooter: FC<{ action?: MissionTimelineAction }> = ({ action }) => {
  if (action?.source === MissionSourceEnum.MONITORENV) return <TextByCacem />
  if (action?.source === MissionSourceEnum.MONITORFISH) return <TextByCnsp />
}

export default MissionTimelineItemControlCardFooter
