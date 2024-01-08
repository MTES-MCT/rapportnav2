import React from 'react'
import MissionContent from './mission-content.tsx'
import { useNavigate, useParams } from 'react-router-dom'
import MissionPageHeader from './page-header'
import MissionPageFooter from './page-footer'
import { useApolloClient } from '@apollo/client'
import useMissionExcerpt from "./general-info/use-mission-excerpt";
import { formatMissionName } from "./utils";

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
    return <div>Chargement...</div>
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

      <MissionContent mission={mission}/>

      <MissionPageFooter missionName={`Mission #${missionId}`} exitMission={exitMission}/>
    </div>
  )
}

export default MissionPage
