import { Dropdown, Icon, THEME } from '@mtes-mct/monitor-ui'
import { ActionStatusType } from '../../mission-types'
import { Stack } from 'rsuite'
import { getColorForStatus } from './utils'

interface StatusSelectionDropdownProps {
  onSelect: (key: ActionStatusType) => void
}

const StatusColorTag: React.FC<{ status: ActionStatusType }> = ({ status }) => (
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
        <Stack.Item>Navigation</Stack.Item>
      </Stack>
    </Dropdown.Item>
    <Dropdown.Item eventKey={ActionStatusType.ANCHORING}>
      <Stack spacing="0.5rem" alignItems="center">
        <Stack.Item>
          <StatusColorTag status={ActionStatusType.ANCHORING} />
        </Stack.Item>
        <Stack.Item>Mouillage</Stack.Item>
      </Stack>
    </Dropdown.Item>
    <Dropdown.Item eventKey={ActionStatusType.DOCKING}>
      <Stack spacing="0.5rem" alignItems="center">
        <Stack.Item>
          <StatusColorTag status={ActionStatusType.DOCKING} />
        </Stack.Item>
        <Stack.Item>Présence à quai</Stack.Item>
      </Stack>
    </Dropdown.Item>
    <Dropdown.Item eventKey={ActionStatusType.UNAVAILABLE}>
      <Stack spacing="0.5rem" alignItems="center">
        <Stack.Item>
          <StatusColorTag status={ActionStatusType.UNAVAILABLE} />
        </Stack.Item>
        <Stack.Item>Indisponibilité</Stack.Item>
      </Stack>
    </Dropdown.Item>
  </Dropdown>
)

export default StatusSelectionDropdown
