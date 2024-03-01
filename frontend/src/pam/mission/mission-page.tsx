import React from 'react'
import MissionContent from './mission-content.tsx'
import { useNavigate, useParams } from 'react-router-dom'
import MissionPageHeader from './page-header'
import MissionPageFooter from './page-footer'
import { useApolloClient } from '@apollo/client'
import useMissionExcerpt from "./general-info/use-mission-excerpt";
import { formatMissionName } from "./utils";
import { Stack } from "rsuite";
import Text from "../../ui/text.tsx";
import { Accent, Button, Size } from "@mtes-mct/monitor-ui";
import { getPath, PAM_HOME_PATH } from "../../router/router.tsx";

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
    return (
      <div
        style={{
          margin: 0,
          marginTop: '10rem',
          display: 'flex',
          flexDirection: 'column',
          minHeight: '100vh',
          maxHeight: '100vh'
        }}
      >
        <Stack justifyContent={"center"} direction={"column"} spacing={"2rem"}>
          <Stack.Item style={{maxWidth: '33%'}}>
            <Text as={"h2"}>
              Une erreur est survenue
            </Text>
            <Text as={"h3"}>
              {error?.message}
            </Text>
          </Stack.Item>
          <Stack.Item>
            <Button
              accent={Accent.PRIMARY}
              size={Size.LARGE}
              onClick={() => navigate(getPath(PAM_HOME_PATH))}
            >
              Retourner à l'accueil
            </Button>
          </Stack.Item>
        </Stack>
      </div>
    )
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
