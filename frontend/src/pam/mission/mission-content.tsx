import { Accent, Button, Dialog, THEME } from '@mtes-mct/monitor-ui'
import find from 'lodash/find'
import React, { useMemo, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { Divider, FlexboxGrid, Stack } from 'rsuite'
import { Action, ActionStatusType } from '../../types/action-types'
import { ActionTypeEnum } from '../../types/env-mission-types'
import { Mission, VesselTypeEnum } from '../../types/mission-types'
import Text from '../../ui/text'
import { formatDateForServers, toLocalISOString } from '../../utils/dates.ts'
import { getComponentForAction } from './actions/action-mapping'
import ActionSelectionDropdown from './actions/action-selection-dropdown'
import useAddOrUpdateControl from './actions/use-add-update-action-control.tsx'
import ControlSelection from './controls/control-selection'
import MissionRecognizedVessel from './general-info/mission-recognized-vessel.tsx'
import MissionObservationsUnit from './mission-observations-unit.tsx'
import useAddOrUpdateNote from './notes/use-add-update-note.tsx'
import useAddAntiPollution from './others/anti-pollution/use-add-anti-pollution.tsx'
import useAddOrUpdateBAAEMPermanence from './others/baaem/use-add-baaem-permanence.tsx'
import useAddIllegalImmigration from './others/illegal-immigration/use-add-illegal-immigration.tsx'
import useAddNauticalEvent from './others/nautical-event/use-add-nautical-event.tsx'
import useAddOrUpdatePublicOrder from './others/public-order/use-add-public-order.tsx'
import useAddRepresentation from './others/representation/use-add-representation.tsx'
import useAddVigimer from './others/vigimer/use-add-vigimer.tsx'
import MissionGeneralInfoPanel from './panel-general-info'
import useAddOrUpdateRescue from './rescues/use-add-update-rescue.tsx'
import StatusSelectionDropdown from './status/status-selection-dropdown'
import useAddOrUpdateStatus from './status/use-add-update-status.tsx'
import MissionTimeline from './timeline/timeline'

export interface MissionProps {
  mission?: Mission
}

const MissionContent: React.FC<MissionProps> = ({ mission }) => {
  const { missionId, actionId } = useParams()

  let navigate = useNavigate()

  const [showControlTypesModal, setShowControlTypesModal] = useState<boolean>(false)

  const [addStatus, { loading: addStatusLoading }] = useAddOrUpdateStatus()
  const [addControl] = useAddOrUpdateControl()
  const [addFreeNote] = useAddOrUpdateNote()
  const [addActionRescue] = useAddOrUpdateRescue()
  const [addActionNauticalEvent] = useAddNauticalEvent()
  const [addActionVigimer] = useAddVigimer()
  const [addActionAntiPollution] = useAddAntiPollution()
  const [addActionBaaemPermanence] = useAddOrUpdateBAAEMPermanence()
  const [addActionPublicOrder] = useAddOrUpdatePublicOrder()
  const [addActionRepresentation] = useAddRepresentation()
  const [addActionIllegalImmigration] = useAddIllegalImmigration()

  const selectedAction = useMemo(() => {
    if (actionId) {
      return find(mission?.actions, { id: actionId })
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
    } else if (key === ActionTypeEnum.RESCUE) {
      await addNewRescue()
    } else if (key === ActionTypeEnum.NAUTICAL_EVENT) {
      await addNewNauticalEvent()
    } else if (key === ActionTypeEnum.VIGIMER) {
      await addNewOther(ActionTypeEnum.VIGIMER)
    } else if (key === ActionTypeEnum.ANTI_POLLUTION) {
      await addNewOther(ActionTypeEnum.ANTI_POLLUTION)
    } else if (key === ActionTypeEnum.BAAEM_PERMANENCE) {
      await addNewOther(ActionTypeEnum.BAAEM_PERMANENCE)
    } else if (key === ActionTypeEnum.PUBLIC_ORDER) {
      await addNewOther(ActionTypeEnum.PUBLIC_ORDER)
    } else if (key === ActionTypeEnum.REPRESENTATION) {
      await addNewOther(ActionTypeEnum.REPRESENTATION)
    } else if (key === ActionTypeEnum.ILLEGAL_IMMIGRATION) {
      await addNewOther(ActionTypeEnum.ILLEGAL_IMMIGRATION)
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

    const response = await addControl({ variables: { controlAction: newControl } })
    navigate(`/pam/missions/${missionId}/${response.data?.addOrUpdateControl.id}`)
  }
  const addNewFreeNote = async () => {
    setShowControlTypesModal(false)

    const newNote = {
      missionId: parseInt(missionId!, 10),
      startDateTimeUtc: formatDateForServers(toLocalISOString())
    }

    const response = await addFreeNote({ variables: { freeNoteAction: newNote } })
    navigate(`/pam/missions/${missionId}/${response.data?.addOrUpdateFreeNote.id}`)
  }

  const addNewRescue = async () => {
    setShowControlTypesModal(false)
    const newRescue = {
      missionId: parseInt(missionId!, 10),
      startDateTimeUtc: formatDateForServers(toLocalISOString()),
      endDateTimeUtc: formatDateForServers(toLocalISOString()),
      isPersonRescue: true
    }
    const response = await addActionRescue({ variables: { rescueAction: newRescue } })
    navigate(`/pam/missions/${missionId}/${response.data?.addOrUpdateActionRescue.id}`)
  }

  const addNewNauticalEvent = async () => {
    setShowControlTypesModal(false)
    const newNautical = {
      missionId: parseInt(missionId!, 10),
      startDateTimeUtc: formatDateForServers(toLocalISOString()),
      endDateTimeUtc: formatDateForServers(toLocalISOString())
    }
    const response = await addActionNauticalEvent({ variables: { nauticalAction: newNautical } })
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
        response = await addActionVigimer({ variables: { vigimerAction: newOther } })
        navigate(`/pam/missions/${missionId}/${response.data?.addOrUpdateActionVigimer.id}`)
        break
      case ActionTypeEnum.BAAEM_PERMANENCE:
        response = await addActionBaaemPermanence({ variables: { baaemPermanenceAction: newOther } })
        navigate(`/pam/missions/${missionId}/${response.data?.addOrUpdateActionBAAEMPermanence.id}`)
        break
      case ActionTypeEnum.ANTI_POLLUTION:
        response = await addActionAntiPollution({ variables: { antiPollutionAction: newOther } })
        navigate(`/pam/missions/${missionId}/${response.data?.addOrUpdateActionAntiPollution.id}`)
        break
      case ActionTypeEnum.PUBLIC_ORDER:
        response = await addActionPublicOrder({ variables: { publicOrderAction: newOther } })
        navigate(`/pam/missions/${missionId}/${response.data?.addOrUpdateActionPublicOrder.id}`)
        break
      case ActionTypeEnum.REPRESENTATION:
        response = await addActionRepresentation({ variables: { representationAction: newOther } })
        navigate(`/pam/missions/${missionId}/${response.data?.addOrUpdateActionRepresentation.id}`)
        break
      case ActionTypeEnum.ILLEGAL_IMMIGRATION:
        response = await addActionIllegalImmigration({ variables: { illegalImmigrationAction: newOther } })
        navigate(`/pam/missions/${missionId}/${response.data?.addOrUpdateActionIllegalImmigration.id}`)
        break
      default:
        break
    }
  }

  if (mission) {
    const MissionActionComponent = getComponentForAction(selectedAction)

    return (
      <>
        <FlexboxGrid justify="space-between" style={{ display: 'flex', flex: 1 }}>
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
              <Stack.Item style={{ width: '100%', padding: '1rem' }}>
                <MissionGeneralInfoPanel mission={mission} />
              </Stack.Item>
              {/*<Stack.Item style={{ width: '100%', padding: '1rem' }}>/!* <MissionActivityPanel /> *!/</Stack.Item>*/}
              {/*<Stack.Item style={{ width: '100%', padding: '1rem' }}>*/}
              {/*  /!* <MissionOperationalSummaryPanel /> *!/*/}
              {/*</Stack.Item>*/}
              {
                //TODO: nbrOfRecognizedVessel
              }
              <Stack.Item style={{ width: '100%', padding: '1rem' }}>
                <MissionRecognizedVessel missionId={mission.id} generalInfo={mission.generalInfo} />
              </Stack.Item>
              <Stack.Item style={{ width: '100%', padding: '1rem' }}>
                <MissionObservationsUnit missionId={mission.id} observationsByUnit={mission.observationsByUnit} />
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
              height: '2000px' // for having 100% height children
            }}
          >
            <FlexboxGrid>
              <FlexboxGrid.Item style={{ width: '100%' }}>
                <Stack direction={'row'} justifyContent={'space-between'} spacing={'0.5rem'} wrap={true}>
                  <Stack.Item>
                    <Stack direction={'row'} spacing={'0.5rem'}>
                      <Stack.Item>
                        <Text as="h2" weight="bold">
                          Actions réalisées en mission
                        </Text>
                      </Stack.Item>
                      <Stack.Item>
                        <ActionSelectionDropdown onSelect={addNewAction} />
                      </Stack.Item>
                    </Stack>
                  </Stack.Item>
                  <Stack.Item>
                    <Stack direction={'row'}>
                      <Stack.Item>
                        {/*<StatusSelectionDropdown onSelect={addNewStatus} loading={addStatusLoading} />*!/*/}
                      </Stack.Item>
                      <Stack.Item>
                        <StatusSelectionDropdown onSelect={addNewStatus} loading={addStatusLoading} />
                      </Stack.Item>
                    </Stack>
                  </Stack.Item>
                </Stack>
                <Divider style={{ backgroundColor: THEME.color.charcoal, marginTop: '2.5rem' }} />
              </FlexboxGrid.Item>
              <FlexboxGrid.Item
                style={{
                  width: '100%',
                  overflowY: 'auto',
                  minHeight: 'calc(100vh - 260px)',
                  maxHeight: 'calc(100vh - 260px)',
                  height: '2000px' // for having 100% height children
                }}
              >
                <MissionTimeline missionId={missionId} onSelectAction={selectAction} />
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
              height: '2000px' // for having 100% height children
            }}
          >
            <FlexboxGrid justify="start" align="middle" style={{ padding: '2rem', width: '100%' }}>
              {selectedAction && MissionActionComponent && (
                <MissionActionComponent action={selectedAction} misisonStatus={mission.status} />
              )}
            </FlexboxGrid>
          </FlexboxGrid.Item>
        </FlexboxGrid>
        <>
          {showControlTypesModal && (
            <Dialog>
              <Dialog.Title>Ajouter des contrôles</Dialog.Title>
              <Dialog.Body>
                <ControlSelection onSelect={addNewControl} />
              </Dialog.Body>
              <Dialog.Action style={{ justifyContent: 'flex-end', paddingRight: '1.5rem' }}>
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
