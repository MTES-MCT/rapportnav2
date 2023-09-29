import React from 'react'
import { Panel } from 'rsuite'
import { THEME } from '@mtes-mct/monitor-ui'
import Title from '../../ui/title'

interface MissionActivityPanelProps {}

const MissionActivityPanel: React.FC<MissionActivityPanelProps> = ({}) => {
  return (
    <Panel
      header={<Title as="h2">Activité du navire</Title>}
      collapsible
      bordered
      style={{ backgroundColor: THEME.color.cultured, border: 0 }}
    >
      <div>todo</div>
    </Panel>
  )
}

export default MissionActivityPanel
