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

const ACTION_STATUS: ActionStatusType[] = [
  ActionStatusType.NAVIGATING,
  ActionStatusType.ANCHORED,
  ActionStatusType.DOCKED,
  ActionStatusType.UNAVAILABLE
]

interface MissionTimelineAddStatusProps {
  missionId: number
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

  const handleAddStatus = async (status: ActionStatusType) => {
    const action = getActionInput(ActionType.STATUS, { status })
    mutation.mutate(action, {
      onSuccess: (data: MissionNavAction) => {
        const id = data?.data?.id
        if (id) {
          const url = `${PAM_V2_HOME_PATH}/${missionId}/${id}`
          navigate(url)
        }
        if (onSubmit && navigator.onLine) {
          onSubmit(id)
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
