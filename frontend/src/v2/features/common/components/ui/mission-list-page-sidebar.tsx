import { IconProps, SideMenu } from '@mtes-mct/monitor-ui'
import { FunctionComponent, useEffect, useState } from 'react'
import { Sidebar } from 'rsuite'
import styled from 'styled-components'

type MissionListPageSidebarProps = {
  defaultItemKey?: string
  items: { key: string; icon: FunctionComponent<IconProps>; url: string }[]
}

const StyledMenu = styled.div`
  height: 100%;
  background-color: red;

  & > div {
    height: 100%; /* Override the 100vh of the sidebar because it prevents footer from showing up */
  }
`

const MissionListPageSidebar: React.FC<MissionListPageSidebarProps> = ({ items, defaultItemKey }) => {
  const [currentKey, setCurrentKey] = useState<string>()
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
              onClick={() => setCurrentKey(item.key)}
            />
          ))}
        </SideMenu>
      </StyledMenu>
    </Sidebar>
  )
}

export default MissionListPageSidebar
