import { useMutation, UseMutationResult, useQueryClient } from '@tanstack/react-query'

import * as Sentry from '@sentry/react'
import axios from '../../../../query-client/axios.ts'
import { inquiriesKeys } from '../../common/services/query-keys.ts'
import { Inquiry } from '../../common/types/inquiry.ts'

const useCreateInquiryMutation = (): UseMutationResult<Inquiry, Error, Inquiry, unknown> => {
  const queryClient = useQueryClient()

  const createInquiry = (inquiry: Inquiry): Promise<Inquiry> =>
    axios.post(`/inquiries`, inquiry).then(response => response.data)

  return useMutation({
    mutationFn: createInquiry,
    onSuccess: (data: Inquiry) => {
      queryClient.invalidateQueries({ queryKey: inquiriesKeys.all() })
      if (data.id) queryClient.invalidateQueries({ queryKey: inquiriesKeys.byId(data.id) })
    },
    onError: error => {
      Sentry.captureException(error)
    },
    scope: {
      id: 'create-inquiry'
    }
  })
}

export default useCreateInquiryMutation
