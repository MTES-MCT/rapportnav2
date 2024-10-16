import { ActionTypeEnum } from '@common/types/env-mission-types.ts'
import { useActionRegistry } from '@features/v2/common/hooks/use-action-registry'
import { useModuleRegistry } from '@features/v2/common/hooks/use-module-registry'
import { ModuleType } from '@features/v2/common/types/module-type'
import { Dropdown, Icon, THEME } from '@mtes-mct/monitor-ui'
import { FC, useState } from 'react'
import styled from 'styled-components'

const DropdownSubItemStyled = styled(Dropdown.Item)(({ theme }) => ({
  color: theme.color.cultured,
  backgroundColor: THEME.color.blueYonder,
  ':hover': {
    color: THEME.color.charcoal,
    backgroundColor: THEME.color.blueYonder25
  }
}))

const DropdownItem: FC<{ type: ActionTypeEnum }> = ({ type }) => {
  const { timeline, icon } = useActionRegistry(type)
  return (
    <Dropdown.Item key={type} Icon={icon} eventKey={type}>
      {timeline?.dropdownText}
    </Dropdown.Item>
  )
}

const DropdownSubItem: FC<{ type: ActionTypeEnum }> = ({ type }) => {
  const { timeline } = useActionRegistry(type)
  return (
    <DropdownSubItemStyled key={type} eventKey={type}>
      {timeline?.dropdownText}
    </DropdownSubItemStyled>
  )
}

interface MissionActionDropdownWrapperProps {
  moduleType: ModuleType
  onSelect: (key: ActionTypeEnum) => void
}

const MissionActionDropdownWrapper: FC<MissionActionDropdownWrapperProps> = ({ onSelect, moduleType }) => {
  const { timelineDropdownItems } = useModuleRegistry(moduleType)
  const [currentKey, setCurrentKey] = useState<ActionTypeEnum>()

  const handleSelect = (eventKey: ActionTypeEnum, event: React.SyntheticEvent) => {
    if (timelineDropdownItems.find(item => item.type === eventKey)?.subItems) {
      event.stopPropagation()
      setCurrentKey(eventKey === currentKey ? undefined : eventKey)
      return
    }
    onSelect(eventKey)
    setCurrentKey(undefined)
  }

  const handleClose = () => setCurrentKey(undefined)

  return (
    <Dropdown Icon={Icon.Plus} onSelect={handleSelect} title="Ajouter" placement={'bottomEnd'} onClose={handleClose}>
      {timelineDropdownItems.map(item => (
        <div key={item.type}>
          <DropdownItem type={item.type} />
          {item.type === currentKey && item.subItems?.map(subItem => <DropdownSubItem type={subItem.type} />)}
        </div>
      ))}
    </Dropdown>
  )
}

export default MissionActionDropdownWrapper
