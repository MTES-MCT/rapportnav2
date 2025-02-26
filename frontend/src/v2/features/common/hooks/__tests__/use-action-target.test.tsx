import { ActionTargetTypeEnum } from '@common/types/env-mission-types'
import { renderHook } from '@testing-library/react'
import { useActionTarget } from '../use-action-target'

describe('useActionTarget', () => {
  it('should return vehicule type options', () => {
    const { result } = renderHook(() => useActionTarget())
    expect(result.current.vehiculeTypeOptions.length).toEqual(3)
  })

  it('should return control check options without', () => {
    const { result } = renderHook(() => useActionTarget())
    expect(result.current.getActionTargetType(ActionTargetTypeEnum.VEHICLE)).toEqual('Véhicule')
    expect(result.current.getActionTargetType(ActionTargetTypeEnum.COMPANY)).toEqual('Personne morale')
    expect(result.current.getActionTargetType(ActionTargetTypeEnum.INDIVIDUAL)).toEqual('Personne physique')
  })
})
