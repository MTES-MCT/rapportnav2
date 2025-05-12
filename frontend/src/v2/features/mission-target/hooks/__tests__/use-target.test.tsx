import { ControlType } from '@common/types/control-types'
import { InfractionTypeEnum } from '@common/types/env-mission-types'
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
})
