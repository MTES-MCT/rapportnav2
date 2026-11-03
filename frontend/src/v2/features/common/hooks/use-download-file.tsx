import { MissionExport } from '../types/mission-export-types.ts'
import * as Sentry from '@sentry/react'

export enum BLOBTYPE {
  ODS = 'application/vnd.oasis.opendocument.spreadsheet',
  ODT = 'application/vnd.oasis.opendocument.text',
  ZIP = 'application/zip',
  DEFAULT = 'application/octet-stream'
}

// Some browsers cancel the download when the object url is revoked synchronously after the click
const REVOKE_OBJECT_URL_DELAY_MS = 1000

// Triggers a browser download for an in-memory file
export const downloadBlob = (blob: Blob, fileName: string): void => {
  const url = window.URL.createObjectURL(blob)
  const link = document.createElement('a')
  link.href = url
  link.download = fileName
  document.body.appendChild(link)
  link.click()
  document.body.removeChild(link)
  setTimeout(() => window.URL.revokeObjectURL(url), REVOKE_OBJECT_URL_DELAY_MS)
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

      downloadBlob(blob, fileName || 'download')
    } catch (error) {
      console.error('Error during file download:', { error, fileToDownload })
      Sentry.captureException(error)
    }
  }

  return { handleDownload }
}
