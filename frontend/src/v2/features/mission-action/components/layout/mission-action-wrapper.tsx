import React, { createElement, FunctionComponent } from 'react'
import { MissionAction } from '../../../common/types/mission-action.ts'
import MissionActionEmpty from '../ui/mission-action-empty.tsx'
import MissionActionError from '../ui/mission-action-error.tsx'
import MissionActionLoader from '../ui/mission-action-loader.tsx'

type MissionActionWrapperProps = {
  isError?: any
  missionId: number
  isLoading?: boolean
  action?: MissionAction
  item: FunctionComponent<{ action: MissionAction; missionId: number }>
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
