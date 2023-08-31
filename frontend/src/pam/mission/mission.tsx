// import { Form, useNavigate } from 'react-router-dom'
// import { Mission as MissionModel } from '../mission-types'
// import { useQuery } from '@tanstack/react-query'
// import { fetchMission, missionKeys, useMutateMission } from './queries'
import { THEME } from '@mtes-mct/monitor-ui'

import { useParams } from 'react-router-dom'
import { FlexboxGrid, Panel, Stack } from 'rsuite'

export default function Mission() {
  let { missionsId } = useParams()
  return (
    <FlexboxGrid justify="space-between" style={{ display: 'flex', flex: 1 }}>
      <FlexboxGrid.Item colspan={8} style={{ height: '100%' }}>
        <Stack direction="column">
          <Stack.Item style={{ width: '100%', padding: '1rem' }}>
            <Panel
              header={<p style={{ fontSize: '16px', fontWeight: 'bold' }}>Informations Générales</p>}
              collapsible
              defaultExpanded
              bordered
              style={{ backgroundColor: THEME.color.cultured, border: 0 }}
            >
              <div>todo</div>
            </Panel>
          </Stack.Item>
          <Stack.Item style={{ width: '100%', padding: '1rem' }}>
            <Panel
              header={<p style={{ fontSize: '16px', fontWeight: 'bold' }}>Activité du navire</p>}
              collapsible
              bordered
              style={{ backgroundColor: THEME.color.cultured, border: 0 }}
            >
              <div>todo</div>
            </Panel>
          </Stack.Item>
          <Stack.Item style={{ width: '100%', padding: '1rem' }}>
            <Panel
              header={<p style={{ fontSize: '16px', fontWeight: 'bold' }}>Bilan opérationnel</p>}
              collapsible
              bordered
              style={{ backgroundColor: THEME.color.cultured, border: 0 }}
            >
              <div>todo</div>
            </Panel>
          </Stack.Item>
        </Stack>
      </FlexboxGrid.Item>
      <FlexboxGrid.Item colspan={8} style={{ backgroundColor: THEME.color.cultured, height: '100%' }}>
        <FlexboxGrid justify="center" align="middle">
          millieu
        </FlexboxGrid>
      </FlexboxGrid.Item>
      <FlexboxGrid.Item colspan={8} style={{ backgroundColor: THEME.color.gainsboro, height: '100%' }}>
        <FlexboxGrid justify="center" align="middle">
          droite
        </FlexboxGrid>
      </FlexboxGrid.Item>
    </FlexboxGrid>
  )
  // const MISSION_ID = 1

  // const fetchQuery = useQuery({ queryKey: missionKeys.detail(MISSION_ID), queryFn: () => fetchMission(MISSION_ID) })

  // // const fetchQuery = useQuery(
  // //   [FETCH_MISSION_QUERY_KEY, MISSION_ID],
  // //   () => fetchMission(MISSION_ID), {
  // //   initialData: () => {
  // //       // Use a todo from the 'todos' query as the initial data for this todo query
  // //       debugger
  // //       const missionFromCache = queryClient.getQueryData(FETCH_MISSION_QUERY_KEY) as any
  // //       return missionFromCache?.find((mission: MissionModel) => mission.id === MISSION_ID)
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

  // const updateQuery = useMutateMission(MISSION_ID, mutationSuccessCallback, mutationErrorCallback)

  // const handleSubmit = async (event: any) => {
  //   event.preventDefault()
  //   const { text, status } = fetchQuery.data as any
  //   const mission = { text, status, id: MISSION_ID }
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
