import { MissionExport } from '@common/types/mission-types'
import * as Sentry from '@sentry/react'

export enum BLOBTYPE {
  ODS = 'application/vnd.oasis.opendocument.spreadsheet',
  ODT = 'application/vnd.oasis.opendocument.text',
  ZIP = 'application/zip'
}

interface DownloadFileHook {
  handleDownload: (blobType: string, fileToDownload?: MissionExport) => Promise<void>
}

export function useDownloadFile(): DownloadFileHook {
  const handleDownload = async (blobType: string, fileToDownload?: MissionExport) => {
    if (!fileToDownload) return

    try {
      const { fileContent, fileName } = fileToDownload

      // Decode Base64
      const decodedContent = atob(fileContent)

      // Convert to Uint8Array
      const uint8Array = new Uint8Array(decodedContent.length)
      for (let i = 0; i < decodedContent.length; i++) {
        uint8Array[i] = decodedContent.charCodeAt(i)
      }

      // Create Blob
      const blob = new Blob([uint8Array], { type: blobType || 'application/octet-stream' })

      // Create temporary link and trigger download
      const url = window.URL.createObjectURL(blob)
      const link = document.createElement('a')
      link.href = url
      link.download = fileName

      document.body.appendChild(link)
      link.click()
      document.body.removeChild(link)

      // Revoke Object URL to prevent memory leaks
      window.URL.revokeObjectURL(url)
    } catch (error) {
      console.error('handleDownload error:', { error, fileToDownload })
      Sentry.captureException(error)
    }
  }

  return { handleDownload }
}
