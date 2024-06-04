import { DateRangePicker, Label, THEME } from '@mtes-mct/monitor-ui'
import React from 'react'
import { FlexboxGrid, Panel, Stack } from 'rsuite'
import { Mission } from '../../types/mission-types'
import Text from '../../ui/text'
import MissionCrew from './crew/mission-crew'
import MissionDistanceAndConsumption from './general-info/mission-distance-consumption'
import MissionService from './mission-service'
import usePatchMissionEnv, { PatchMissionEnvInput } from './use-patch-mission-env.tsx'

interface MissionGeneralInfoPanelProps {
  mission: Mission
}

const MissionGeneralInfoPanel: React.FC<MissionGeneralInfoPanelProps> = ({ mission }) => {
  const [mutateEnvMission] = usePatchMissionEnv()

  const onChange = async (value: any) => {
    debugger
    const startDateTimeUtc = value[0].toISOString()
    const endDateTimeUtc = value[1].toISOString()
    const data: PatchMissionEnvInput = {
      missionId: mission.id,
      startDateTimeUtc,
      endDateTimeUtc
    }
    await mutateEnvMission({ variables: { mission: data } })
  }

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
                    onChange={async (nextValue?: [Date, Date] | [string, string]) => {
                      await onChange(nextValue)
                    }}
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
          </Stack>
        </FlexboxGrid.Item>
      </FlexboxGrid>
    </Panel>
  )
}

export default MissionGeneralInfoPanel
