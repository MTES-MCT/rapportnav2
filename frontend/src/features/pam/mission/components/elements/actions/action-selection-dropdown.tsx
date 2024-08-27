import { FC } from 'react'
import { Dropdown, Icon } from '@mtes-mct/monitor-ui'
import { Dropdown as RSuiteDropdown } from 'rsuite'
import { ActionTypeEnum } from '@common/types/env-mission-types.ts'
import MoreIcon from '@rsuite/icons/More'

interface ActionSelectionDropdownProps {
  onSelect: (key: ActionTypeEnum) => void
}

const ActionSelectionDropdown: FC<ActionSelectionDropdownProps> = ({ onSelect }) => {
  return (
    <Dropdown Icon={Icon.Plus} onSelect={onSelect} title="Ajouter" placement={'bottomEnd'}>
      <Dropdown.Item Icon={Icon.ControlUnit} eventKey={ActionTypeEnum.CONTROL}>
        Ajouter des contrôles
      </Dropdown.Item>
      <Dropdown.Item Icon={Icon.Note} eventKey={ActionTypeEnum.NOTE}>
        Ajouter une note libre
      </Dropdown.Item>
      <Dropdown.Item Icon={Icon.Rescue} eventKey={ActionTypeEnum.RESCUE}>
        Ajouter une assistance / sauvetage
      </Dropdown.Item>
      <RSuiteDropdown.Separator />
      {/*<Dropdown.Item icon={<MoreIcon />} active={false} disabled={true}>*/}
      {/*  Ajouter une autre activité de mission*/}
      {/*</Dropdown.Item>*/}
      <Dropdown.Item icon={<MoreIcon />} eventKey={ActionTypeEnum.NAUTICAL_EVENT}>
        Sécu de manifestation nautique
      </Dropdown.Item>
      <Dropdown.Item icon={<MoreIcon />} eventKey={ActionTypeEnum.VIGIMER}>
        Permanence Vigimer
      </Dropdown.Item>
      <Dropdown.Item icon={<MoreIcon />} eventKey={ActionTypeEnum.ANTI_POLLUTION}>
        Opération de lutte anti-pollution
      </Dropdown.Item>
      <Dropdown.Item icon={<MoreIcon />} eventKey={ActionTypeEnum.BAAEM_PERMANENCE}>
        Permanence BAAEM
      </Dropdown.Item>
      <Dropdown.Item icon={<MoreIcon />} eventKey={ActionTypeEnum.PUBLIC_ORDER}>
        Maintien de l'ordre public
      </Dropdown.Item>
      <Dropdown.Item icon={<MoreIcon />} eventKey={ActionTypeEnum.REPRESENTATION}>
        Représentation
      </Dropdown.Item>
      <Dropdown.Item icon={<MoreIcon />} eventKey={ActionTypeEnum.ILLEGAL_IMMIGRATION}>
        Lutte contre l'immigration illégale
      </Dropdown.Item>
      {/*</RSuiteDropdown.Menu>*/}
    </Dropdown>
  )
}

export default ActionSelectionDropdown
