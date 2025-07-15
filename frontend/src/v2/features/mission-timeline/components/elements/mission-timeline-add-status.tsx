import { ActionStatusType } from '@common/types/action-types.ts'
import { getColorForStatus, mapStatusToText } from '@common/utils/status-utils'
import { Dropdown, Icon } from '@mtes-mct/monitor-ui'
import { FC } from 'react'
import { Stack } from 'rsuite'
import { useMissionTimeline } from '../../../common/hooks/use-mission-timeline'
import useCreateMissionActionMutation from '../../../common/services/use-create-mission-action'
import { ActionType } from '../../../common/types/action-type'
import { MissionNavAction } from '../../../common/types/mission-action.ts'
import { PAM_V2_HOME_PATH } from '@router/routes.tsx'
import { useNavigate } from 'react-router-dom'
import { useOnlineManager } from '../../../common/hooks/use-online-manager.tsx'
import { v4 as uuidv4 } from 'uuid'

const ACTION_STATUS: ActionStatusType[] = [
  ActionStatusType.NAVIGATING,
  ActionStatusType.ANCHORED,
  ActionStatusType.DOCKED,
  ActionStatusType.UNAVAILABLE
]

interface MissionTimelineAddStatusProps {
  missionId: string
  onSubmit?: (id?: string) => void
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

const MissionTimelineAddStatus: FC<MissionTimelineAddStatusProps> = ({ missionId, onSubmit }) => {
  const navigate = useNavigate()
  const { getActionInput } = useMissionTimeline(missionId)
  const mutation = useCreateMissionActionMutation(missionId)
  const { isOnline } = useOnlineManager()

  const handleAddStatus = async (status: ActionStatusType) => {
    const action = {
      id: uuidv4(), // Generate a UUID locally
      ...getActionInput(ActionType.STATUS, { status })
    }

    mutation.mutate(action, {
      onSuccess: (data: MissionNavAction) => {
        const id = data?.data?.id
        if (id) {
          const url = `${PAM_V2_HOME_PATH}/${missionId}/${id}`
          navigate(url)
        }
        if (onSubmit && isOnline) {
          onSubmit(id)
        }
      }
    })
  }

  return (
    <Dropdown Icon={Icon.FleetSegment} onSelect={handleAddStatus} title="&#x25BC;" placement="bottomEnd">
      {ACTION_STATUS.map(status => (
        <Dropdown.Item key={status} eventKey={status}>
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
