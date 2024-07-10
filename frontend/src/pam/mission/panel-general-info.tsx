import { DateRangePicker, Label, THEME } from '@mtes-mct/monitor-ui'
import React from 'react'
import { FlexboxGrid, Panel, Stack } from 'rsuite'
import { Mission } from '../../types/mission-types'
import Text from '../../ui/text'
import MissionCrew from './crew/mission-crew'
import MissionDistanceAndConsumption from './general-info/mission-distance-consumption'
import MissionObservationsUnit from './mission-observations-unit.tsx'
import MissionService from './mission-service'

interface MissionGeneralInfoPanelProps {
  mission: Mission
}

const MissionGeneralInfoPanel: React.FC<MissionGeneralInfoPanelProps> = ({ mission }) => {
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
      style={{ backgroundColor: THEME.color.cultured, border: 0, overflow: 'visible' }}
    >
      <FlexboxGrid style={{ width: '100%' }}>
        <FlexboxGrid.Item style={{ width: '100%' }}>
          <Stack direction="column" alignItems="flex-start" spacing="2rem" style={{ width: '100%' }}>
            <Stack.Item style={{ width: '100%' }}>
              <FlexboxGrid style={{ width: '100%' }}>
                <FlexboxGrid.Item colspan={20}>
                  <Label>Dates du rapport</Label>
                  <DateRangePicker
                    defaultValue={[mission.startDateTimeUtc || new Date(), mission.endDateTimeUtc || new Date()]}
                    // label="Dates du rapport"
                    withTime={true}
                    isCompact={true}
                    readOnly={true}
                    disabled={true}
                  />
                </FlexboxGrid.Item>
                <FlexboxGrid.Item colspan={4} style={{ display: 'flex', justifyContent: 'end' }}>
                  <MissionService
                    services={mission.services}
                    missionId={Number(mission.id)}
                    serviceId={mission.generalInfo?.serviceId}
                  />
                </FlexboxGrid.Item>
              </FlexboxGrid>
            </Stack.Item>
            <Stack.Item style={{ width: '100%' }}>
              <MissionCrew />
            </Stack.Item>
            <Stack.Item style={{ width: '100%' }}>
              <MissionDistanceAndConsumption info={mission.generalInfo} />
            </Stack.Item>
            <Stack.Item style={{ width: '100%' }}>
              <MissionObservationsUnit missionId={mission.id} observationsByUnit={mission.observationsByUnit} />
            </Stack.Item>
          </Stack>
        </FlexboxGrid.Item>
      </FlexboxGrid>
    </Panel>
  )
}

export default MissionGeneralInfoPanel
