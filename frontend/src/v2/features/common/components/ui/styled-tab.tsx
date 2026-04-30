import { IconProps } from '@mtes-mct/monitor-ui'
import { createElement, FC, FunctionComponent, useEffect, useState } from 'react'
import { Tabs } from 'rsuite'

export type StyledTabItem = {
  key: string
  title: string
  isEditable?: () => boolean
  component: FunctionComponent<any>
  icon?: FunctionComponent<IconProps> | undefined
}

interface StyledTabProps {
  params: any
  items: StyledTabItem[]
  defaultActiveKey: string
}

const StyledTabs: FC<StyledTabProps> = ({ items, params, defaultActiveKey }) => {
  const [activeKey, setActiveKey] = useState<string>('')
  useEffect(() => {
    if (!defaultActiveKey) return
    setActiveKey(defaultActiveKey)
  }, [defaultActiveKey])

  return (
    <Tabs
      activeKey={activeKey}
      className="styled-tabs"
      onSelect={setActiveKey}
      defaultActiveKey={defaultActiveKey}
      style={{ width: '100%', height: '100%' }}
    >
      {items.map(item => (
        <Tabs.Tab
          eventKey={item.key}
          title={item.title}
          icon={
            item.icon ? (
              <div
                style={{
                  width: 20,
                  height: 20,
                  color: 'white',
                  display: 'flex',
                  borderRadius: '50%',
                  alignItems: 'center',
                  justifyContent: 'center',
                  backgroundColor: '#5697D2'
                }}
              >
                {createElement<IconProps>(item.icon, { size: 15 })}
              </div>
            ) : undefined
          }
        >
          {createElement<any>(item.component, params)}
        </Tabs.Tab>
      ))}
    </Tabs>
  )
}

export default StyledTabs
