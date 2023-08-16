// import { Form, useNavigate } from 'react-router-dom'
// import { Mission as MissionModel } from '../mission-types'
// import { useQuery } from '@tanstack/react-query'
// import { fetchMission, missionKeys, useMutateMission } from './queries'

export default function Mission() {
  return <div>mission</div>
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
