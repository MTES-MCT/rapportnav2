import { Stack } from 'rsuite'
import { ModuleType } from '../../../common/types/module-type'
import { TimelineDropdownItem } from '../../hooks/use-timeline'
import MissionTimelineAddAction from '../elements/mission-timeline-add-action'
import MissionTimelineAddStatus from '../elements/mission-timeline-add-status'

type MissionTimelineHeaderWrapperProps = {
  missionId: string
  hideAction?: boolean
  hideStatus?: boolean
  moduleType: ModuleType
  onSubmit: (id?: string) => void
  dropdownItems: TimelineDropdownItem[]
}

const MissionTimelineHeaderWrapper: React.FC<MissionTimelineHeaderWrapperProps> = ({
  onSubmit,
  hideAction,
  hideStatus,
  missionId,
  moduleType,
  dropdownItems
}) => {
  return (
    <Stack direction={'row'} justifyContent={'space-between'} spacing={'0.5rem'} wrap={true}>
      <Stack.Item>
        {!hideAction && (
          <MissionTimelineAddAction
            onSumbit={onSubmit}
            missionId={missionId}
            moduleType={moduleType}
            dropdownItems={dropdownItems}
          />
        )}
      </Stack.Item>
      <Stack.Item>
        <Stack direction={'row'}>
          <Stack.Item>
            {!hideStatus && <MissionTimelineAddStatus missionId={missionId} onSumbit={onSubmit} />}
          </Stack.Item>
        </Stack>
      </Stack.Item>
    </Stack>
  )
}

export default MissionTimelineHeaderWrapper
