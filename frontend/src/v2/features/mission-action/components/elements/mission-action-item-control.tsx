import { Action } from '@common/types/action-types'
import { MissionSourceEnum } from '@common/types/env-mission-types'
import { FC } from 'react'
import MissionActionItemEnvControl from './mission-action-item-env-control'
import MissionActionItemFishControl from './mission-action-item-fish-control'
import MissionActionItemNavControl from './mission-action-item-nav-control'

const MissionActionItemControl: FC<{
  action: Action
  onChange: (newAction: Action, debounceTime?: number) => Promise<unknown>
  isMissionFinished?: boolean
}> = ({ action, onChange, isMissionFinished }) => {
  if (action?.source === MissionSourceEnum.MONITORENV)
    return <MissionActionItemEnvControl action={action} onChange={onChange} />
  if (action?.source === MissionSourceEnum.MONITORFISH)
    return <MissionActionItemFishControl action={action} onChange={onChange} isMissionFinished={isMissionFinished} />
  return <MissionActionItemNavControl action={action} onChange={onChange} />
}

export default MissionActionItemControl
