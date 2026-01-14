import { IconProps, SideMenu } from '@mtes-mct/monitor-ui'
import { FunctionComponent, useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { Sidebar } from 'rsuite'
import styled from 'styled-components'

type SideBarItem = { key: string; icon: FunctionComponent<IconProps>; url: string }

type MissionListPageSidebarProps = {
  defaultItemKey?: string
  items: SideBarItem[]
}

const StyledMenu = styled.div`
  height: 100%;
  background-color: red;

  & > div {
    height: 100%; /* Override the 100vh of the sidebar because it prevents footer from showing up */
  }
`

const MissionListPageSidebar: React.FC<MissionListPageSidebarProps> = ({ items, defaultItemKey }) => {
  const navigate = useNavigate()
  const [currentKey, setCurrentKey] = useState<string>()

  const handleClick = (item: SideBarItem) => {
    setCurrentKey(item.key)
    navigate(item.url)
  }

  useEffect(() => {
    setCurrentKey(defaultItemKey)
  }, [defaultItemKey])

  return (
    <Sidebar style={{ flex: 0, width: '64px' }}>
      <StyledMenu>
        <SideMenu>
          {items.map(item => (
            <SideMenu.Button
              key={item.key}
              Icon={item.icon}
              title={item.key}
              isActive={item.key === currentKey}
              onClick={() => handleClick(item)}
            />
          ))}
        </SideMenu>
      </StyledMenu>
    </Sidebar>
  )
}

export default MissionListPageSidebar
