import { useMutation, UseMutationResult, useQueryClient } from '@tanstack/react-query'

import * as Sentry from '@sentry/react'
import axios from '../../../../query-client/axios.ts'
import { resourcesKeys } from '../../common/services/query-keys.ts'
import { ControlUnitResource, ResourceInput } from '../../common/types/control-unit-types.ts'

const useManageUpsertResourceMutation = (
  serviceId?: number
): UseMutationResult<ControlUnitResource, Error, ResourceInput, unknown> => {
  const queryClient = useQueryClient()

  const upsertResource = (resources: ResourceInput): Promise<ControlUnitResource> => {
    return axios.put(`manage/services/${serviceId}/resources`, resources).then(response => response.data)
  }

  return useMutation({
    mutationFn: upsertResource,
    onSuccess: () => queryClient.invalidateQueries({ queryKey: resourcesKeys.all() }),
    onError: error => {
      Sentry.captureException(error)
      queryClient.invalidateQueries({ queryKey: resourcesKeys.all() })
    },
    scope: {
      id: `upsert-manage-resources`
    }
  })
}

export default useManageUpsertResourceMutation
