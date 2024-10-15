import { ModuleType } from '@common/types/module-type'
import { useNavigate } from 'react-router-dom'
import { Stack } from 'rsuite'
import MissionTimelineAddAction from './mission-timeline-add-action'
import MissionTimelineAddStatus from './mission-timeline-add-status'

type MissionTimelineHeaderProps = {
  missionId?: string
  hideAction?: boolean
  hideStatus?: boolean
  moduleType: ModuleType
}

const MissionTimelineHeader: React.FC<MissionTimelineHeaderProps> = ({
  hideAction,
  hideStatus,
  missionId,
  moduleType
}: MissionTimelineHeaderProps) => {
  const navigate = useNavigate()
  const handleOnSubmit = (id?: string) => navigate(`/${moduleType}/missions/${missionId}/${id}`)

  return (
    <Stack direction={'row'} justifyContent={'space-between'} spacing={'0.5rem'} wrap={true}>
      <Stack.Item>
        {!hideAction && (
          <MissionTimelineAddAction moduleType={moduleType} missionId={missionId} onSumbit={handleOnSubmit} />
        )}
      </Stack.Item>
      <Stack.Item>
        <Stack direction={'row'}>
          <Stack.Item>
            {!hideStatus && <MissionTimelineAddStatus missionId={missionId} onSumbit={handleOnSubmit} />}
          </Stack.Item>
        </Stack>
      </Stack.Item>
    </Stack>
  )
}

export default MissionTimelineHeader
