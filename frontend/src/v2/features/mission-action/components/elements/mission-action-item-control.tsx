import { FC } from 'react'
import { MissionAction } from '../../../common/types/mission-action'
import { MissionSourceEnum } from '../../../common/types/mission-types'
import MissionActionItemEnvControl from './mission-action-item-env-control'
import MissionActionItemFishControl from './mission-action-item-fish-control'
import MissionActionItemNavControl from './mission-action-item-nav-control'

const MissionActionItemControl: FC<{
  action: MissionAction
  onChange: (newAction: MissionAction, debounceTime?: number) => Promise<unknown>
  isMissionFinished?: boolean
}> = ({ action, onChange, isMissionFinished }) => {
  switch (action.source) {
    case MissionSourceEnum.MONITORENV:
      return <MissionActionItemEnvControl action={action} onChange={onChange} />
    case MissionSourceEnum.MONITORFISH:
      return <MissionActionItemFishControl action={action} onChange={onChange} isMissionFinished={isMissionFinished} />
    default:
      return <MissionActionItemNavControl action={action} onChange={onChange} />
  }
}

export default MissionActionItemControl
