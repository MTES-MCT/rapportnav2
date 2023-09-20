import { Accent, Icon, Dialog, IconButton, Size, THEME } from '@mtes-mct/monitor-ui'

import { useNavigate, useParams } from 'react-router-dom'
import { Divider, FlexboxGrid, Panel, Stack } from 'rsuite'
import { fetchMission, missionKeys, useMutateMission } from './queries'
import { useQuery } from '@tanstack/react-query'
import MissionGeneralInfoPanel from './mission-general-info-panel'
import MissionOperationalSummary from './mission-operational-summary'
import MissionActivityPanel from './mission-activity-panel'
import MissionTimeline from './mission-timeline/mission-timeline'
import { useState } from 'react'
import { getComponentForAction } from './actions/action-mapping'
import Title from '../../ui/title'
import { ControlTarget, MissionAction } from '../mission-types'
import ActionSelectionDropdown from './actions/action-selection-dropdown'
import { ActionTypeEnum } from '../env-mission-types'
import ControlSelection from './controls/control-selection'

export default function Mission() {
  const { missionsId } = useParams()

  let navigate = useNavigate()
  const [selectedAction, setSelectedAction] = useState<MissionAction | undefined>(undefined)
  const [showControlTypesModal, setShowControlTypesModal] = useState<boolean>(false)

  const fetchQuery = useQuery({
    queryKey: missionKeys.detail(missionsId as any),
    queryFn: () => fetchMission(missionsId as any)
  })

  const selectMissionAction = (action: MissionAction) => {
    setSelectedAction(action)
  }

  const addNewAction = (key: ActionTypeEnum) => {
    if (key === ActionTypeEnum.CONTROL) {
      setShowControlTypesModal(true)
    }
  }

  const addNewControl = (controlType: string, targetType: ControlTarget) => {
    setShowControlTypesModal(false)
  }

  const mutationSuccessCallback = () => {
    console.log('mutation success')
    // navigate("/");
  }
  const mutationErrorCallback = () => {
    console.log('mutation failed')
    alert('error')
  }

  const updateQuery = useMutateMission(missionsId, mutationSuccessCallback, mutationErrorCallback)

  const handleSubmit = async (event: any) => {
    event.preventDefault()
    const { text, status } = fetchQuery.data as any
    const mission = { text, status, id: missionsId }
    // const fakeMission: MissionModel = {
    //   id: 1,
    //   text: mission!.text + Math.floor(Math.random() * 101),
    //   status: mission!.status
    // }
    // updateQuery.mutate(fakeMission, { onSuccess: () => navigate('/') })
  }
  if (fetchQuery.isLoading && fetchQuery.isFetching) {
    return <div>Loading...</div>
  }

  if (fetchQuery.data) {
    const mission = fetchQuery.data
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
                <MissionOperationalSummary />
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
                          Icon={Icon.Plus}
                          accent={Accent.PRIMARY}
                          size={Size.NORMAL}
                          color={THEME.color.gainsboro}
                        />
                      </Stack.Item>
                      <Stack.Item style={{ paddingLeft: '0.5rem' }}>
                        <IconButton
                          Icon={Icon.FleetSegment}
                          accent={Accent.PRIMARY}
                          size={Size.NORMAL}
                          color={THEME.color.gainsboro}
                        />
                      </Stack.Item>
                    </Stack>
                  </FlexboxGrid.Item>
                </FlexboxGrid>
              </FlexboxGrid.Item>
              <FlexboxGrid.Item style={{ width: '100%' }}>
                <Divider />
              </FlexboxGrid.Item>
              <FlexboxGrid.Item style={{ width: '100%' }}>
                <MissionTimeline mission={mission} onSelectAction={selectMissionAction} />
              </FlexboxGrid.Item>
            </FlexboxGrid>
          </FlexboxGrid.Item>
          <FlexboxGrid.Item colspan={8} style={{ backgroundColor: THEME.color.gainsboro, height: '100%' }}>
            <FlexboxGrid justify="center" align="middle">
              <div>
                {selectedAction && MissionActionComponent && <MissionActionComponent action={selectedAction} />}
              </div>
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
