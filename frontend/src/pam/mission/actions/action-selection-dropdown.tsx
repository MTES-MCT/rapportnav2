import { Dropdown, Icon, THEME } from '@mtes-mct/monitor-ui'
import { ActionTypeEnum } from '../../env-mission-types'

interface ActionSelectionDropdownProps {
  onSelect: (key: ActionTypeEnum) => void
}

const ActionSelectionDropdown: React.FC<ActionSelectionDropdownProps> = ({ onSelect }) => {
  return (
    <Dropdown Icon={Icon.Plus} onSelect={onSelect} title="Ajouter">
      <Dropdown.Item Icon={Icon.ControlUnit} eventKey={ActionTypeEnum.CONTROL}>
        Ajouter des contrôles
      </Dropdown.Item>
      <Dropdown.Item Icon={Icon.Note} eventKey={ActionTypeEnum.NOTE} disabled>
        Ajouter une note libre
      </Dropdown.Item>
      <Dropdown.Item Icon={Icon.More} eventKey={ActionTypeEnum.RESCUE} disabled>
        Ajouter une assitance / sauvetage
      </Dropdown.Item>
      <Dropdown.Item Icon={Icon.More} eventKey={ActionTypeEnum.OTHER} disabled>
        Ajouter une autre activité de mission
      </Dropdown.Item>
    </Dropdown>
  )
}

export default ActionSelectionDropdown
