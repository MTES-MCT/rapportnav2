import { useMissionFinished } from '../../common/hooks/use-mission-finished.tsx'
import { AbstractFormikSubFormHook } from '../../common/types/abstract-formik-hook'
import { MissionAction, MissionNavActionData } from '../../common/types/mission-action'
import { ActionControlInput } from '../types/action-type'
import { getControlSectorFishingSchema } from '../validation-schema/control-sector-fishing'
import { useMissionActionGenericControl } from './use-mission-action-generic-control'

const SECTOR_FISHING_BOOLEANS = [
  'incidentDuringOperation',
  'isControlDuringSecurityDay',
  'isWithinDepartment',
  'hasDivingDuringOperation',
  'isSeizureSleepingFishingGear',
  'signature',
  'isOutOfUnit'
]

export function useMissionActionControlSectorFishing(
  action: MissionAction,
  onChange: (newAction: MissionAction) => Promise<unknown>
): AbstractFormikSubFormHook<ActionControlInput> {
  const isMissionFinished = useMissionFinished(action.ownerId)

  const transformFieldValue = (data: MissionNavActionData): MissionNavActionData => {
    const establishment = data.sati?.establishment
    if (!data.sati || !establishment) return data

    return {
      ...data,
      sati: {
        ...data.sati,
        establishment: {
          ...establishment,
          data: data.establishment,
          type: data.sectorEstablishmentType ?? establishment.type
        }
      }
    }
  }

  return useMissionActionGenericControl(
    action,
    onChange,
    getControlSectorFishingSchema(isMissionFinished),
    false,
    SECTOR_FISHING_BOOLEANS,
    transformFieldValue
  )
}
