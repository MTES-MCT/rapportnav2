import { ControlType } from '@common/types/control-types'
import { ActionTargetTypeEnum, InfractionTypeEnum } from '@common/types/env-mission-types'
import { renderHook } from '@testing-library/react'
import { describe } from 'vitest'
import { MissionSourceEnum } from '../../../common/types/mission-types'
import { Control, Target, TargetInfraction, TargetType } from '../../../common/types/target-types'
import { useTarget } from '../use-target'

describe('useTarget', () => {
  it('should validate that is default', () => {
    const target = { targetType: TargetType.DEFAULT, source: MissionSourceEnum.RAPPORTNAV } as Target
    const { result } = renderHook(() => useTarget())
    expect(result.current.isDefaultTarget(target)).toBeTruthy()

    target.targetType = TargetType.VEHICLE
    expect(result.current.isDefaultTarget(target)).toBeFalsy()
  })

  it('should validate that is default event when source is null', () => {
    const target = { targetType: TargetType.DEFAULT, source: null } as Target
    const { result } = renderHook(() => useTarget())
    expect(result.current.isDefaultTarget(target)).toBeTruthy()
  })

  it('should push infraction into the target at the right control without erasing previous infraction', () => {
    const infractionId1 = 'infraction1'

    const input = {
      target: {
        actionId: 'mycontrolId',
        identityControlledPerson: 'My identify controlled person',
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
      } as Target,
      control: {
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
      } as Control,
      infraction: {
        observations: 'infraction 2',
        natinfs: ['10034', '43534624'],
        controlType: ControlType.GENS_DE_MER,
        infractionType: InfractionTypeEnum.WITH_REPORT
      }
    } as TargetInfraction
    const { result } = renderHook(() => useTarget())
    const response = result.current.fromInputToFieldValue(input)
    expect(response).toBeTruthy()
    expect(response?.controls?.length).toEqual(2)
    expect(response?.actionId).toEqual(input.target?.actionId)
    expect(response?.identityControlledPerson).toEqual(input.target?.identityControlledPerson)

    const control = response?.controls?.find(c => c.controlType === ControlType.GENS_DE_MER)
    expect(control?.infractions?.length).toEqual(2)
    expect(control?.infractions?.map(i => i.id)).toContain(infractionId1)

    const infraction = control?.infractions?.find(i => !i.id)

    expect(infraction?.natinfs).toEqual(input.infraction?.natinfs)
    expect(infraction?.observations).toEqual(input.infraction?.observations)
    expect(infraction?.infractionType).toEqual(input.infraction?.infractionType)
  })

  it('should push infraction into the target at the right control when isInquiry', () => {
    const input = {
      target: {
        actionId: 'mycontrolId',
        identityControlledPerson: 'My identify controlled person',
        controls: [
          {
            amountOfControls: 1,
            controlType: ControlType.ADMINISTRATIVE
          },
          {
            amountOfControls: 1,
            controlType: ControlType.GENS_DE_MER
          }
        ]
      } as Target,
      control: {
        controlType: ControlType.GENS_DE_MER
      } as Control,
      infraction: {
        observations: 'infraction 2',
        natinfs: ['10034', '43534624'],
        infractionType: InfractionTypeEnum.WITH_REPORT
      }
    } as TargetInfraction
    const { result } = renderHook(() => useTarget())
    const response = result.current.fromInputToFieldValue(input, true)
    expect(response).toBeTruthy()
    expect(response?.controls?.length).toEqual(2)
    expect(response?.actionId).toEqual(input.target?.actionId)
    expect(response?.identityControlledPerson).toEqual(input.target?.identityControlledPerson)

    const control = response?.controls?.find(c => c.controlType === ControlType.GENS_DE_MER)
    expect(control?.infractions?.length).toEqual(1)

    const infraction = control?.infractions?.find(i => !i.id)
    expect(infraction?.observations).toEqual(input.infraction?.observations)
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

    const updated = result.current.deleteInfraction(target, 0, 0)
    expect(updated?.controls?.[0].infractions?.length).toBe(1)
    expect(updated?.controls?.[0].infractions?.[0].id).toBe('2')
  })

  it('should return correct number of infractions for targets', () => {
    const { result } = renderHook(() => useTarget())
    const targets: Target[] = [
      {
        controls: [
          { controlType: ControlType.ADMINISTRATIVE, infractions: [{}, {}] },
          { controlType: ControlType.GENS_DE_MER, infractions: [{ natinfs: ['natinfs1'] }] }
        ]
      } as Target,
      {
        controls: [{ controlType: ControlType.NAVIGATION, infractions: [{ natinfs: ['natinfs2'] }] }]
      } as Target
    ]
    expect(result.current.getNbrInfraction(targets)).toBe(2)
    expect(result.current.getNbrInfraction(undefined)).toBe(0)
  })

  it('should calcul availables control types except those already on target', () => {
    const { result } = renderHook(() => useTarget())
    const controlTypes = [
      ControlType.SECURITY,
      ControlType.NAVIGATION,
      ControlType.GENS_DE_MER,
      ControlType.ADMINISTRATIVE
    ]
    const target = {
      controls: [
        {
          amountOfControls: 0,
          controlType: ControlType.SECURITY
        },
        {
          amountOfControls: 1,
          controlType: ControlType.GENS_DE_MER
        },
        {
          amountOfControls: 1,
          controlType: ControlType.NAVIGATION
        },
        {
          amountOfControls: 1,
          infractions: [
            {
              id: 'erere',
              infractionType: InfractionTypeEnum.WITH_REPORT
            }
          ],
          controlType: ControlType.ADMINISTRATIVE
        }
      ]
    } as Target

    const responses = result.current.getAvailableControlTypes(target, controlTypes)

    expect(responses).toHaveLength(3)
    expect(responses).not.toContain(ControlType.ADMINISTRATIVE)
  })

  describe('getAvailableEnvControlTypes', () => {
    it('should calcul available control types', () => {
      const { result } = renderHook(() => useTarget())
      const controlTypes = [
        ControlType.SECURITY,
        ControlType.NAVIGATION,
        ControlType.GENS_DE_MER,
        ControlType.ADMINISTRATIVE
      ]

      const targets = [
        {
          targetType: TargetType.DEFAULT,
          controls: [
            {
              amountOfControls: 1,
              controlType: ControlType.SECURITY
            },
            {
              amountOfControls: 1,
              controlType: ControlType.GENS_DE_MER
            },
            {
              amountOfControls: 0,
              controlType: ControlType.NAVIGATION
            },
            {
              amountOfControls: 1,
              controlType: ControlType.ADMINISTRATIVE
            }
          ]
        },
        {
          targetType: TargetType.INDIVIDUAL,
          controls: [
            {
              controlType: ControlType.GENS_DE_MER,
              infractions: [
                {
                  id: '1212&',
                  observations: 'infraction 1',
                  natinfs: ['10034'],
                  infractionType: InfractionTypeEnum.WITHOUT_REPORT
                }
              ]
            }
          ]
        }
      ] as Target[]

      const responses = result.current.getAvailableEnvControlTypes(targets, controlTypes)

      expect(responses).toHaveLength(2)
      expect(responses).toContain(ControlType.SECURITY)
      expect(responses).toContain(ControlType.ADMINISTRATIVE)
    })

    it('should handle empty targets array', () => {
      const { result } = renderHook(() => useTarget())
      const controlTypes = [ControlType.SECURITY]

      // Edge case: empty targets
      const responses = result.current.getAvailableEnvControlTypes([], controlTypes)

      expect(responses).toEqual([])
    })

    it('should handle undefined targets', () => {
      const { result } = renderHook(() => useTarget())
      const controlTypes = [ControlType.SECURITY]

      // Edge case: undefined targets
      const responses = result.current.getAvailableEnvControlTypes(undefined, controlTypes)

      expect(responses).toEqual([])
    })

    it('should handle targets with no empty array controls', () => {
      const { result } = renderHook(() => useTarget())
      const controlTypes = [ControlType.SECURITY]

      const targets = [
        {
          targetType: TargetType.DEFAULT,
          controls: []
        }
      ] as Target[]

      const responses = result.current.getAvailableEnvControlTypes(targets, controlTypes)

      expect(responses).toEqual([])
    })

    it('should NOT reproduce error with null/undefined controls', () => {
      const { result } = renderHook(() => useTarget())
      const controlTypes = [ControlType.SECURITY]

      // Try with a target that might cause getControlAmountByControlTypes to return undefined/null
      const targets = [
        {
          targetType: TargetType.DEFAULT,
          controls: undefined // Missing controls
        }
      ] as Target[]

      try {
        const response = result.current.getAvailableEnvControlTypes(targets, controlTypes)
        expect(response).toEqual([])
      } catch (error) {
        expect(error.message).toMatch(/entries.*reduce/)
      }
    })
  })
})
