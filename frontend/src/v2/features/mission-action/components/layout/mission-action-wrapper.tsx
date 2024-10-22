import { Action } from '@common/types/action-types.ts'
import React, { createElement, FunctionComponent } from 'react'
import MissionActionEmpty from '../ui/mission-action-empty.tsx'
import MissionActionError from '../ui/mission-action-error.tsx'
import MissionActionLoader from '../ui/mission-action-loader.tsx'

type MissionActionWrapperProps = {
  action?: Action
  isError?: any
  missionId?: number
  isLoading?: boolean
  item: FunctionComponent<{ action: Action; missionId?: number }>
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
