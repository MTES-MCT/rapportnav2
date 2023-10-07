import { Accent, Icon, Dialog, IconButton, Size, THEME } from '@mtes-mct/monitor-ui'
import { v4 as uuidv4 } from 'uuid'
import { Divider, FlexboxGrid, Stack } from 'rsuite'
import { useNavigate, useParams } from 'react-router-dom'
import MissionGeneralInfoPanel from './panel-general-info'
import MissionOperationalSummaryPanel from './panel-operational-summary'
import MissionActivityPanel from './panel-activity'
import MissionTimeline from './timeline/timeline'
import { useState } from 'react'
import { getComponentForAction } from './actions/action-mapping'
import Title from '../../ui/title'
import { ControlTarget, Action, ActionStatusType, VesselType } from '../mission-types'
import ActionSelectionDropdown from './actions/action-selection-dropdown'
import { ActionTypeEnum, MissionSourceEnum } from '../env-mission-types'
import ControlSelection from './controls/control-selection'
import { useApolloClient, useMutation, useQuery } from '@apollo/client'
import {
  GET_MISSION_BY_ID,
  MUTATION_ADD_OR_UPDATE_ACTION_CONTROL,
  MUTATION_ADD_OR_UPDATE_ACTION_STATUS
} from './queries'
import StatusSelectionDropdown from './status/status-selection-dropdown'

export default function Mission() {
  const { missionId } = useParams()

  const apolloClient = useApolloClient()
  const [selectedAction, setSelectedAction] = useState<Action | undefined>(undefined)
  const [showControlTypesModal, setShowControlTypesModal] = useState<boolean>(false)

  const { loading, error, data, refetch, updateQuery } = useQuery(GET_MISSION_BY_ID, {
    variables: { missionId },
    fetchPolicy: 'cache-only'
  })

  const [addStatus] = useMutation(MUTATION_ADD_OR_UPDATE_ACTION_STATUS, {
    refetchQueries: ['GetMissionById']
  })
  const [addControl] = useMutation(MUTATION_ADD_OR_UPDATE_ACTION_CONTROL, {
    refetchQueries: ['GetMissionById']
  })

  const selectAction = (action: Action) => {
    setSelectedAction(action)
  }

  const resetSelectedAction = () => setSelectedAction(undefined)

  const addNewAction = (key: ActionTypeEnum) => {
    if (key === ActionTypeEnum.CONTROL) {
      setShowControlTypesModal(true)
    }
  }

  const addNewStatus = async (key: ActionStatusType) => {
    const uuid = uuidv4()
    const date = new Date().toISOString()
    const newActionData = {
      id: uuid,
      missionId: parseInt(missionId!, 10),
      status: key,
      startDateTimeUtc: date,
      isStart: true,
      reason: null,
      observations: null
    }
    const newAction = {
      id: uuid,
      type: ActionTypeEnum.STATUS,
      source: MissionSourceEnum.RAPPORTNAV,
      status: key,
      startDateTimeUtc: date,
      endDateTimeUtc: null,
      data: {
        ...newActionData
      }
    }

    addStatus({
      variables: {
        statusAction: newActionData
      }
    })

    setSelectedAction(newAction as any)
  }

  const addNewControl = (controlMethod: string, vesselType: VesselType) => {
    setShowControlTypesModal(false)
    debugger
    const uuid = uuidv4()
    const date = new Date().toISOString()
    const newActionData = {
      id: uuid,
      missionId: parseInt(missionId!, 10),
      startDateTimeUtc: date,
      controlMethod,
      vesselType,
      vesselIdentifier: null,
      vesselSize: null,
      identityControlledPerson: null,
      observations: null
    }
    const newAction = {
      id: uuid,
      type: ActionTypeEnum.CONTROL,
      source: MissionSourceEnum.RAPPORTNAV,
      startDateTimeUtc: date,
      endDateTimeUtc: null,
      data: {
        ...newActionData
      }
    }

    addControl({ variables: { controlAction: newActionData } })
    setSelectedAction(newAction as any)
  }

  if (loading) {
    return <div>Loading...</div>
  }

  if (data) {
    const mission = data.mission
    const MissionActionComponent = getComponentForAction(selectedAction)

    return (
      <>
        <FlexboxGrid justify="space-between" style={{ display: 'flex', flex: 1 }}>
          <FlexboxGrid.Item colspan={8} style={{ height: '100%' }}>
            <Stack direction="column">
              <Stack.Item style={{ width: '100%', padding: '1rem' }}>
                <MissionGeneralInfoPanel startDate={mission.startDateTimeUtc} endDate={mission.endDateTimeUtc} />
              </Stack.Item>
              <Stack.Item style={{ width: '100%', padding: '1rem' }}>
                <MissionActivityPanel />
              </Stack.Item>
              <Stack.Item style={{ width: '100%', padding: '1rem' }}>
                <MissionOperationalSummaryPanel />
              </Stack.Item>
            </Stack>
          </FlexboxGrid.Item>
          <FlexboxGrid.Item
            colspan={8}
            style={{ backgroundColor: THEME.color.cultured, height: '100%', padding: '2rem 1rem' }}
          >
            <FlexboxGrid>
              <FlexboxGrid.Item style={{ width: '100%' }}>
                <FlexboxGrid justify="space-between">
                  <FlexboxGrid.Item>
                    <Stack alignItems="center">
                      <Stack.Item>
                        <Title as="h2">Actions réalisées en mission</Title>
                      </Stack.Item>
                      <Stack.Item style={{ paddingLeft: '0.5rem' }}>
                        <ActionSelectionDropdown onSelect={addNewAction} />
                      </Stack.Item>
                    </Stack>
                  </FlexboxGrid.Item>
                  <FlexboxGrid.Item>
                    <Stack>
                      <Stack.Item>
                        <IconButton
                          Icon={Icon.Phone}
                          accent={Accent.PRIMARY}
                          size={Size.NORMAL}
                          color={THEME.color.gainsboro}
                        />
                      </Stack.Item>
                      <Stack.Item style={{ paddingLeft: '0.5rem' }}>
                        <StatusSelectionDropdown onSelect={addNewStatus} />
                      </Stack.Item>
                    </Stack>
                  </FlexboxGrid.Item>
                </FlexboxGrid>
              </FlexboxGrid.Item>
              <FlexboxGrid.Item style={{ width: '100%' }}>
                <Divider />
              </FlexboxGrid.Item>
              <FlexboxGrid.Item style={{ width: '100%' }}>
                <MissionTimeline mission={mission} onSelectAction={selectAction} />
              </FlexboxGrid.Item>
            </FlexboxGrid>
          </FlexboxGrid.Item>
          <FlexboxGrid.Item colspan={8} style={{ backgroundColor: THEME.color.gainsboro, height: '100%' }}>
            <FlexboxGrid justify="start" align="middle" style={{ padding: '2rem' }}>
              {selectedAction && MissionActionComponent && (
                <MissionActionComponent action={selectedAction} resetSelectedAction={resetSelectedAction} />
              )}
            </FlexboxGrid>
          </FlexboxGrid.Item>
        </FlexboxGrid>
        <>
          {showControlTypesModal && (
            <Dialog>
              <Dialog.Body>
                <ControlSelection onSelect={addNewControl} />
              </Dialog.Body>
            </Dialog>
          )}
        </>
      </>
    )
  }
}
