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

export function useTarget() {
  const isDefaultTarget = (target?: Target) =>
    target?.targetType === TargetType.DEFAULT && target?.source === MissionSourceEnum.RAPPORTNAV

  const getTargetType = (actionTargetType?: ActionTargetTypeEnum) =>
    actionTargetType ? TargetType[actionTargetType as keyof typeof TargetType] : TargetType.INDIVIDUAL

  const fromInputToFieldValue = (input: TargetInfraction, isInquiry?: boolean): Target | undefined => {
    const target = { ...(input.target ?? ({} as Target)) }
    const control = { ...(input.control ?? ({} as Control)) }
    const infraction = { ...(input.infraction ?? ({} as Infraction)) }

    if (!isEmpty(infraction)) {
      const infractions = [...(control.infractions ?? [])]
      const infractionIndex = infractions.findIndex(i => i.id === infraction.id)
      if (infractionIndex === -1) infractions.push(infraction)
      if (infractionIndex !== -1) infractions[infractionIndex] = infraction
      control.infractions = infractions
    }

    if (!isEmpty(control)) {
      const controls = [...(target.controls ?? [])]
      const controlIndex = controls?.findIndex(c =>
        isInquiry ? c.controlType === control.controlType : c.id === control.id
      )
      if (controlIndex === -1) controls.push(control)
      if (controlIndex !== -1) controls[controlIndex] = control
      target.controls = controls
    }

    return { ...target }
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
    return (
      targets
        ?.reduce((acc, target) => {
          return getControlAmountByControlTypes(target ?? [], acc) ?? acc
        }, new Map())
        .entries()
        .reduce((acc, [key, value]) => {
          if (value > 0 && controlTypes?.includes(key)) acc.push(key)
          return acc
        }, [] as ControlType[]) ?? []
    )
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
    controlTypes: CONTROL_TYPES
  }
}
