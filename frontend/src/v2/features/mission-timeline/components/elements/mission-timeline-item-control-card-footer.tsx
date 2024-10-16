import { Action } from '@common/types/action-types'
import { MissionSourceEnum } from '@common/types/env-mission-types'
import { FC } from 'react'
import TextByCacem from '../../../../features/common/components/ui/text-by-cacem'
import TextByCnsp from '../../../../features/common/components/ui/text-by-cnsp'

const MissionTimelineItemControlCardFooter: FC<{ action?: Action }> = ({ action }) => {
  if (action?.source === MissionSourceEnum.MONITORENV) return <TextByCacem />
  if (action?.source === MissionSourceEnum.MONITORFISH) return <TextByCnsp />
}

export default MissionTimelineItemControlCardFooter
