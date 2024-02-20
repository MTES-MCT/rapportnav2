import { Dropdown, Icon } from '@mtes-mct/monitor-ui'
import { ActionTypeEnum } from '../../../types/env-mission-types'
import { FC } from "react";

interface ActionSelectionDropdownProps {
  onSelect: (key: ActionTypeEnum) => void
}

const ActionSelectionDropdown: FC<ActionSelectionDropdownProps> = ({onSelect}) => {
  return (
    <Dropdown Icon={Icon.Plus} onSelect={onSelect} title="Ajouter">
      <Dropdown.Item Icon={Icon.ControlUnit} eventKey={ActionTypeEnum.CONTROL}>
        Ajouter des contrôles
      </Dropdown.Item>
      <Dropdown.Item Icon={Icon.Note} eventKey={ActionTypeEnum.NOTE}>
        Ajouter une note libre
      </Dropdown.Item>
      <Dropdown.Item Icon={Icon.Rescue} eventKey={ActionTypeEnum.RESCUE} disabled>
        Ajouter une assistance / sauvetage
      </Dropdown.Item>
      <Dropdown.Item Icon={Icon.More} eventKey={ActionTypeEnum.OTHER} disabled>
        Ajouter une autre activité de mission
      </Dropdown.Item>
    </Dropdown>
  )
}

export default ActionSelectionDropdown
