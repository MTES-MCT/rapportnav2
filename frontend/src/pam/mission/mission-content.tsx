import { Accent, Button, Dialog, THEME } from '@mtes-mct/monitor-ui'
import { Divider, FlexboxGrid, Stack } from 'rsuite'
import { useNavigate, useParams } from 'react-router-dom'
import MissionGeneralInfoPanel from './panel-general-info'
import MissionTimeline from './timeline/timeline'
import React, { useMemo, useState } from 'react'
import { getComponentForAction } from './actions/action-mapping'
import Text from '../../ui/text'
import { Mission, VesselTypeEnum } from '../../types/mission-types'
import { Action, ActionStatusType } from '../../types/action-types'
import ActionSelectionDropdown from './actions/action-selection-dropdown'
import { ActionTypeEnum } from '../../types/env-mission-types'
import ControlSelection from './controls/control-selection'
import StatusSelectionDropdown from './status/status-selection-dropdown'
import find from 'lodash/find'
import { formatDateForServers, toLocalISOString } from "../../utils/dates.ts";
import useAddOrUpdateControl from "./actions/use-add-update-action-control.tsx";
import useAddOrUpdateStatus from "./status/use-add-update-status.tsx";
import useAddOrUpdateNote from "./notes/use-add-update-note.tsx";
import useAddOrUpdateRescue from './rescues/use-add-update-rescue.tsx'
import useAddNauticalEvent from './others/nautical-event/use-add-nautical-event.tsx'
import useAddVigimer from './others/vigimer/use-add-vigimer.tsx'
import useAddAntiPollution from './others/anti-pollution/use-add-anti-pollution.tsx'
import useAddOrUpdateBAAEMPermanence from './others/baaem/use-add-baaem-permanence.tsx'

export interface MissionProps {
    mission?: Mission
}

