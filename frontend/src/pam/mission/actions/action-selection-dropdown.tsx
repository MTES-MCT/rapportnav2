import { FC } from 'react'
import { Dropdown, Icon } from '@mtes-mct/monitor-ui'
import { Dropdown as RSuiteDropdown } from 'rsuite'
import { ActionTypeEnum } from '../../../types/env-mission-types'
import MoreIcon from '@rsuite/icons/More'

interface ActionSelectionDropdownProps {
  onSelect: (key: ActionTypeEnum) => void
}

const ActionSelectionDropdown: FC<ActionSelectionDropdownProps> = ({ onSelect }) => {
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
      <RSuiteDropdown.Menu title={'Ajouter une autre activité de mission'} icon={<MoreIcon />}>
        <Dropdown.Item eventKey={ActionTypeEnum.NAUTICAL_EVENT}>Manifestation nautique</Dropdown.Item>
        <Dropdown.Item eventKey={ActionTypeEnum.VIGIMER}>Permanence Vigimer</Dropdown.Item>
        <Dropdown.Item eventKey={ActionTypeEnum.ANTI_POLLUTION}>Opération de lutte anti-pollution</Dropdown.Item>
        <Dropdown.Item eventKey={ActionTypeEnum.BAAEM_PERMANENCE}>Permanence BAAEM</Dropdown.Item>
        <Dropdown.Item eventKey={ActionTypeEnum.PUBLIC_ORDER}>Maintien de l'ordre public</Dropdown.Item>
        <Dropdown.Item eventKey={ActionTypeEnum.REPRESENTATION}>Representation</Dropdown.Item>
        <Dropdown.Item eventKey={ActionTypeEnum.ILLEGAL_IMMIGRATION}>
          Lutte contre l'immigration irrégulière
        </Dropdown.Item>
      </RSuiteDropdown.Menu>
    </Dropdown>
  )
}

export default ActionSelectionDropdown
