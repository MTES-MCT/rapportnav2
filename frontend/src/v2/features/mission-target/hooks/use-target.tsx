import { ControlType } from '@common/types/control-types'
import { ActionTargetTypeEnum } from '@common/types/env-mission-types'
import { MissionSourceEnum } from '../../common/types/mission-types'
import { Control, Target, TargetType } from '../../common/types/target-types'
import { TargetInfraction } from '../../mission-infraction/hooks/use-infraction-env-form2'

type MapControl = {
  [key in ControlType]?: Control
}

export function useTarget() {
  const isDefaultTarget = (target?: Target) =>
    target?.targetType === TargetType.DEFAULT && target?.source === MissionSourceEnum.RAPPORTNAV

  const getTargetType = (actionTargetType?: ActionTargetTypeEnum) =>
    actionTargetType ? TargetType[actionTargetType as keyof typeof TargetType] : TargetType.INDIVIDUAL

  const fromInputToFieldValue = (input: TargetInfraction, target?: Target): Target | undefined => {
    const infraction = input.infraction
    const controlType = input.control?.controlType

    if (!controlType || !infraction) return target
    let newTarget = { ...(target ?? ({} as Target)), ...(input.target ?? ({} as Target)) }

    const controls = toMapControl(newTarget?.controls)
    controls[controlType] = { ...(controls[controlType] ?? {}), controlType, infractions: [infraction] }
    return { ...newTarget, controls: Object.values(controls) }
  }

  const toMapControl = (controls?: Control[]) => {
    return (
      controls?.reduce(function (map, obj) {
        map[obj.controlType] = obj
        return map
      }, {} as MapControl) ?? ({} as MapControl)
    )
  }

  const deleteInfraction = (controlIndex: number, infractionIndex: number, target?: Target): Target | undefined => {
    target?.controls?.at(controlIndex)?.infractions?.splice(infractionIndex, 1)
    return target
  }

  const filterAvailableControlType = (
    target: Target,
    availableControlTypes: ControlType[],
    actionNumberOfControls: number
  ) => {
    const fullControlTypes = target.controls
      ?.filter(control => (control.infractions?.length ?? 0) >= actionNumberOfControls)
      .map(control => control.controlType)

    return availableControlTypes.filter(a => !fullControlTypes?.includes(a))
  }

  return {
    getTargetType,
    isDefaultTarget,
    deleteInfraction,
    fromInputToFieldValue,
    filterAvailableControlType
  }
}
