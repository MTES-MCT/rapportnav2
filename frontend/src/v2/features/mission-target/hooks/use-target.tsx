import { ControlType } from '@common/types/control-types'
import { ActionTargetTypeEnum } from '@common/types/env-mission-types'
import { isEmpty } from 'lodash'
import { MissionSourceEnum } from '../../common/types/mission-types'
import { Control, Infraction, Target, TargetInfraction, TargetType } from '../../common/types/target-types'

type AmountControlType = Map<ControlType, number>

const CONTROL_TYPES = [
  ControlType.ADMINISTRATIVE,
  ControlType.GENS_DE_MER,
  ControlType.NAVIGATION,
  ControlType.SECURITY
]

const MISSION_SOURCE_TYPES_EXTERNAL = [
  MissionSourceEnum.MONITORENV,
  MissionSourceEnum.MONITORFISH,
  MissionSourceEnum.POSEIDON_CACEM,
  MissionSourceEnum.POSEIDON_CNSP
]

const upsert = <T,>(
  arr: T[],
  item: T,
  predicate: (el: T) => boolean,
  merge: (existing: T, incoming: T) => T = (_, incoming) => incoming
): T[] => {
  const index = arr.findIndex(predicate)
  return index === -1 ? [...arr, item] : arr.map((el, i) => (i === index ? merge(el, item) : el))
}

export function useTarget() {
  const isDefaultTarget = (target?: Target) =>
    target?.targetType === TargetType.DEFAULT && !MISSION_SOURCE_TYPES_EXTERNAL.includes(target?.source)

  const getTargetType = (actionTargetType?: ActionTargetTypeEnum) =>
    actionTargetType ? TargetType[actionTargetType as keyof typeof TargetType] : TargetType.INDIVIDUAL

  const fromInputToFieldValue = (input: TargetInfraction): Target | undefined => {
    const target = { ...(input.target ?? ({} as Target)) }
    const control = { ...(input.control ?? ({} as Control)) }
    const infraction = { ...(input.infraction ?? ({} as Infraction)) }

    const updatedControl = isEmpty(infraction)
      ? control
      : { ...control, infractions: upsert(control.infractions ?? [], infraction, i => i.id === infraction.id) }

    if (isEmpty(updatedControl)) return target

    return {
      ...target,
      controls: upsert(
        target.controls ?? [],
        updatedControl,
        c => c.controlType === updatedControl.controlType,
        (existing, incoming) => ({ ...existing, infractions: incoming.infractions })
      )
    }
  }

  const deleteInfraction = (target: Target, controlIndex: number, infractionIndex: number): Target | undefined => {
    if (!target || !target.controls) return

    const controls = [...target.controls]
    if (!controls[controlIndex]) return

    const infractions = target.controls[controlIndex].infractions?.filter((_, index) => index !== infractionIndex)
    controls[controlIndex] = { ...controls[controlIndex], infractions }

    return { ...target, controls }
  }

  const getControlAmountByControlTypes = (
    target: Target,
    amount: Map<ControlType, number>
  ): AmountControlType | undefined => {
    return target.controls?.reduce((acc, control) => {
      if (target.targetType === TargetType.DEFAULT) {
        acc.set(control.controlType, (acc.get(control.controlType) ?? 0) + (control.amountOfControls ?? 0))
      } else {
        acc.set(control.controlType, (acc.get(control.controlType) ?? 0) - (control.infractions?.length ?? 0))
      }
      return acc
    }, amount)
  }

  const getAvailableEnvControlTypes = (targets?: Target[], controlTypes?: ControlType[]): ControlType[] => {
    if (!targets) return []

    const controlMap = targets.reduce((acc, target) => {
      return getControlAmountByControlTypes(target ?? [], acc) ?? acc
    }, new Map())

    return Array.from(controlMap.entries())
      .filter(([key, value]) => value > 0 && controlTypes?.includes(key))
      .map(([key]) => key)
  }

  const getAvailableControlTypes = (target?: Target, controlTypes?: ControlType[]): ControlType[] => {
    const excludeControlTypes = target?.controls
      ?.filter(control => control?.infractions?.length && !!control.controlType)
      ?.map(control => control?.controlType)
    return controlTypes?.filter(c => !excludeControlTypes?.includes(c)) ?? []
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
    getNbrInfraction,
    fromInputToFieldValue,
    getAvailableControlTypes,
    getAvailableEnvControlTypes,
    defaultControlTypes: CONTROL_TYPES,
    allControlTypes: Object.keys(ControlType)?.map(key => ControlType[key as keyof typeof ControlType])
  }
}
