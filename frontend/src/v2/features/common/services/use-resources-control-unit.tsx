import { ControlUnitResource } from '../types/control-unit-types.ts'
import useResourcesQuery from './use-resources.tsx'

const useResourceByControlUnitQuery = (controlUnitId?: number) => {
  const { data } = useResourcesQuery()
  return {
    resources: (data ?? [])?.filter((c: ControlUnitResource) => c.controlUnitId === controlUnitId)
  }
}

export default useResourceByControlUnitQuery
