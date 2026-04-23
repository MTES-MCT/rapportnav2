import { useMutation, UseMutationResult, useQueryClient } from '@tanstack/react-query'

import * as Sentry from '@sentry/react'
import axios from '../../../../query-client/axios.ts'

const useAdminDisableFishAuctionMutation = (): UseMutationResult<void, Error, number, unknown> => {
  const queryClient = useQueryClient()

  const disableFishAuction = (id?: number): Promise<void> => {
    return axios.delete(`admin/fish_auctions/${id}`)
  }

  return useMutation({
    mutationFn: disableFishAuction,
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['admin-fish-auctions'] }),
    onError: error => {
      Sentry.captureException(error)
      queryClient.invalidateQueries({ queryKey: ['admin-fish-auctions'] })
    },
    scope: {
      id: `disable-admin-fish-auction`
    }
  })
}

export default useAdminDisableFishAuctionMutation
