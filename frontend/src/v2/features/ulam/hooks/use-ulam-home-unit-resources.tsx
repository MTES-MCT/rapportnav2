import { ControlUnit } from '@mtes-mct/monitor-ui'
import ControlUnitResourceType = ControlUnit.ControlUnitResourceType

type ResourceUnitTypeRegistry = { [key in ControlUnitResourceType]: string }

const RESOURCE_UNIT_REGISTRY: ResourceUnitTypeRegistry = {
  [ControlUnitResourceType.PIROGUE]: 'Pirogue',
  [ControlUnitResourceType.NO_RESOURCE]: 'N/A',
  [ControlUnitResourceType.RIGID_HULL]: 'Coque rigide',
  [ControlUnitResourceType.CAR]: 'Voiture'
}

export function useControlUnitResourceLabel(controlUnits: ControlUnit[]): string {
  if (controlUnits && controlUnits.length > 0) {
    return controlUnits.map((controlUnit) => {
      if (controlUnit.controlUnitResources && controlUnit.controlUnitResources.length > 0) {
        return controlUnit.controlUnitResources.map((resource) => {
          const resourceType = resource.name as ControlUnitResourceType
          return RESOURCE_UNIT_REGISTRY[resourceType] || 'Inconnu'
        }).join(', ')
      }
      return 'N/A'
    }).join(', ')
  }
  return 'N/A'
}
