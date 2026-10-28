import { useCallback } from 'react'
import useResourcesQuery from '../services/use-resources.tsx'
import { ControlUnitResource } from '../types/control-unit-types.ts'

const useResources = () => {
  const { data } = useResourcesQuery()
  // Stable across renders (only changes when the query data changes) so callers can safely use the
  // returned list as a hook dependency without re-running effects on every render.
  const getByControlUnit = useCallback(
    (controlUnitId?: number) => (data ?? []).filter((c: ControlUnitResource) => c.controlUnitId === controlUnitId),
    [data]
  )

  const getResourcesOptions = (resources?: ControlUnitResource[]) =>
    (resources ?? data)?.map((resource: ControlUnitResource) => ({
      value: resource.id!,
      label: `${resource.name}`
    })) ?? []

  const getResourcesOptionsByControlUnitId = (controlUnitId?: number) =>
    getByControlUnit(controlUnitId)?.map((resource: ControlUnitResource) => ({
      label: `${resource.name}`,
      value: resource.id!?.toString()
    })) ?? []

  const getResourceById = (id?: number) => data?.find(d => d.id === id)

  return { getByControlUnit, resources: data, getResourceById, getResourcesOptions, getResourcesOptionsByControlUnitId }
}

export default useResources
