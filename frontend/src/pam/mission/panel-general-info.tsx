import React from 'react'
import { FlexboxGrid, Panel, Stack } from 'rsuite'
import { THEME, DateRangePicker } from '@mtes-mct/monitor-ui'
import Text from '../../ui/text'

interface MissionGeneralInfoPanelProps {
  startDate: string
  endDate: string
  crew?: any[]
  passengers?: any[]
}

const MissionGeneralInfoPanel: React.FC<MissionGeneralInfoPanelProps> = ({ startDate, endDate, crew, passengers }) => {
  return (
    <Panel
      header={
        <Text as="h2" weight="bold">
          Informations Générales
        </Text>
      }
      collapsible
      defaultExpanded
      bordered
      style={{ backgroundColor: THEME.color.cultured, border: 0 }}
    >
      <FlexboxGrid>
        <FlexboxGrid.Item>
          <Stack direction="column" alignItems="flex-start">
            <Stack.Item>
              <DateRangePicker
                defaultValue={[startDate || new Date(), endDate || new Date()]}
                label="Dates du rapport"
                withTime={true}
                isCompact={true}
                // disabled={true}
              />
            </Stack.Item>
          </Stack>
        </FlexboxGrid.Item>
      </FlexboxGrid>
    </Panel>
  )
}

export default MissionGeneralInfoPanel
