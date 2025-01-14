import { ActionStatusType } from '@common/types/action-types.ts'
import { getColorForStatus, mapStatusToText } from '@common/utils/status-utils'
import { Dropdown, Icon } from '@mtes-mct/monitor-ui'
import { FC } from 'react'
import { Stack } from 'rsuite'
import { useMissionTimeline } from '../../../common/hooks/use-mission-timeline'
import useCreateMissionActionMutation from '../../../common/services/use-create-mission-action'
import { ActionType } from '../../../common/types/action-type'
import { v4 as uuidv4 } from 'uuid'
import { MissionNavAction } from '../../../common/types/mission-action.ts'

const ACTION_STATUS: ActionStatusType[] = [
  ActionStatusType.NAVIGATING,
  ActionStatusType.ANCHORED,
  ActionStatusType.DOCKED,
  ActionStatusType.UNAVAILABLE
]

interface MissionTimelineAddStatusProps {
  missionId: number
  onSumbit?: (id?: string) => void
}

export const MissionStatusColorTag: FC<{ status: ActionStatusType }> = ({ status }) => (
  <div
    style={{
      backgroundColor: getColorForStatus(status),
      width: '16px',
      height: '16px',
      borderRadius: '16px'
    }}
  ></div>
)

const MissionTimelineAddStatus: FC<MissionTimelineAddStatusProps> = ({ missionId, onSumbit }) => {
  const { getActionInput } = useMissionTimeline(missionId)
  const mutation = useCreateMissionActionMutation(missionId)

  const handleAddStatus = async (status: ActionStatusType) => {
    const id = uuidv4()
    const action = { ...getActionInput(ActionType.STATUS, { status }), id }
    debugger
    mutation.mutate(action, {
      onSuccess: (data: MissionNavAction) => {
        // debugger
        // if (onSumbit && navigator.onLine) {
        //   onSumbit(data?.id)
        // }
      },
      onSettled: (_, __, ___, context) => {
        debugger
        if (onSumbit) {
          onSumbit(context.action?.id)
        }
      }
    })
  }

  return (
    <Dropdown
      Icon={Icon.FleetSegment}
      onSelect={handleAddStatus}
      disabled={mutation.isPending}
      title="&#x25BC;"
      placement="bottomEnd"
    >
      {ACTION_STATUS.map(status => (
        <Dropdown.Item key={status} eventKey={status} disabled={mutation.isPending}>
          <Stack spacing="0.5rem" alignItems="center">
            <Stack.Item>
              <MissionStatusColorTag status={status} />
            </Stack.Item>
            <Stack.Item>{mapStatusToText(status)}</Stack.Item>
          </Stack>
        </Dropdown.Item>
      ))}
    </Dropdown>
  )
}

export default MissionTimelineAddStatus
