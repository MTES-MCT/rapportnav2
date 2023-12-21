import React from 'react'
import MissionComponent from './mission-component.tsx'
import { useNavigate, useParams } from 'react-router-dom'
import MissionPageHeader from './page-header'
import MissionPageFooter from './page-footer'
import { useApolloClient } from '@apollo/client'
import useMissionExcerpt from "./general-info/use-mission-excerpt.tsx";
import { formatMissionName } from "./utils.ts";

const MissionPage: React.FC = () => {
  const navigate = useNavigate()
  let {missionId} = useParams()
  const apolloClient = useApolloClient()

  const {loading, error, data: mission} = useMissionExcerpt(missionId)


  const exitMission = async () => {
    // TODO centralise the following into a class - also used in use-auth()
    // reset apollo store
    await apolloClient.resetStore()
    // flush apollo persist cache
    apolloClient.cache.evict({})

    navigate('..')
  }

  if (loading) {
    return <div>Loading...</div>
  }

  if (error) {
    return <div>error...</div>
  }

  return (
    <div
      style={{
        margin: 0,
        display: 'flex',
        flexDirection: 'column',
        minHeight: '100vh',
        maxHeight: '100vh'
      }}
    >
      <MissionPageHeader missionName={formatMissionName(mission?.startDateTimeUtc)}
                         missionSource={mission?.missionSource}
                         onClickClose={exitMission}/>

      <MissionComponent mission={mission}/>

      <MissionPageFooter missionName={`Mission #${missionId}`} exitMission={exitMission}/>
    </div>
  )
}

export default MissionPage
