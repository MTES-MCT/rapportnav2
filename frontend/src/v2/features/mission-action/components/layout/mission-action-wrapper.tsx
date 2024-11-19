import React, { createElement, FunctionComponent } from 'react'
import { MissionActionOutput } from '../../../common/types/mission-action-output.ts'
import MissionActionEmpty from '../ui/mission-action-empty.tsx'
import MissionActionError from '../ui/mission-action-error.tsx'
import MissionActionLoader from '../ui/mission-action-loader.tsx'

type MissionActionWrapperProps = {
  isError?: any
  missionId?: number
  isLoading?: boolean
  action?: MissionActionOutput
  item: FunctionComponent<{ action: MissionActionOutput; missionId?: number }>
}

const MissionActionWrapper: React.FC<MissionActionWrapperProps> = ({ item, action, missionId, isError, isLoading }) => {
  if (isLoading) return <MissionActionLoader />
  if (!action) return <MissionActionEmpty />
  if (isError) return <MissionActionError error={isError} />
  return (
    <div style={{ width: '100%' }}>
      {createElement(item, {
        key: action.id,
        action,
        missionId
      })}
    </div>
  )
}

export default MissionActionWrapper
