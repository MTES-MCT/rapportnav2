import React from 'react'
import { Panel } from 'rsuite'
import { THEME } from '@mtes-mct/monitor-ui'

interface MissionActivityPanelProps {}

const MissionActivityPanel: React.FC<MissionActivityPanelProps> = ({}) => {
  return (
    <Panel
      header={<p style={{ fontSize: '16px', fontWeight: 'bold' }}>Activité du navire</p>}
      collapsible
      bordered
      style={{ backgroundColor: THEME.color.cultured, border: 0 }}
    >
      <div>todo</div>
    </Panel>
  )
}

export default MissionActivityPanel
