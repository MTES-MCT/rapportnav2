import { useMutation, UseMutationResult } from '@tanstack/react-query'

import axios from '../../../../query-client/axios'
import { downloadBlob } from '../../common/hooks/use-download-file.tsx'

type SatiPdfExportInput = {
  ownerId: string
  actionId: string
}

// With `responseType: 'blob'` the ProblemDetail of an error also comes back as a Blob,
// so the axios interceptor cannot read it: parse it here to keep a readable error message.
const withProblemDetail = async (error: any): Promise<never> => {
  const data = error?.response?.data
  if (data instanceof Blob && data.type.includes('json')) {
    try {
      const problemDetail = JSON.parse(await data.text())
      error.problemDetail = problemDetail
      if (problemDetail?.detail) error.message = problemDetail.detail
    } catch {
      // keep the original error
    }
  }
  throw error
}

// Kept separate from the download so that a future pdf preview can reuse it
export const fetchSatiPdf = ({ ownerId, actionId }: SatiPdfExportInput): Promise<Blob> =>
  axios
    .get<Blob>(`owners/${ownerId}/actions/${actionId}/sati/pdf`, {
      responseType: 'blob',
      headers: { Accept: 'application/pdf, application/problem+json' }
    })
    .then(response => response.data)
    .catch(withProblemDetail)

const useSatiPdfExport = (): UseMutationResult<void, Error, SatiPdfExportInput, unknown> =>
  useMutation({
    mutationFn: async (input: SatiPdfExportInput) =>
      downloadBlob(await fetchSatiPdf(input), `sati-${input.actionId}.pdf`),
    // a download must not be queued while offline and replayed later
    networkMode: 'always'
  })

export default useSatiPdfExport
