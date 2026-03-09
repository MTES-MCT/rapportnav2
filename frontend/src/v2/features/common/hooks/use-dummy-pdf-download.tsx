import { logSoftError } from '@mtes-mct/monitor-ui'
import { useState } from 'react'
import useDummyPdfExport from '../services/use-dummy-pdf-export'
import { BLOBTYPE, useDownloadFile } from './use-download-file'

interface DummyPdfDownloadHook {
  isLoading: boolean
  downloadDummyPdf: () => Promise<void>
}

export function useDummyPdfDownload(): DummyPdfDownloadHook {
  const { handleDownload } = useDownloadFile()
  const { refetch } = useDummyPdfExport()
  const [isLoading, setIsLoading] = useState<boolean>(false)

  const downloadDummyPdf = async () => {
    setIsLoading(true)
    try {
      const { data, error } = await refetch()

      if (error) {
        logSoftError({
          isSideWindowError: false,
          message: error.message,
          userMessage: `Le PDF de test n'a pas pu être généré. Si l'erreur persiste, veuillez contacter l'équipe RapportNav/SNC3.`
        })
      } else if (data) {
        await handleDownload(BLOBTYPE.PDF, data)
      }
    } catch (err) {
      logSoftError({
        isSideWindowError: false,
        message: err instanceof Error ? err.message : 'Unknown error',
        userMessage: `Le PDF de test n'a pas pu être généré. Si l'erreur persiste, veuillez contacter l'équipe RapportNav/SNC3.`
      })
    } finally {
      setIsLoading(false)
    }
  }

  return { downloadDummyPdf, isLoading }
}
