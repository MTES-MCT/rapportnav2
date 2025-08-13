import { ControlType } from '@common/types/control-types'
import { ActionTargetTypeEnum } from '@common/types/env-mission-types'
import { MissionSourceEnum } from '../../common/types/mission-types'
import { Control, Infraction, Target, TargetType } from '../../common/types/target-types'
import { TargetInfraction } from '../../mission-infraction/hooks/use-infraction-env-form'

type MapControl = {
  [key in ControlType]?: Control
}

export function useTarget() {
  const isDefaultTarget = (target?: Target) =>
    target?.targetType === TargetType.DEFAULT && target?.source === MissionSourceEnum.RAPPORTNAV

  const getTargetType = (actionTargetType?: ActionTargetTypeEnum) =>
    actionTargetType ? TargetType[actionTargetType as keyof typeof TargetType] : TargetType.INDIVIDUAL

  const controlToMap = (controls?: Control[]) => {
    return (
      controls?.reduce(function (map, obj) {
        map[obj.controlType] = obj
        return map
      }, {} as MapControl) ?? ({} as MapControl)
    )
  }

  const getNewTarget = (input: TargetInfraction, target?: Target) => {
    return { ...(target ?? ({} as Target)), ...(input.target ?? ({} as Target)) }
  }

  const getNewInfractions = (infractionInput: Infraction, infractions: Infraction[]) => {
    if (infractionInput.id) {
      const infractionIndex = infractions.findIndex(infraction => infraction.id === infractionInput.id)
      infractions[infractionIndex] = infractionInput
    } else {
      infractions = [...infractions, infractionInput]
    }
    return infractions
  }

  const getNewControls = (input: TargetInfraction, controls?: Control[]): Control[] | undefined => {
    const infractionInput = input.infraction
    const controlType = input.control?.controlType
    if (!controlType || !infractionInput) return controls

    const controlsMap = controlToMap(controls)
    const infractions = getNewInfractions(infractionInput, controlsMap[controlType]?.infractions ?? [])

    controlsMap[controlType] = { ...(controlsMap[controlType] ?? {}), controlType, infractions }
    return Object.values(controlsMap)
  }

  const fromInputToFieldValue = (input: TargetInfraction, target?: Target): Target | undefined => {
    const newTarget = getNewTarget(input, target)
    const newControls = getNewControls(input, newTarget?.controls)
    return { ...newTarget, controls: newControls }
  }

  const deleteInfraction = (controlType: ControlType, infractionIndex: number, target?: Target): Target | undefined => {
    if (!target) return
    const controls = controlToMap(target?.controls)
    let infractions = controls[controlType]?.infractions ?? []

    // Filter out the item at the specified index
    infractions = infractions.filter((_, index) => index !== infractionIndex)

    controls[controlType] = {
      ...(controls[controlType] ?? {}),
      controlType,
      infractions
    }

    return {
      ...target,
      controls: Object.values(controls)
    }
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

  const getNbrInfraction = (targets?: Target[]): number => {
    return (
      targets
        ?.flatMap(t => t.controls)
        ?.flatMap(c => c?.infractions)
        ?.reduce((acc, infraction) => acc + (infraction?.natinfs?.length ?? 0), 0) ?? 0
    )
  }
  return {
    getTargetType,
    isDefaultTarget,
    deleteInfraction,
    fromInputToFieldValue,
    filterAvailableControlType,
    getNbrInfraction
  }
}
