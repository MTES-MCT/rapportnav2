import { MissionExport } from '@common/types/mission-types'
import * as Sentry from '@sentry/react'

export enum BLOBTYPE {
  ODS = 'application/vnd.oasis.opendocument.spreadsheet',
  ODT = 'application/vnd.oasis.opendocument.text',
  ZIP = 'application/zip',
  DEFAULT = 'application/octet-stream'
}

interface DownloadFileHook {
  handleDownload: (blobType: BLOBTYPE, fileToDownload?: MissionExport) => Promise<void>
}

export function useDownloadFile(): DownloadFileHook {
  const handleDownload = async (blobType: BLOBTYPE, fileToDownload?: MissionExport) => {
    if (!fileToDownload) {
      console.warn('No file provided for download.')
      return
    }

    try {
      const { fileContent, fileName } = fileToDownload

      // Decode Base64 string to binary data
      const decodedContent = atob(fileContent)

      // Convert binary string to Uint8Array
      const uint8Array = new Uint8Array(decodedContent.length)
      for (let i = 0; i < decodedContent.length; i++) {
        uint8Array[i] = decodedContent.charCodeAt(i)
      }

      // Create a Blob object for the file
      const blob = new Blob([uint8Array], { type: blobType || BLOBTYPE.DEFAULT })

      // Generate a temporary URL for the Blob
      const url = window.URL.createObjectURL(blob)

      // Create an anchor element and trigger the download
      const link = document.createElement('a')
      link.href = url
      link.download = fileName || 'download'
      document.body.appendChild(link)
      link.click()
      document.body.removeChild(link)

      // Revoke the Blob URL to release memory
      window.URL.revokeObjectURL(url)
    } catch (error) {
      console.error('Error during file download:', { error, fileToDownload })
      Sentry.captureException(error)
    }
  }

  return { handleDownload }
}
