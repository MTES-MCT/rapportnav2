import { useStore } from '@tanstack/react-store'
import { createElement, FC } from 'react'
import { store } from '../../../../store'
import { resetDebounceTime } from '../../../../store/slices/delay-query-reducer'
import { useDelay } from '../../../common/hooks/use-delay'
import { useMissionActionInput } from '../../../common/hooks/use-mission-action-input'
import useUpdateMissionActionMutation from '../../../common/services/use-update-mission-action'
import { MissionActionOutput } from '../../../common/types/mission-action-output'
import { useUlamActionRegistry } from '../../hooks/use-ulam-action-registry'

interface MissionActionItemUlamProps {
  missionId?: number
  isMissionFinished?: boolean
  action: MissionActionOutput
}

const MissionActionItemUlam: FC<MissionActionItemUlamProps> = ({ action, missionId, isMissionFinished }) => {
  const { handleExecuteOnDelay } = useDelay()
  const debounceTime = useStore(store, state => state.delayQuery.debounceTime)
  const { getMissionActionInput } = useMissionActionInput()
  const { actionComponent } = useUlamActionRegistry(action.actionType)

  const [updateAction] = useUpdateMissionActionMutation(action.id, missionId)

  const onChange = async (newAction: MissionActionOutput) => {
    const input = getMissionActionInput(newAction)
    handleExecuteOnDelay(async () => {
      await updateAction({ variables: { input } })
      if (debounceTime !== undefined) resetDebounceTime()
    })
  }

  return (
    <div style={{ width: '100%' }}>
      {actionComponent && createElement(actionComponent, { action, onChange, isMissionFinished })}
    </div>
  )
}

export default MissionActionItemUlam
