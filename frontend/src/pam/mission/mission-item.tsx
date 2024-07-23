import React, { useState } from 'react'
import { Mission, MissionExport } from '../../types/mission-types.ts'
import { Accent, Button, Icon, logSoftError, Size, THEME } from '@mtes-mct/monitor-ui'
import { Link } from 'react-router-dom'
import { FlexboxGrid, Stack } from 'rsuite'
import { formatMissionName } from './utils.ts'
import MissionOpenByTag from './mission-open-by-tag.tsx'
import { formatDateForFrenchHumans } from '../../utils/dates.ts'
import MissionStatusTag from './mission-status-tag.tsx'
import MissionCompletenessForStatsTag from './mission-completeness-for-stats-tag.tsx'
import useIsMissionCompleteForStats from './use-is-mission-complete-for-stats.tsx'
import styled from 'styled-components'
import * as Sentry from '@sentry/react'
import useLazyMissionExport from './export/use-lazy-mission-export.tsx'
import useLazyMissionAEMExport from './export/use-lazy-mission-aem-export.tsx'

interface MissionItemProps {
  mission: Mission
  prefetchMission: (missionId: string) => void
}

const ListItemWithHover = styled.div`
  height: inherit;
  background-color: ${THEME.color.cultured};
  transition: background-color 0.3s ease;

  &:hover {
    background-color: ${THEME.color.blueGray25};
  }
`

const MissionItem: React.FC<MissionItemProps> = ({mission, prefetchMission}) => {

  const [getMissionReport] = useLazyMissionExport()
  const [getMissionAEMReport] = useLazyMissionAEMExport()
  const [exportLoading, setExportLoading] = useState<boolean>(false)

  const [exportationCanBeDisplayed, setExportationCanBeDisplayed] = useState<boolean>(false)
  const exportRapportEnabled = useIsMissionCompleteForStats(mission?.id)

  const handleDownload = (missionExport?: MissionExport) => {
    if (missionExport) {
      try {
        const content = missionExport?.fileContent
        // Decode base64 string
        const decodedContent = atob(content)

        // Convert the decoded content to a Uint8Array
        const uint8Array = new Uint8Array(decodedContent.length)
        for (let i = 0; i < decodedContent.length; i++) {
          uint8Array[i] = decodedContent.charCodeAt(i)
        }

        // Create a Blob from the Uint8Array
        const blob = new Blob([uint8Array], { type: 'application/vnd.oasis.opendocument.text' })

        // Create a temporary link element
        const link = document.createElement('a')
        link.href = window.URL.createObjectURL(blob)
        link.download = missionExport.fileName

        // Append the link to the document body and trigger a click event
        document.body.appendChild(link)
        link.click()

        // Remove the link element from the document body
        document.body.removeChild(link)
      } catch (error) {
        console.log('handleDownload error: ', error)
        Sentry.captureException(error)
      }
    }
  }

  const exportMission = async (missionId, isAEMExport: boolean = false) => {
    setExportLoading(true)

    let { data, error, loading, called } = await getMissionReport({ variables: { missionId } })


    if (isAEMExport) {
      let { data, error, loading, called } = await getMissionAEMReport({ variables: { missionId } })
    }

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

  const onItemMouseOver = () => {
    if (exportRapportEnabled) {
      setExportationCanBeDisplayed(true)
    }
  }

  const onItemMouseOut = () => {
    setExportationCanBeDisplayed(false)
  }

  return (
    <Stack onMouseOver={onItemMouseOver} onMouseOut={onItemMouseOut}>
      <Stack.Item
        key={mission.id}
        style={{ backgroundColor: THEME.color.cultured, width: '100%', height: '100%' }}>
        <ListItemWithHover>
          <Link
            to={`/pam/missions/${mission.id}`}
            style={{
              textDecoration: 'none'
            }}
            onMouseOver={() => {
              prefetchMission(mission.id)
            }}
          >
            <FlexboxGrid align="middle" style={{ height: '100%', padding: '0.5rem 1rem' }}>
              <FlexboxGrid.Item colspan={1} style={{ paddingTop: '8px' }}>
                <Icon.MissionAction size={28} color={THEME.color.charcoal} />
              </FlexboxGrid.Item>
              <FlexboxGrid.Item colspan={5}>
                <p style={{ color: THEME.color.charcoal, fontSize: '16px', fontWeight: 'bold' }}>
                  {formatMissionName(mission.startDateTimeUtc)}
                </p>
              </FlexboxGrid.Item>
              <FlexboxGrid.Item colspan={5}>
                <MissionOpenByTag missionSource={mission.missionSource} isFake={mission.openBy === 'fake'} />
              </FlexboxGrid.Item>
              <FlexboxGrid.Item colspan={3}>
                <p style={{ color: THEME.color.charcoal, fontSize: '13px' }}>
                  {' '}
                  {formatDateForFrenchHumans(mission.startDateTimeUtc)}
                </p>
              </FlexboxGrid.Item>
              <FlexboxGrid.Item colspan={4}>
                <MissionStatusTag status={mission.status} />
              </FlexboxGrid.Item>
              <FlexboxGrid.Item colspan={4}>
                <MissionCompletenessForStatsTag
                  missionStatus={mission.status}
                  completenessForStats={mission.completenessForStats?.status}
                />
              </FlexboxGrid.Item>
              <FlexboxGrid.Item colspan={1}></FlexboxGrid.Item>
              <FlexboxGrid.Item colspan={1}>
                <Icon.Edit size={20} style={{ color: THEME.color.charcoal }} />
              </FlexboxGrid.Item>
            </FlexboxGrid>
          </Link>
        </ListItemWithHover>
        {exportationCanBeDisplayed && (
          <FlexboxGrid >
            <FlexboxGrid.Item colspan={8} style={{ height: '100%', padding: '0.5rem 1rem' }}>
              <Button
                Icon={Icon.Download}
                accent={Accent.SECONDARY}
                size={Size.NORMAL}
                role={'dl-mission-export'}
                onClick={() => exportMission(mission.id)}
              >
                Exporter le rapport de la mission
              </Button>

            </FlexboxGrid.Item>

            <FlexboxGrid.Item colspan={8} style={{ height: '100%', padding: '0.5rem 1rem' }}>
              <Button
                Icon={Icon.Download}
                accent={Accent.PRIMARY}
                size={Size.NORMAL}
                role={'dl-mission-export'}
                onClick={() => exportMission(mission.id, true)}
              >
                Télécharger les tableaux
              </Button>
            </FlexboxGrid.Item>
          </FlexboxGrid>
        )}

      </Stack.Item>
    </Stack>

  )

}

export default MissionItem
