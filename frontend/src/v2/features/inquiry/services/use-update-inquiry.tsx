import { useMutation, UseMutationResult, useQueryClient } from '@tanstack/react-query'

import * as Sentry from '@sentry/react'
import axios from '../../../../query-client/axios.ts'
import { inquiriesKeys } from '../../common/services/query-keys.ts'
import { Inquiry } from '../../common/types/inquiry.ts'

const useUpdateInquiryMutation = (inquiryId?: string): UseMutationResult<Inquiry, Error, Inquiry, unknown> => {
  const queryClient = useQueryClient()

  const updateInquiry = (inquiry: Inquiry): Promise<Inquiry> =>
    axios.put(`/inquiries/${inquiryId}`, inquiry).then(response => response.data)

  return useMutation({
    mutationFn: updateInquiry,
    onSuccess: (data: Inquiry) => {
      queryClient.invalidateQueries({ queryKey: inquiriesKeys.all() })
      if (data.id) queryClient.invalidateQueries({ queryKey: inquiriesKeys.byId(data.id) })
    },
    onError: error => {
      Sentry.captureException(error)
    },
    scope: {
      id: 'update-inquiry'
    }
  })
}

export default useUpdateInquiryMutation
