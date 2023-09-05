import React from 'react'
import { Panel } from 'rsuite'
import { THEME } from '@mtes-mct/monitor-ui'

interface MissionCrewPanelProps {}

const MissionCrewPanel: React.FC<MissionCrewPanelProps> = ({}) => {
  return (
    <Panel
      header={<p style={{ fontSize: '16px', fontWeight: 'bold' }}>Informations Générales</p>}
      collapsible
      defaultExpanded
      bordered
      style={{ backgroundColor: THEME.color.cultured, border: 0 }}
    >
      <div>todo</div>
    </Panel>
  )
}

export default MissionCrewPanel
