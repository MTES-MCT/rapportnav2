import React, { createElement, FunctionComponent } from 'react'
import { MissionAction } from '../../types/mission-action.ts'
import ActionEmpty from '../ui/action-empty.tsx'
import ActionError from '../ui/action-error.tsx'
import ActionLoader from '../ui/action-loader.tsx'

type ActionWrapperProps = {
  isError?: any
  ownerId: string
  isLoading?: boolean
  action?: MissionAction
  item: FunctionComponent<{ action: MissionAction; ownerId: string }>
}

const ActionWrapper: React.FC<ActionWrapperProps> = ({ item, action, ownerId, isError, isLoading }) => {
  if (isLoading) return <ActionLoader />
  if (!action) return <ActionEmpty />
  if (isError) return <ActionError error={isError} />
  return (
    <div style={{ width: '100%' }}>
      {createElement(item, {
        key: action.id,
        action,
        ownerId
      })}
    </div>
  )
}

export default ActionWrapper
