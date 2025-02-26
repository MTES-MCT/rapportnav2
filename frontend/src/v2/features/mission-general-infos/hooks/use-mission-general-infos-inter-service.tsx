import { useEffect, useState } from 'react'
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
  const [admins, setAdmins] = useState<FilteredAdministration[]>([])
  const [controlUnits, setControlUnits] = useState<ControlUnit[]>([])

  useEffect(() => {
    if (!administrations) return
    const admins = administrations.filter(a => !a.isArchived)
    setControlUnits(admins.flatMap(a => a.controlUnits).filter(c => !c.isArchived))
    setAdmins(admins.map(a => ({ id: a.id, name: a.name })))
  }, [administrations])
  return {
    admins,
    controlUnits
  }
}
