import { Dropdown, Icon } from '@mtes-mct/monitor-ui'
import { ActionStatusType } from '../../mission-types'
import { Stack } from 'rsuite'
import { getColorForStatus, mapStatusToText } from './utils'

interface StatusSelectionDropdownProps {
  onSelect: (key: ActionStatusType) => void
}

export const StatusColorTag: React.FC<{ status: ActionStatusType }> = ({ status }) => (
  <div
    style={{
      backgroundColor: getColorForStatus(status),
      width: '10px',
      height: '10px',
      borderRadius: '10px'
    }}
  ></div>
)

const StatusSelectionDropdown: React.FC<StatusSelectionDropdownProps> = ({ onSelect }) => (
  <Dropdown Icon={Icon.FleetSegment} onSelect={onSelect} title="">
    <Dropdown.Item eventKey={ActionStatusType.NAVIGATING}>
      <Stack spacing="0.5rem" alignItems="center">
        <Stack.Item>
          <StatusColorTag status={ActionStatusType.NAVIGATING} />
        </Stack.Item>
        <Stack.Item>{mapStatusToText(ActionStatusType.NAVIGATING)}</Stack.Item>
      </Stack>
    </Dropdown.Item>
    <Dropdown.Item eventKey={ActionStatusType.ANCHORING}>
      <Stack spacing="0.5rem" alignItems="center">
        <Stack.Item>
          <StatusColorTag status={ActionStatusType.ANCHORING} />
        </Stack.Item>
        <Stack.Item>{mapStatusToText(ActionStatusType.ANCHORING)}</Stack.Item>
      </Stack>
    </Dropdown.Item>
    <Dropdown.Item eventKey={ActionStatusType.DOCKED}>
      <Stack spacing="0.5rem" alignItems="center">
        <Stack.Item>
          <StatusColorTag status={ActionStatusType.DOCKED} />
        </Stack.Item>
        <Stack.Item>{mapStatusToText(ActionStatusType.DOCKED)}</Stack.Item>
      </Stack>
    </Dropdown.Item>
    <Dropdown.Item eventKey={ActionStatusType.UNAVAILABLE}>
      <Stack spacing="0.5rem" alignItems="center">
        <Stack.Item>
          <StatusColorTag status={ActionStatusType.UNAVAILABLE} />
        </Stack.Item>
        <Stack.Item>{mapStatusToText(ActionStatusType.UNAVAILABLE)}</Stack.Item>
      </Stack>
    </Dropdown.Item>
  </Dropdown>
)

export default StatusSelectionDropdown
