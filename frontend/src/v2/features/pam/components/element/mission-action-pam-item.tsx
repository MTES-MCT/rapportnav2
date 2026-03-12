import { useStore } from '@tanstack/react-store'
import { isEqual } from 'lodash'
import { createElement, FC } from 'react'
import { store } from '../../../../store'
import useUpdateActionMutation from '../../../common/services/use-update-action'
import { MissionAction } from '../../../common/types/mission-action'
import { OwnerType } from '../../../common/types/owner-type'
import { usePamActionRegistry } from '../../hooks/use-pam-action-registry'

interface MissionActionPamItemProps {
  ownerId: string
  action: MissionAction
}

const MissionActionPamItem: FC<MissionActionPamItemProps> = ({ action, ownerId }) => {
  const mutation = useUpdateActionMutation()
  const { component } = usePamActionRegistry(action.actionType)
  const isMissionFinished = useStore(store, state => state.mission.isMissionFinished)

  const onChange = async (newAction: MissionAction): Promise<void> => {
    if (isEqual(action, newAction)) return
    await mutation.mutateAsync({ ownerId, ownerType: OwnerType.MISSION, action: newAction })
  }

  return (
    <div style={{ width: '100%' }}>
      {component && createElement(component, { action, onChange, isMissionFinished })}
    </div>
  )
}

export default MissionActionPamItem
