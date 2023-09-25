import React from 'react'
import { Panel } from 'rsuite'
import { THEME } from '@mtes-mct/monitor-ui'
import Title from '../../ui/title'

interface MissionOperationalSummaryProps {}

const MissionOperationalSummary: React.FC<MissionOperationalSummaryProps> = ({}) => {
  return (
    <Panel
      header={<Title as="h2">Bilan opérationnel</Title>}
      collapsible
      bordered
      style={{ backgroundColor: THEME.color.cultured, border: 0 }}
    >
      <div>todo</div>
    </Panel>
  )
}

export default MissionOperationalSummary
