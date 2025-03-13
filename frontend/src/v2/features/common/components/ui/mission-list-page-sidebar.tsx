import { IconProps, SideMenu } from '@mtes-mct/monitor-ui'
import { FunctionComponent, useEffect, useState } from 'react'
import { Sidebar } from 'rsuite'

type MissionListPageSidebarProps = {
  defaultItemKey?: string
  items: { key: string; icon: FunctionComponent<IconProps>; url: string }[]
}

const MissionListPageSidebar: React.FC<MissionListPageSidebarProps> = ({ items, defaultItemKey }) => {
  const [currentKey, setCurrentKey] = useState<string>()
  useEffect(() => {
    setCurrentKey(defaultItemKey)
  }, [defaultItemKey])
  return (
    <Sidebar style={{ flex: 0, width: '64px' }}>
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
    </Sidebar>
  )
}

export default MissionListPageSidebar
