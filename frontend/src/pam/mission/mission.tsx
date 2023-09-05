// import { Form, useNavigate } from 'react-router-dom'
// import { Mission as MissionModel } from '../mission-types'
// import { useQuery } from '@tanstack/react-query'
// import { fetchMission, missionKeys, useMutateMission } from './queries'
import { THEME } from '@mtes-mct/monitor-ui'

import { useNavigate, useParams } from 'react-router-dom'
import { FlexboxGrid, Panel, Stack } from 'rsuite'
import { fetchMission, missionKeys, useMutateMission } from './queries'
import { useQuery } from '@tanstack/react-query'
import MissionCrewPanel from './mission-crew-panel'
import MissionOperationalSummary from './mission-operational-summary'
import MissionActivityPanel from './mission-activity-panel'
import MissionTimeline from './mission-timeline'
import { EnvAction } from '../mission-types'
import { useState } from 'react'
import { getComponentForAction } from './mission-actions/mission-action-mapping'

export default function Mission() {
  const { missionsId } = useParams()

  const [selectedAction, setSelectedAction] = useState<EnvAction | undefined>(undefined)

  const fetchQuery = useQuery({
    queryKey: missionKeys.detail(missionsId as any),
    queryFn: () => fetchMission(missionsId as any)
  })

  const selectMissionAction = (action: EnvAction) => {
    setSelectedAction(action)
  }

  let navigate = useNavigate()
  const mutationSuccessCallback = () => {
    console.log('mutation success')
    debugger
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
      <FlexboxGrid justify="space-between" style={{ display: 'flex', flex: 1 }}>
        <FlexboxGrid.Item colspan={8} style={{ height: '100%' }}>
          <Stack direction="column">
            <Stack.Item style={{ width: '100%', padding: '1rem' }}>
              <MissionCrewPanel />
            </Stack.Item>
            <Stack.Item style={{ width: '100%', padding: '1rem' }}>
              <MissionActivityPanel />
            </Stack.Item>
            <Stack.Item style={{ width: '100%', padding: '1rem' }}>
              <MissionOperationalSummary />
            </Stack.Item>
          </Stack>
        </FlexboxGrid.Item>
        <FlexboxGrid.Item colspan={8} style={{ backgroundColor: THEME.color.cultured, height: '100%' }}>
          <FlexboxGrid justify="center" align="middle">
            <div>
              <MissionTimeline mission={mission} onSelectAction={selectMissionAction} />
            </div>
          </FlexboxGrid>
        </FlexboxGrid.Item>
        <FlexboxGrid.Item colspan={8} style={{ backgroundColor: THEME.color.gainsboro, height: '100%' }}>
          <FlexboxGrid justify="center" align="middle">
            <div>{selectedAction && MissionActionComponent && <MissionActionComponent action={selectedAction} />}</div>
          </FlexboxGrid>
        </FlexboxGrid.Item>
      </FlexboxGrid>
    )
  }
  // const missionsId = 1

  // const fetchQuery = useQuery({ queryKey: missionKeys.detail(missionsId), queryFn: () => fetchMission(missionsId) })

  // // const fetchQuery = useQuery(
  // //   [FETCH_MISSION_QUERY_KEY, missionsId],
  // //   () => fetchMission(missionsId), {
  // //   initialData: () => {
  // //       // Use a todo from the 'todos' query as the initial data for this todo query
  // //       debugger
  // //       const missionFromCache = queryClient.getQueryData(FETCH_MISSION_QUERY_KEY) as any
  // //       return missionFromCache?.find((mission: MissionModel) => mission.id === missionsId)
  // //     },
  // // })

  // let navigate = useNavigate()
  // const mutationSuccessCallback = () => {
  //   console.log('mutation success')
  //   debugger
  //   // navigate("/");
  // }
  // const mutationErrorCallback = () => {
  //   console.log('mutation failed')
  //   alert('error')
  // }

  // const updateQuery = useMutateMission(missionsId, mutationSuccessCallback, mutationErrorCallback)

  // const handleSubmit = async (event: any) => {
  //   event.preventDefault()
  //   const { text, status } = fetchQuery.data as any
  //   const mission = { text, status, id: missionsId }
  //   // const fakeMission: MissionModel = {
  //   //   id: 1,
  //   //   text: mission!.text + Math.floor(Math.random() * 101),
  //   //   status: mission!.status
  //   // }
  //   // updateQuery.mutate(fakeMission, { onSuccess: () => navigate('/') })
  // }

  // if (fetchQuery.isLoading && fetchQuery.isFetching) {
  //   return <div>Loading...</div>
  // }

  // if (fetchQuery.data) {
  //   const { text, status } = fetchQuery.data as any
  //   const mission = { text, status, id: 1 }
  //   return (
  //     <div id="mission">
  //       <div>
  //         <h1>Mission #{mission.id}</h1>

  //         <div>
  //           <p>MissionText: {mission.text}</p>
  //         </div>

  //         <div>
  //           <Form action="edit" onSubmit={handleSubmit}>
  //             <button type="submit">Edit</button>
  //           </Form>
  //         </div>
  //       </div>
  //     </div>
  //   )
  // }

  // return null
}
