import {Accent, Icon, Dialog, IconButton, Size, THEME, Button} from '@mtes-mct/monitor-ui'
import {Divider, FlexboxGrid, Stack} from 'rsuite'
import {useNavigate, useParams} from 'react-router-dom'
import MissionGeneralInfoPanel from './panel-general-info'
import MissionOperationalSummaryPanel from './panel-operational-summary'
import MissionActivityPanel from './panel-activity'
import MissionTimeline from './timeline/timeline'
import {useMemo, useState} from 'react'
import {getComponentForAction} from './actions/action-mapping'
import Text from '../../ui/text'
import {VesselTypeEnum} from '../../types/mission-types'
import {Action, ActionStatusType} from '../../types/action-types'
import ActionSelectionDropdown from './actions/action-selection-dropdown'
import {ActionTypeEnum} from '../../types/env-mission-types'
import ControlSelection from './controls/control-selection'
import {useMutation} from '@apollo/client'
import {
    MUTATION_ADD_OR_UPDATE_ACTION_CONTROL,
    MUTATION_ADD_OR_UPDATE_ACTION_STATUS
} from './queries'
import StatusSelectionDropdown from './status/status-selection-dropdown'
import find from 'lodash/find'
import {GET_MISSION_TIMELINE} from "./timeline/use-mission-timeline.tsx";
import useMissionExcerpt from "./general-info/use-mission-excerpt.tsx";

