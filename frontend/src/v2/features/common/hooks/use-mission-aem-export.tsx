import { MissionExport } from '@common/types/mission-types'
import { logSoftError } from '@mtes-mct/monitor-ui'
import * as Sentry from '@sentry/react'
import { useState } from 'react'
import { useLazyMissionAEMExportMutation } from '../services/use-lazy-mission-aem-export.tsx'

interface ExportMissionAEMHook {
  exportIsLoading: boolean
  exportMission: () => Promise<void>
}

export function useMissionAEMExport(missionAEMExportInput?): ExportMissionAEMHook {
  const [getMissionAEM] = useLazyMissionAEMExportMutation()
  const [exportIsLoading, setExportIsLoading] = useState<boolean>(false)

  const handleDownload = (missionAEMExport?: MissionExport) => {
    if (missionAEMExport) {
      try {
        const content = missionAEMExport?.fileContent
        // Decode base64 string
        const decodedContent = atob(content)

        // Convert the decoded content to a Uint8Array
        const uint8Array = new Uint8Array(decodedContent.length)
        for (let i = 0; i < decodedContent.length; i++) {
          uint8Array[i] = decodedContent.charCodeAt(i)
        }

        // Create a Blob from the Uint8Array
        const blob = new Blob([uint8Array], { type: 'application/vnd.oasis.opendocument.spreadsheet' })

        // Create a temporary link element
        const link = document.createElement('a')
        link.href = window.URL.createObjectURL(blob)
        link.download = missionAEMExport.fileName

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

  const exportMissionAEM = async () => {
    setExportIsLoading(true)
    const { data, error } = await getMissionAEM({ variables: { missionAEMExportInput } })

    if (error) {
      setExportIsLoading(false)
      logSoftError({
        isSideWindowError: false,
        message: error.message,
        userMessage: `Le tableau AEM n'a pas pu être généré. Si l'erreur persiste, veuillez contacter l'équipe RapportNav/SNC3.`
      })
    } else if (data) {
      setExportIsLoading(false)
      handleDownload((data as any)?.missionAEMExportV2 as any)
    }
  }

  return { exportMissionAEM, exportIsLoading }
}
