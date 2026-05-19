import { useMemo } from 'react'
import { Administration, ControlUnit } from '../../common/types/control-unit-types'

type FilteredAdministration = {
  id: number
  name: string
}

type MissionGeneralInfosInterServiceHook = {
  controlUnits: ControlUnit[]
  admins: FilteredAdministration[]
}

export function useMissionGeneralInfosInterService(
  administrations: Administration[]
): MissionGeneralInfosInterServiceHook {
  const admins = useMemo<FilteredAdministration[]>(() => {
    if (!administrations) return []
    return administrations.filter(a => !a.isArchived).map(a => ({ id: a.id, name: a.name }))
  }, [administrations])

  const controlUnits = useMemo<ControlUnit[]>(() => {
    if (!administrations) return []
    return administrations.filter(a => !a.isArchived).flatMap(a => a.controlUnits).filter(c => !c.isArchived)
  }, [administrations])

  return {
    admins,
    controlUnits
  }
}
