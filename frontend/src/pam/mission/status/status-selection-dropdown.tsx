import { Dropdown, Icon } from '@mtes-mct/monitor-ui'
import { ActionStatusType } from '../../../types/action-types'
import { Stack } from 'rsuite'
import { getColorForStatus, mapStatusToText } from './utils'
import { FC } from "react";

interface StatusSelectionDropdownProps {
  onSelect: (key: ActionStatusType) => void
  loading?: boolean
}

export const StatusColorTag: FC<{ status: ActionStatusType }> = ({status}) => (
  <div
    style={{
      backgroundColor: getColorForStatus(status),
      width: '16px',
      height: '16px',
      borderRadius: '16px'
    }}
  ></div>
)

const StatusSelectionDropdown: FC<StatusSelectionDropdownProps> = ({onSelect, loading}) => (
  <Dropdown
    Icon={Icon.FleetSegment}
    onSelect={onSelect}
    disabled={loading}
    title="&#x25BC;"
    placement="bottomEnd"
  >
    <Dropdown.Item eventKey={ActionStatusType.NAVIGATING} disabled={loading}>
      <Stack spacing="0.5rem" alignItems="center">
        <Stack.Item>
          <StatusColorTag status={ActionStatusType.NAVIGATING}/>
        </Stack.Item>
        <Stack.Item>{mapStatusToText(ActionStatusType.NAVIGATING)}</Stack.Item>
      </Stack>
    </Dropdown.Item>
    <Dropdown.Item eventKey={ActionStatusType.ANCHORED} disabled={loading}>
      <Stack spacing="0.5rem" alignItems="center">
        <Stack.Item>
          <StatusColorTag status={ActionStatusType.ANCHORED}/>
        </Stack.Item>
        <Stack.Item>{mapStatusToText(ActionStatusType.ANCHORED)}</Stack.Item>
      </Stack>
    </Dropdown.Item>
    <Dropdown.Item eventKey={ActionStatusType.DOCKED} disabled={loading}>
      <Stack spacing="0.5rem" alignItems="center">
        <Stack.Item>
          <StatusColorTag status={ActionStatusType.DOCKED}/>
        </Stack.Item>
        <Stack.Item>{mapStatusToText(ActionStatusType.DOCKED)}</Stack.Item>
      </Stack>
    </Dropdown.Item>
    <Dropdown.Item eventKey={ActionStatusType.UNAVAILABLE} disabled={loading}>
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
