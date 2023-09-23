import React from 'react'
import { FlexboxGrid, Panel, Stack } from 'rsuite'
import { THEME, DatePicker } from '@mtes-mct/monitor-ui'
import Title from '../../ui/title'

interface MissionGeneralInfoPanelProps {
  startDate: string
  endDate: string
  crew?: any[]
  passengers?: any[]
}

const MissionGeneralInfoPanel: React.FC<MissionGeneralInfoPanelProps> = ({ startDate, endDate, crew, passengers }) => {
  return (
    <Panel
      header={<Title as="h2">Informations Générales</Title>}
      collapsible
      defaultExpanded
      bordered
      style={{ backgroundColor: THEME.color.cultured, border: 0 }}
    >
      <FlexboxGrid>
        <FlexboxGrid.Item>
          <Stack direction="column" alignItems="flex-start">
            <Stack.Item style={{ paddingBottom: '0.5rem' }}>
              <Title as="h3" color={THEME.color.slateGray}>
                Dates du rapport
              </Title>
            </Stack.Item>
            <Stack.Item>
              <Stack direction="row">
                <Stack.Item>
                  <DatePicker
                    defaultValue={startDate}
                    error=""
                    onChange={function noRefCheck() {}}
                    withTime
                    isCompact
                  />
                </Stack.Item>
                <Stack.Item style={{ paddingLeft: '0.5rem' }}>au</Stack.Item>
                <Stack.Item style={{ paddingLeft: '0.5rem' }}>
                  <DatePicker defaultValue={endDate} error="" onChange={function noRefCheck() {}} withTime isCompact />
                </Stack.Item>
              </Stack>
            </Stack.Item>
          </Stack>
        </FlexboxGrid.Item>
      </FlexboxGrid>
    </Panel>
  )
}

export default MissionGeneralInfoPanel
