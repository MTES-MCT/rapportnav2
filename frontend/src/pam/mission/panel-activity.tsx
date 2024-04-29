import React from 'react'
import { Panel } from 'rsuite'
import { THEME } from '@mtes-mct/monitor-ui'
import Text from '../../ui/text'

interface MissionActivityPanelProps {}

const MissionActivityPanel: React.FC<MissionActivityPanelProps> = () => {
  return (
    <Panel
      header={
        <Text as="h2" weight="bold">
          Activité du navire
        </Text>
      }
      collapsible
      bordered
      style={{ backgroundColor: THEME.color.cultured, border: 0 }}
    >
      <div>todo</div>
    </Panel>
  )
}

export default MissionActivityPanel
