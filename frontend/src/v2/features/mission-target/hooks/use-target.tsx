import { ControlType } from '@common/types/control-types'
import { ActionTargetTypeEnum } from '@common/types/env-mission-types'
import { isEmpty, uniq } from 'lodash'
import { MissionSourceEnum } from '../../common/types/mission-types'
import { Control, Infraction, Target, TargetInfraction, TargetType } from '../../common/types/target-types'

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

  const fromInputToFieldValue = (input: TargetInfraction): Target | undefined => {
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
      const controlIndex = controls?.findIndex(c => c.id === control.id)
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

  const getControlTypeWithAmount = (targets?: Target[]): ControlType[] => {
    return (
      uniq(
        targets
          ?.flatMap(target => target.controls)
          ?.filter(control => control?.amountOfControls)
          ?.filter(controlType => !!controlType)
          ?.map(control => control?.controlType)
      ) ?? []
    )
  }

  const getControlTypeOnTarget = (targets?: Target[]): ControlType[] => {
    return (
      uniq(
        targets
          ?.flatMap(t => t.controls)
          ?.filter(control => control?.infractions?.length)
          ?.filter(controlType => !!controlType)
          ?.map(control => control?.controlType)
      ) ?? []
    )
  }

  const getNbrInfraction = (targets?: Target[]): number => {
    return (
      targets
        ?.flatMap(t => t.controls)
        ?.flatMap(c => c?.infractions)
        ?.reduce((acc, infraction) => acc + (infraction?.natinfs?.length ?? 0), 0) ?? 0
    )
  }

  const computeControlTypes = (controlTypes?: ControlType[], targets?: Target[]): ControlType[] => {
    const includesControlTypes = computeControlTypeWithAmount(controlTypes, targets)
    return computeControlTypeOnTarget(includesControlTypes, targets)
  }

  const computeControlTypeWithAmount = (controlTypes?: ControlType[], target?: Target[]): ControlType[] => {
    const includeControlTypes = getControlTypeWithAmount(target)
    return controlTypes?.filter(c => includeControlTypes?.includes(c)) ?? []
  }

  const computeControlTypeOnTarget = (controlTypes?: ControlType[], targets?: Target[]): ControlType[] => {
    const excludeControlTypes = getControlTypeOnTarget(targets)
    return controlTypes?.filter(c => !excludeControlTypes?.includes(c)) ?? []
  }

  return {
    getTargetType,
    isDefaultTarget,
    deleteInfraction,
    getNbrInfraction,
    fromInputToFieldValue,
    computeControlTypes,
    computeControlTypeOnTarget,
    computeControlTypeWithAmount,
    controlTypes: CONTROL_TYPES
  }
}
