import React from 'react'
import { Panel } from 'rsuite'
import { THEME } from '@mtes-mct/monitor-ui'

interface MissionOperationalSummaryProps {}

const MissionOperationalSummary: React.FC<MissionOperationalSummaryProps> = ({}) => {
  return (
    <Panel
      header={<p style={{ fontSize: '16px', fontWeight: 'bold' }}>Bilan opérationnel</p>}
      collapsible
      bordered
      style={{ backgroundColor: THEME.color.cultured, border: 0 }}
    >
      <div>todo</div>
    </Panel>
  )
}

export default MissionOperationalSummary
