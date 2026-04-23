import { useMutation, UseMutationResult, useQueryClient } from '@tanstack/react-query'

import * as Sentry from '@sentry/react'
import axios from '../../../../query-client/axios.ts'
import { AdminFishAuction } from '../types/admin-fish-auction-types.ts'

const useAdminCreateOrUpdateFishAuctionMutation = (): UseMutationResult<
  AdminFishAuction,
  Error,
  AdminFishAuction,
  unknown
> => {
  const queryClient = useQueryClient()

  const createOrUpdateFishAuction = (key: AdminFishAuction): Promise<AdminFishAuction> => {
    const url = `admin/fish_auctions`
    if (key.id) {
      return axios.put(url, key).then(response => response.data)
    }
    return axios.post(url, key).then(response => response.data)
  }

  return useMutation({
    mutationFn: createOrUpdateFishAuction,
    onSettled: async () => {
      await queryClient.invalidateQueries({ queryKey: ['admin-fish-auctions'] })
    },
    onError: error => {
      Sentry.captureException(error)
    },
    scope: {
      id: `create-admin-fish-auctions`
    }
  })
}

export default useAdminCreateOrUpdateFishAuctionMutation
