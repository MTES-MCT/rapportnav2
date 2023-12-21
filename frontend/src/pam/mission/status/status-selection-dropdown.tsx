import { Dropdown, Icon } from '@mtes-mct/monitor-ui'
import { ActionStatusType } from '../../../types/action-types'
import { Stack } from 'rsuite'
import { getColorForStatus, mapStatusToText } from './utils'

interface StatusSelectionDropdownProps {
  onSelect: (key: ActionStatusType) => void
}

export const StatusColorTag: React.FC<{ status: ActionStatusType }> = ({status}) => (
  <div
    style={{
      backgroundColor: getColorForStatus(status),
      width: '16px',
      height: '16px',
      borderRadius: '16px'
    }}
  ></div>
)

const StatusSelectionDropdown: React.FC<StatusSelectionDropdownProps> = ({onSelect}) => (
  <Dropdown Icon={Icon.FleetSegment} onSelect={onSelect} title="&#x25BC;" placement="bottomEnd">
    <Dropdown.Item eventKey={ActionStatusType.NAVIGATING}>
      <Stack spacing="0.5rem" alignItems="center">
        <Stack.Item>
          <StatusColorTag status={ActionStatusType.NAVIGATING}/>
        </Stack.Item>
        <Stack.Item>{mapStatusToText(ActionStatusType.NAVIGATING)}</Stack.Item>
      </Stack>
    </Dropdown.Item>
    <Dropdown.Item eventKey={ActionStatusType.ANCHORED}>
      <Stack spacing="0.5rem" alignItems="center">
        <Stack.Item>
          <StatusColorTag status={ActionStatusType.ANCHORED}/>
        </Stack.Item>
        <Stack.Item>{mapStatusToText(ActionStatusType.ANCHORED)}</Stack.Item>
      </Stack>
    </Dropdown.Item>
    <Dropdown.Item eventKey={ActionStatusType.DOCKED}>
      <Stack spacing="0.5rem" alignItems="center">
        <Stack.Item>
          <StatusColorTag status={ActionStatusType.DOCKED}/>
        </Stack.Item>
        <Stack.Item>{mapStatusToText(ActionStatusType.DOCKED)}</Stack.Item>
      </Stack>
    </Dropdown.Item>
    <Dropdown.Item eventKey={ActionStatusType.UNAVAILABLE}>
      <Stack spacing="0.5rem" alignItems="center">
        <Stack.Item>
          <StatusColorTag status={ActionStatusType.UNAVAILABLE}/>
        </Stack.Item>
        <Stack.Item>{mapStatusToText(ActionStatusType.UNAVAILABLE)}</Stack.Item>
      </Stack>
    </Dropdown.Item>
  </Dropdown>
)

export default StatusSelectionDropdown