const MissionContent: React.FC<MissionProps> = ({mission}) => {
    const {missionId, actionId} = useParams()

    let navigate = useNavigate()

    const [showControlTypesModal, setShowControlTypesModal] = useState<boolean>(false)


    const [addStatus, {loading: addStatusLoading}] = useAddOrUpdateStatus()
    const [addControl] = useAddOrUpdateControl()
    const [addFreeNote] = useAddOrUpdateNote()
    const [addActionRescue] = useAddOrUpdateRescue()
    const [addActionNauticalEvent] = useAddNauticalEvent()
    const [addActionVigimer] = useAddVigimer()
    const [addActionAntiPollution] = useAddAntiPollution()
    const [addActionBaaemPermanence] = useAddOrUpdateBAAEMPermanence();

    const selectedAction = useMemo(() => {
        if (actionId) {
            return find(mission?.actions, {id: actionId})
        }
    }, [mission, actionId])

    const selectAction = (action: Action) => {
        navigate(`/pam/missions/${missionId}/${action.id}`)
    }

    const addNewAction = async (key: ActionTypeEnum) => {
        if (key === ActionTypeEnum.CONTROL) {
            setShowControlTypesModal(true)
        } else if (key === ActionTypeEnum.NOTE) {
            await addNewFreeNote()
        }
        else if (key === ActionTypeEnum.RESCUE) {
          await addNewRescue()
        }
        else if (key === ActionTypeEnum.NAUTICAL_EVENT) {
          await addNewNauticalEvent()
        }
        else if (key === ActionTypeEnum.VIGIMER) {
          await addNewOther(ActionTypeEnum.VIGIMER)
        }
        else if (key === ActionTypeEnum.ANTI_POLLUTION) {
          await addNewOther(ActionTypeEnum.ANTI_POLLUTION)
        }
        else if (key === ActionTypeEnum.BAAEM_PERMANENCE) {
          await addNewOther(ActionTypeEnum.BAAEM_PERMANENCE)
        }
    }

    const addNewStatus = async (key: ActionStatusType) => {
        const newActionData = {
            missionId: parseInt(missionId!, 10),
            startDateTimeUtc: formatDateForServers(toLocalISOString()),
            status: key,
            reason: null,
            observations: null
        }

        const response = await addStatus({
            variables: {
                statusAction: newActionData
            }
        })

        // TODO change this
        navigate(`/pam/missions/${missionId}/${response.data?.addOrUpdateStatus.id}`)
    }

    const addNewControl = async (controlMethod: string, vesselType: VesselTypeEnum) => {
        setShowControlTypesModal(false)

        const newControl = {
            missionId: parseInt(missionId!, 10),
            startDateTimeUtc: formatDateForServers(toLocalISOString()),
            endDateTimeUtc: formatDateForServers(toLocalISOString()),
            controlMethod,
            vesselType
        }

        const response = await addControl({variables: {controlAction: newControl}})
        navigate(`/pam/missions/${missionId}/${response.data?.addOrUpdateControl.id}`)
    }
    const addNewFreeNote = async () => {
        setShowControlTypesModal(false)

        const newNote = {
            missionId: parseInt(missionId!, 10),
            startDateTimeUtc: formatDateForServers(toLocalISOString())
        }

        const response = await addFreeNote({variables: {freeNoteAction: newNote}})
        navigate(`/pam/missions/${missionId}/${response.data?.addOrUpdateFreeNote.id}`)
    }

    const addNewRescue = async () => {
      setShowControlTypesModal(false)
      const newRescue = {
        missionId: parseInt(missionId!, 10),
        startDateTimeUtc: formatDateForServers(toLocalISOString()),
        endDateTimeUtc: formatDateForServers(toLocalISOString()),
        isVesselRescue: false,
        isPersonRescue: false,
        isVesselNoticed: false,
        longitude: 0.0,
        latitude: 0.0,
        isInSRRorFollowedByCROSSMRCC: false,
        isVesselTowed: false,
        numberPersonsRescued: 0,
        numberOfDeaths: 0,
        operationFollowsDEFREP: false
      }
      const response = await addActionRescue({variables: {rescueAction: newRescue}})
      navigate(`/pam/missions/${missionId}/${response.data?.addOrUpdateActionRescue.id}`)
    }

  const addNewNauticalEvent = async () => {
    setShowControlTypesModal(false)
    const newNautical = {
      missionId: parseInt(missionId!, 10),
      startDateTimeUtc: formatDateForServers(toLocalISOString()),
      endDateTimeUtc: formatDateForServers(toLocalISOString())
    }
    const response = await addActionNauticalEvent({variables: {nauticalAction: newNautical}})
    navigate(`/pam/missions/${missionId}/${response.data?.addOrUpdateActionNauticalEvent.id}`)
  }

  const addNewOther = async (type: ActionTypeEnum) => {
    setShowControlTypesModal(false)
    const newOther = {
      missionId: parseInt(missionId!, 10),
      startDateTimeUtc: formatDateForServers(toLocalISOString()),
      endDateTimeUtc: formatDateForServers(toLocalISOString())
    }

    let response
    switch (type) {
      case ActionTypeEnum.VIGIMER:
         response = await addActionVigimer({variables: {vigimerAction: newOther}})
        navigate(`/pam/missions/${missionId}/${response.data?.addOrUpdateActionVigimer.id}`)
        break
      case ActionTypeEnum.BAAEM_PERMANENCE:
         response = await addActionBaaemPermanence({variables: {baaemPermanenceAction: newOther}})
        navigate(`/pam/missions/${missionId}/${response.data?.addOrUpdateActionBAAEMPermanence.id}`)
        break
      case ActionTypeEnum.ANTI_POLLUTION:
         response = await addActionAntiPollution({variables: {antiPollutionAction: newOther}})
        navigate(`/pam/missions/${missionId}/${response.data?.addOrUpdateActionAntiPollution.id}`)
        break
      default:
        break
    }

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
                            maxHeight: 'calc(100vh - 2 * 60px)',
                            height: '2000px', // for having 100% height children
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
                                                <StatusSelectionDropdown onSelect={addNewStatus}
                                                                         loading={addStatusLoading}/>
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
                                maxHeight: 'calc(100vh - 260px)',
                                height: '2000px', // for having 100% height children
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
                            maxHeight: 'calc(100vh - 2 * 60px)',
                            height: '2000px', // for having 100% height children
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
                            <Dialog.Action style={{justifyContent: 'flex-end', paddingRight: '1.5rem'}}>
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

export default MissionContent
