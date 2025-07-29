import { useMutation, UseMutationResult, useQueryClient } from '@tanstack/react-query'

import axios from '../../../../query-client/axios.ts'
import { inquiriesKeys } from '../../common/services/query-keys.ts'

const useDeleteInquiryMutation = (inquiryId?: string): UseMutationResult<void, Error, void, unknown> => {
  const queryClient = useQueryClient()

  const deleteInquiry = (): Promise<void> => axios.delete(`inquiries/${inquiryId}`).then(response => response.data)

  const mutation = useMutation({
    mutationFn: deleteInquiry,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: inquiriesKeys.all() })
      if (inquiryId) queryClient.invalidateQueries({ queryKey: inquiriesKeys.byId(inquiryId) })
    },
    scope: {
      id: 'delete-inquiry'
    }
  })
  return mutation
}

export default useDeleteInquiryMutation
