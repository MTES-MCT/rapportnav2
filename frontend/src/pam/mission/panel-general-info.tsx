import React from 'react'
import {FlexboxGrid, Panel, Stack} from 'rsuite'
import {THEME, DateRangePicker, Label, TextInput} from '@mtes-mct/monitor-ui'
import Text from '../../ui/text'
import {Mission} from '../../types/mission-types'
import MissionDistanceAndConsumption from './general-info/mission-distance-consumption'
import MissionCrew from './crew/mission-crew'

interface MissionGeneralInfoPanelProps {
    mission: Mission
}

const MissionGeneralInfoPanel: React.FC<MissionGeneralInfoPanelProps> = ({mission}) => {
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
            style={{backgroundColor: THEME.color.cultured, border: 0, overflow: 'visible'}}
        >
            <FlexboxGrid style={{width: '100%'}}>
                <FlexboxGrid.Item style={{width: '100%'}}>
                    <Stack direction="column" alignItems="flex-start" spacing="2rem" style={{width: '100%'}}>
                        <Stack.Item style={{width: '100%'}}>
                            <Label>Dates du rapport</Label>
                            <DateRangePicker
                                defaultValue={[mission.startDateTimeUtc || new Date(), mission.endDateTimeUtc || new Date()]}
                                // label="Dates du rapport"
                                withTime={true}
                                isCompact={true}
                                readOnly={true}
                                disabled={true}
                            />
                        </Stack.Item>
                        <Stack.Item style={{width: '100%'}}>
                            <MissionCrew/>
                        </Stack.Item>
                        <Stack.Item style={{width: '100%'}}>
                            <MissionDistanceAndConsumption info={mission.generalInfo}/>
                        </Stack.Item>
                    </Stack>
                </FlexboxGrid.Item>
            </FlexboxGrid>
        </Panel>
    )
}

export default MissionGeneralInfoPanel
