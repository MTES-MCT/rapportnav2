import React, { useState } from 'react'
import MissionContent from './mission-content.tsx'
import { useNavigate, useParams } from 'react-router-dom'
import MissionPageHeader from './page-header'
import MissionPageFooter from './page-footer'
import { useApolloClient } from '@apollo/client'
import useMissionExcerpt from "./general-info/use-mission-excerpt";
import useLazyMissionExport from "./export/use-lazy-mission-export.tsx";
import { Stack } from "rsuite";
import Text from "../../ui/text.tsx";
import { Accent, Button, logSoftError, Size } from "@mtes-mct/monitor-ui";
import { getPath, PAM_HOME_PATH } from "../../router/router.tsx";
import { MissionExport } from "../../types/mission-types.ts";
import * as Sentry from "@sentry/react";

const MissionPage: React.FC = () => {

  const navigate = useNavigate()
  let {missionId} = useParams()
  const apolloClient = useApolloClient()

  const [exportLoading, setExportLoading] = useState<boolean>(false)
  const {loading, error, data: mission} = useMissionExcerpt(missionId)
  const [getMissionReport] = useLazyMissionExport()


  const exitMission = async () => {
    // TODO centralise the following into a class - also used in use-auth()
    // reset apollo store
    await apolloClient.resetStore()
    // flush apollo persist cache
    apolloClient.cache.evict({})

    navigate('..')
  }

  const handleDownload = (missionExport?: MissionExport) => {
    if (missionExport) {
      try {
        const content = missionExport?.fileContent
        // Decode base64 string
        const decodedContent = atob(content);

        // Convert the decoded content to a Uint8Array
        const uint8Array = new Uint8Array(decodedContent.length);
        for (let i = 0; i < decodedContent.length; i++) {
          uint8Array[i] = decodedContent.charCodeAt(i);
        }

        // Create a Blob from the Uint8Array
        const blob = new Blob([uint8Array], {type: 'application/vnd.oasis.opendocument.text'});

        // Create a temporary link element
        const link = document.createElement('a');
        link.href = window.URL.createObjectURL(blob);
        link.download = missionExport.fileName

        // Append the link to the document body and trigger a click event
        document.body.appendChild(link);
        link.click();

        // Remove the link element from the document body
        document.body.removeChild(link);
      } catch (error) {
        console.log('handleDownload error: ', error)
        Sentry.captureException(error);
      }
    }
  };

  const exportMission = async () => {
    setExportLoading(true)
    const {data, error, loading, called} = await getMissionReport({variables: {missionId}})

    if (error) {
      setExportLoading(false)
      logSoftError({
        isSideWindowError: false,
        message: error.message,
        userMessage: `Le rapport n'a pas pu être généré. Si l'erreur persiste, veuillez contacter l'équipe RapportNav/SNC3.`
      })
    } else if (data) {
      setExportLoading(false)
      handleDownload((data as any)?.missionExport as any)
    }

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
      <MissionPageHeader mission={mission}
                         onClickClose={exitMission}
                         onClickExport={exportMission}
                         exportLoading={exportLoading}
      />

      <MissionContent mission={mission}/>

      <MissionPageFooter missionName={`Mission #${missionId}`} exitMission={exitMission}/>
    </div>
  )
}

export default MissionPage
