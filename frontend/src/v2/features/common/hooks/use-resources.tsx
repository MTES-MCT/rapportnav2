import useResourcesQuery from '../services/use-resources.tsx'
import { ControlUnitResource } from '../types/control-unit-types.ts'

const useResources = () => {
  const { data } = useResourcesQuery()
  const getByControlUnit = (controlUnitId?: number) =>
    (data ?? [])?.filter((c: ControlUnitResource) => c.controlUnitId === controlUnitId)

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
