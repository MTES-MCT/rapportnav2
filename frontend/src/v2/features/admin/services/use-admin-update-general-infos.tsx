import { useMutation, UseMutationResult, useQueryClient } from '@tanstack/react-query'

import * as Sentry from '@sentry/react'
import axios from '../../../../query-client/axios.ts'
import { AdminGeneralInfos } from '../types/admin-general-infos-types.ts'

const useAdminUpdateGeneralInfosMutation = (): UseMutationResult<
  AdminGeneralInfos,
  Error,
  AdminGeneralInfos,
  unknown
> => {
  const queryClient = useQueryClient()

  const createOrUpdateGeneralInfos = (data: AdminGeneralInfos): Promise<AdminGeneralInfos> => {
    const url = `admin/general-infos`
    return axios.put(url, data).then(response => response.data)
  }

  return useMutation({
    mutationFn: createOrUpdateGeneralInfos,
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['admin-general-infos'] }),
    onError: error => {
      Sentry.captureException(error)
      queryClient.invalidateQueries({ queryKey: ['admin-general-infos'] })
    },
    scope: {
      id: `create-admin-general-infos`
    }
  })
}

export default useAdminUpdateGeneralInfosMutation
