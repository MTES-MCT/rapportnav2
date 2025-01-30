import { Dropdown, Icon, THEME } from '@mtes-mct/monitor-ui'
import { FC, useState } from 'react'
import styled from 'styled-components'
import { ActionType } from '../../../common/types/action-type'
import { TimelineDropdownItem, TimelineDropdownSubItem } from '../../../mission-timeline/hooks/use-timeline'

const DropdownSubItemStyled = styled(Dropdown.Item)(({ theme }) => ({
  color: theme.color.cultured,
  backgroundColor: THEME.color.blueYonder,
  ':hover': {
    color: THEME.color.charcoal,
    backgroundColor: THEME.color.blueYonder25
  }
}))

const DropdownItem: FC<{ item: TimelineDropdownItem } & { style?: React.CSSProperties }> = ({ item, style }) => {
  return (
    <Dropdown.Item key={item.type} Icon={item.icon} eventKey={item.type} style={style}>
      {item?.dropdownText}
    </Dropdown.Item>
  )
}

const DropdownSubItem: FC<{ item: TimelineDropdownSubItem }> = ({ item }) => {
  return (
    <DropdownSubItemStyled key={item.type} eventKey={item.type}>
      {item.dropdownText}
    </DropdownSubItemStyled>
  )
}

interface MissionTimelineDropdownWrapperProps {
  dropdownItems: TimelineDropdownItem[]
  onSelect: (key: ActionType) => void
}

const MissionTimelineDropdownWrapper: FC<MissionTimelineDropdownWrapperProps> = ({ onSelect, dropdownItems }) => {
  const [currentKey, setCurrentKey] = useState<ActionType>()

  const handleSelect = (eventKey: ActionType, event: React.SyntheticEvent) => {
    if (dropdownItems.find(item => item.type === eventKey)?.subItems) {
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
      {dropdownItems.map(item => (
        <div key={item.type}>
          <DropdownItem item={item} style={{ minWidth: 300 }} />
          {item.type === currentKey &&
            item.subItems?.map(subItem => <DropdownSubItem key={subItem.type} item={subItem} />)}
        </div>
      ))}
    </Dropdown>
  )
}

export default MissionTimelineDropdownWrapper
