import { ControlType } from '@common/types/control-types'
import { ActionTargetTypeEnum, InfractionTypeEnum } from '@common/types/env-mission-types'
import { renderHook } from '@testing-library/react'
import { TargetInfraction } from 'src/v2/features/mission-infraction/hooks/use-infraction-env-form'
import { MissionSourceEnum } from '../../../common/types/mission-types'
import { Control, Target, TargetType } from '../../../common/types/target-types'
import { useTarget } from '../use-target'

describe('useTarget', () => {
  it('should validate that is default', () => {
    const target = { targetType: TargetType.DEFAULT, source: MissionSourceEnum.RAPPORTNAV } as Target
    const { result } = renderHook(() => useTarget())
    expect(result.current.isDefaultTarget(target)).toBeTruthy()

    target.targetType = TargetType.VEHICLE
    expect(result.current.isDefaultTarget(target)).toBeFalsy()
  })

  it('should push infraction into the target at the rightt control without ersing previous infraction', () => {
    const infractionId1 = 'infraction1'

    const input = {
      target: {
        identityControlledPerson: 'My identify controlled person'
      } as Target,
      control: {
        amountOfControls: 4,
        controlType: ControlType.GENS_DE_MER
      } as Control,
      infraction: {
        observations: 'infraction 2',
        natinfs: ['10034', '43534624'],
        controlType: ControlType.GENS_DE_MER,
        infractionType: InfractionTypeEnum.WITH_REPORT
      }
    } as TargetInfraction
    const target = {
      actionId: 'mycontrolId',
      controls: [
        {
          amountOfControls: 1,
          controlType: ControlType.ADMINISTRATIVE
        },
        {
          amountOfControls: 1,
          controlType: ControlType.GENS_DE_MER,
          infractions: [
            {
              id: infractionId1,
              observations: 'infraction 1',
              natinfs: ['10034'],
              infractionType: InfractionTypeEnum.WITHOUT_REPORT
            }
          ]
        }
      ]
    } as Target

    const { result } = renderHook(() => useTarget())
    const response = result.current.fromInputToFieldValue(input, target)
    expect(response).toBeTruthy()
    expect(response?.controls?.length).toEqual(2)
    expect(response?.actionId).toEqual(target.actionId)
    expect(response?.identityControlledPerson).toEqual(input.target?.identityControlledPerson)

    const control = response?.controls?.find(c => c.controlType === ControlType.GENS_DE_MER)
    expect(control?.infractions?.length).toEqual(2)
    expect(control?.infractions?.map(i => i.id)).toContain(infractionId1)

    const infraction = control?.infractions?.find(i => !i.id)

    expect(infraction?.natinfs).toEqual(input.infraction?.natinfs)
    expect(infraction?.observations).toEqual(input.infraction?.observations)
    expect(infraction?.infractionType).toEqual(input.infraction?.infractionType)
  })

  it('should get target type from actionTargetType', () => {
    const { result } = renderHook(() => useTarget())
    expect(result.current.getTargetType(ActionTargetTypeEnum.INDIVIDUAL)).toBe(TargetType.INDIVIDUAL)
    expect(result.current.getTargetType(ActionTargetTypeEnum.VEHICLE)).toBe(TargetType.VEHICLE)
    expect(result.current.getTargetType(undefined)).toBe(TargetType.INDIVIDUAL)
  })

  it('should delete infraction at given index and controlType', () => {
    const { result } = renderHook(() => useTarget())
    const target: Target = {
      controls: [
        {
          controlType: ControlType.ADMINISTRATIVE,
          infractions: [
            { id: '1', observations: 'obs1' },
            { id: '2', observations: 'obs2' }
          ]
        }
      ]
    } as Target

    const updated = result.current.deleteInfraction(ControlType.ADMINISTRATIVE, 0, target)
    expect(updated?.controls?.[0].infractions?.length).toBe(1)
    expect(updated?.controls?.[0].infractions?.[0].id).toBe('2')
  })

  it('should return undefined when deleting infraction from undefined target', () => {
    const { result } = renderHook(() => useTarget())
    expect(result.current.deleteInfraction(ControlType.ADMINISTRATIVE, 0, undefined)).toBeUndefined()
  })

  it('should filter available control types based on infractions count', () => {
    const { result } = renderHook(() => useTarget())
    const target: Target = {
      controls: [
        {
          controlType: ControlType.ADMINISTRATIVE,
          infractions: [{}, {}]
        },
        {
          controlType: ControlType.GENS_DE_MER,
          infractions: [{}]
        }
      ]
    } as Target

    const available = [ControlType.ADMINISTRATIVE, ControlType.GENS_DE_MER]
    const filtered = result.current.filterAvailableControlType(target, available, 2)
    expect(filtered).toContain(ControlType.GENS_DE_MER)
    expect(filtered).not.toContain(ControlType.ADMINISTRATIVE)
  })

  it('should return correct number of infractions for targets', () => {
    const { result } = renderHook(() => useTarget())
    const targets: Target[] = [
      {
        controls: [
          { controlType: ControlType.ADMINISTRATIVE, infractions: [{}, {}] },
          { controlType: ControlType.GENS_DE_MER, infractions: [{}] }
        ]
      } as Target,
      {
        controls: [{ controlType: ControlType.NAVIGATION, infractions: [{}] }]
      } as Target
    ]
    expect(result.current.getNbrInfraction(targets)).toBe(4)
    expect(result.current.getNbrInfraction(undefined)).toBe(0)
  })
})