export default function Mission() {
    const {missionId, actionId} = useParams()

    let navigate = useNavigate()

    const [showControlTypesModal, setShowControlTypesModal] = useState<boolean>(false)

    const {loading, error, data: mission} = useMissionExcerpt(missionId)

    const [addStatus, {statusMutationResponse}] = useMutation(MUTATION_ADD_OR_UPDATE_ACTION_STATUS, {
        refetchQueries: [GET_MISSION_TIMELINE]
    })
    const [addControl, {controlMutationResponse}] = useMutation(MUTATION_ADD_OR_UPDATE_ACTION_CONTROL, {
        refetchQueries: [GET_MISSION_TIMELINE]
    })

    const selectedAction = useMemo(() => {
        if (actionId) {
            return find(mission?.actions, {id: actionId})
        }
    }, [mission, actionId])

    const selectAction = (action: Action) => {
        navigate(`/pam/missions/${missionId}/${action.id}`)
        // setSelectedAction(action)
    }

    const addNewAction = (key: ActionTypeEnum) => {
        if (key === ActionTypeEnum.CONTROL) {
            setShowControlTypesModal(true)
        }
    }

    const addNewStatus = async (key: ActionStatusType) => {
        const newActionData = {
            missionId: parseInt(missionId!, 10),
            status: key,
            isStart: true,
            reason: null,
            observations: null
        }

        const response = await addStatus({
            variables: {
                statusAction: newActionData
            }
        })

        // TODO change this
        navigate(`/pam/missions/${missionId}/${response.data.addOrUpdateStatus.id}`)
    }

    const addNewControl = async (controlMethod: string, vesselType: VesselTypeEnum) => {
        setShowControlTypesModal(false)
        // TODO id creation should be in backend
        const newControl = {
            missionId: parseInt(missionId!, 10),
            controlMethod,
            vesselType
        }

        const response = await addControl({variables: {controlAction: newControl}})
        debugger
        navigate(`/pam/missions/${missionId}/${response.data.addOrUpdateControl.id}`)
    }

    if (loading) {
        return <div>Loading...</div>
    }

    if (mission) {
        const MissionActionComponent = getComponentForAction(selectedAction)

        return (
            <>
                <FlexboxGrid justify="space-between" style={{display: 'flex', flex: 1}}>
                    <FlexboxGrid.Item
                        colspan={8}
                        style={{
                            flex: 1,
                            overflowY: 'auto',
                            minHeight: 'calc(100vh - 2 * 60px)',
                            maxHeight: 'calc(100vh - 2 * 60px)'
                        }}
                    >
                        <Stack direction="column">
                            <Stack.Item style={{width: '100%', padding: '1rem'}}>
                                <MissionGeneralInfoPanel mission={mission}/>
                            </Stack.Item>
                            <Stack.Item
                                style={{width: '100%', padding: '1rem'}}>{/* <MissionActivityPanel /> */}</Stack.Item>
                            <Stack.Item style={{width: '100%', padding: '1rem'}}>
                                {/* <MissionOperationalSummaryPanel /> */}
                            </Stack.Item>
                        </Stack>
                    </FlexboxGrid.Item>
                    <FlexboxGrid.Item
                        colspan={8}
                        style={{
                            backgroundColor: THEME.color.cultured,
                            padding: '2rem',
                            flex: 1,
                            overflowY: 'hidden',
                            minHeight: 'calc(100vh - 2 * 60px)',
                            maxHeight: 'calc(100vh - 2 * 60px)'
                        }}
                    >
                        <FlexboxGrid>
                            <FlexboxGrid.Item style={{width: '100%'}}>
                                <FlexboxGrid justify="space-between">
                                    <FlexboxGrid.Item>
                                        <Stack alignItems="center">
                                            <Stack.Item>
                                                <Text as="h2" weight="bold">
                                                    Actions réalisées en mission
                                                </Text>
                                            </Stack.Item>
                                            <Stack.Item style={{paddingLeft: '0.5rem'}}>
                                                <ActionSelectionDropdown onSelect={addNewAction}/>
                                            </Stack.Item>
                                        </Stack>
                                    </FlexboxGrid.Item>
                                    <FlexboxGrid.Item>
                                        <Stack>
                                            {/* <Stack.Item>
                        <IconButton
                          Icon={Icon.Phone}
                          accent={Accent.PRIMARY}
                          size={Size.NORMAL}
                        />
                      </Stack.Item> */}
                                            <Stack.Item style={{paddingLeft: '0.5rem'}}>
                                                <StatusSelectionDropdown onSelect={addNewStatus}/>
                                            </Stack.Item>
                                        </Stack>
                                    </FlexboxGrid.Item>
                                </FlexboxGrid>
                                <Divider style={{backgroundColor: THEME.color.charcoal}}/>
                            </FlexboxGrid.Item>
                            <FlexboxGrid.Item style={{
                                width: '100%',
                                overflowY: 'auto',
                                minHeight: 'calc(100vh - 260px)',
                                maxHeight: 'calc(100vh - 260px)'
                            }}>
                                <MissionTimeline missionId={missionId} onSelectAction={selectAction}/>
                            </FlexboxGrid.Item>
                        </FlexboxGrid>
                    </FlexboxGrid.Item>
                    <FlexboxGrid.Item
                        colspan={8}
                        style={{
                            backgroundColor: THEME.color.gainsboro,
                            flex: 1,
                            overflowY: 'auto',
                            minHeight: 'calc(100vh - 2 * 60px)',
                            maxHeight: 'calc(100vh - 2 * 60px)'
                        }}
                    >
                        <FlexboxGrid justify="start" align="middle" style={{padding: '2rem', width: '100%'}}>
                            {selectedAction && MissionActionComponent &&
                                <MissionActionComponent action={selectedAction}/>}
                        </FlexboxGrid>
                    </FlexboxGrid.Item>
                </FlexboxGrid>
                <>
                    {showControlTypesModal && (
                        <Dialog>
                            <Dialog.Title>Ajouter des contrôles</Dialog.Title>
                            <Dialog.Body>
                                <ControlSelection onSelect={addNewControl}/>
                            </Dialog.Body>
                            <Dialog.Action>
                                <Button accent={Accent.SECONDARY} onClick={() => setShowControlTypesModal(false)}>
                                    Fermer
                                </Button>
                            </Dialog.Action>
                        </Dialog>
                    )}
                </>
            </>
        )
    }
}
