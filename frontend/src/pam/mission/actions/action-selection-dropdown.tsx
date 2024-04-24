import { Dropdown, Icon } from '@mtes-mct/monitor-ui'
import { Dropdown as RSuiteDropdown} from 'rsuite'
import { ActionTypeEnum } from '../../../types/env-mission-types'
import React, { FC } from "react";

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
      <Dropdown.Item Icon={Icon.Rescue} eventKey={ActionTypeEnum.RESCUE}>
        Ajouter une assistance / sauvetage
      </Dropdown.Item>
      <RSuiteDropdown.Menu title="Ajouter une autre activité de mission" eventKey={ActionTypeEnum.OTHER} >
        <RSuiteDropdown.Item>Item 2-1-1</RSuiteDropdown.Item>
        <RSuiteDropdown.Item>Item 2-1-2</RSuiteDropdown.Item>
        <RSuiteDropdown.Item>Item 2-1-3</RSuiteDropdown.Item>
      </RSuiteDropdown.Menu>
    </Dropdown>
  )
}

export default ActionSelectionDropdown
