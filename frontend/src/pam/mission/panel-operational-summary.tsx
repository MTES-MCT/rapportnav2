import React from 'react'
import { Panel } from 'rsuite'
import { THEME } from '@mtes-mct/monitor-ui'
import Text from '../../ui/text'

interface MissionOperationalSummaryPanelProps {
}

const MissionOperationalSummaryPanel: React.FC<MissionOperationalSummaryPanelProps> = () => {
  return (
    <Panel
      header={
        <Text as="h2" weight="bold">
          Bilan opérationnel
        </Text>
      }
      collapsible
      bordered
      style={{backgroundColor: THEME.color.cultured, border: 0}}
    >
      <div>todo</div>
    </Panel>
  )
}

export default MissionOperationalSummaryPanel
