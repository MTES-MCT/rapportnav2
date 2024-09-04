import { ActionTypeEnum } from '@common/types/env-mission-types.ts'
import { Dropdown, Icon, IconProps, THEME } from '@mtes-mct/monitor-ui'
import { FC, useState } from 'react'
import styled from 'styled-components'

type DropdownSubItem = { type: ActionTypeEnum; label: string }

type DropdownItem = DropdownSubItem & { icon: React.FunctionComponent<IconProps> }

const DROPDOWN_ITEMS: DropdownItem[] = [
  { icon: Icon.ControlUnit, type: ActionTypeEnum.CONTROL, label: 'Ajouter des contrôles' },
  { icon: Icon.Note, type: ActionTypeEnum.NOTE, label: 'Ajouter une note libre' },
  { icon: Icon.Rescue, type: ActionTypeEnum.RESCUE, label: 'Ajouter une assistance / sauvetage' },
  { icon: Icon.More, type: ActionTypeEnum.OTHER, label: 'Ajouter une autre activité de mission' }
]

const DROPDOWN_SUB_ITEMS: DropdownSubItem[] = [
  { type: ActionTypeEnum.NAUTICAL_EVENT, label: 'Sécu de manifestation nautique' },
  { type: ActionTypeEnum.BAAEM_PERMANENCE, label: 'Permanence BAAEM' },
  { type: ActionTypeEnum.VIGIMER, label: 'Permanence Vigimer' },
  { type: ActionTypeEnum.ANTI_POLLUTION, label: 'Opération de lutte anti-pollution' },
  { type: ActionTypeEnum.ILLEGAL_IMMIGRATION, label: `Lutte contre l'immigration illégale` },
  { type: ActionTypeEnum.PUBLIC_ORDER, label: `Maintien de l'ordre public` },
  { type: ActionTypeEnum.REPRESENTATION, label: 'Représentation' }
]

const DropdownSubItemStyled = styled(Dropdown.Item)(({ theme }) => ({
  color: theme.color.cultured,
  backgroundColor: THEME.color.blueYonder,
  ':hover': {
    color: THEME.color.charcoal,
    backgroundColor: THEME.color.blueYonder25
  }
}))

interface ActionDropdownProps {
  onSelect: (key: ActionTypeEnum) => void
}

const ActionDropdown: FC<ActionDropdownProps> = ({ onSelect }) => {
  const [showSubItem, setShowSubItem] = useState<boolean>(false)
  const handleSelect = (eventKey: ActionTypeEnum, event: React.SyntheticEvent) => {
    if (eventKey === ActionTypeEnum.OTHER) {
      event.stopPropagation()
      setShowSubItem(!showSubItem)
      return
    }
    onSelect(eventKey)
    setShowSubItem(false)
  }

  const handleClose = () => setShowSubItem(false)

  return (
    <Dropdown Icon={Icon.Plus} onSelect={handleSelect} title="Ajouter" placement={'bottomEnd'} onClose={handleClose}>
      {DROPDOWN_ITEMS.map(item => (
        <Dropdown.Item key={item.type} Icon={item.icon} eventKey={item.type}>
          {item.label}
        </Dropdown.Item>
      ))}
      {showSubItem &&
        DROPDOWN_SUB_ITEMS.map(item => (
          <DropdownSubItemStyled key={item.type} eventKey={item.type}>
            {item.label}
          </DropdownSubItemStyled>
        ))}
    </Dropdown>
  )
}

export default ActionDropdown
