import { useLazyMissionExportMutation } from '@common/services/use-lazy-mission-export'
import { MissionExport } from '@common/types/mission-types'
import { logSoftError } from '@mtes-mct/monitor-ui'
import * as Sentry from '@sentry/react'
import { useState } from 'react'

interface ExportMissionHook {
  exportIsLoading: boolean
  exportMission: () => Promise<void>
}

export function useMissionExport(missionId?: string): ExportMissionHook {
  const [getMissionReport] = useLazyMissionExportMutation()
  const [exportIsLoading, setExportIsLoading] = useState<boolean>(false)

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

  const exportMission = async () => {
    setExportIsLoading(true)
    const { data, error } = await getMissionReport({ variables: { missionId } })

    if (error) {
      setExportIsLoading(false)
      logSoftError({
        isSideWindowError: false,
        message: error.message,
        userMessage: `Le rapport n'a pas pu être généré. Si l'erreur persiste, veuillez contacter l'équipe RapportNav/SNC3.`
      })
    } else if (data) {
      setExportIsLoading(false)
      handleDownload((data as any)?.missionExport as any)
    }
  }

  return { exportMission, exportIsLoading }
}
