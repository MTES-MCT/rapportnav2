import { Dropdown, Icon } from '@mtes-mct/monitor-ui'
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
      <Dropdown.Item Icon={Icon.More} eventKey={ActionTypeEnum.NAUTICAL_EVENT}>
        Manifestation nautique
      </Dropdown.Item>
      <Dropdown.Item Icon={Icon.More} eventKey={ActionTypeEnum.VIGIMER}>
        Permanence Vigimer
      </Dropdown.Item>
      <Dropdown.Item Icon={Icon.More} eventKey={ActionTypeEnum.ANTI_POLLUTION}>
        Opération de lutte anti-pollution
      </Dropdown.Item>
      <Dropdown.Item Icon={Icon.More} eventKey={ActionTypeEnum.BAAEM_PERMANENCE}>
        Permanence BAAEM
      </Dropdown.Item>
    </Dropdown>
  )
}

export default ActionSelectionDropdown
