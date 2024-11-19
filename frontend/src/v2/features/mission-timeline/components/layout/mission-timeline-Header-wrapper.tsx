import { useNavigate } from 'react-router-dom'
import { Stack } from 'rsuite'
import { ModuleType } from '../../../common/types/module-type'
import MissionTimelineAddAction from '../elements/mission-timeline-add-action'
import MissionTimelineAddStatus from '../elements/mission-timeline-add-status'

type MissionTimelineHeaderWrapperProps = {
  missionId?: number
  hideAction?: boolean
  hideStatus?: boolean
  moduleType: ModuleType
}

const MissionTimelineHeaderWrapper: React.FC<MissionTimelineHeaderWrapperProps> = ({
  hideAction,
  hideStatus,
  missionId,
  moduleType
}) => {
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

export default MissionTimelineHeaderWrapper
