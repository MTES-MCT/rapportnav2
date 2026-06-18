import { ControlCheck } from '@common/types/fish-mission-types'
import { renderHook } from '@testing-library/react'
import { usecontrolCheck } from '../use-control-check'

describe('usecontrolCheck', () => {
  it('should return control check options', () => {
    const { result } = renderHook(() => usecontrolCheck())
    expect(result.current.controlCheckRadioOptions.length).toEqual(3)
  })

  it('should return control check options without', () => {
    const { result } = renderHook(() => usecontrolCheck())
    expect(result.current.controlCheckRadioBooleanOptions.length).toEqual(2)
    expect(result.current.controlCheckRadioBooleanOptions.map(t => t.label).includes('Non contrôlé')).toBeFalsy()
  })

  it('should return control check options without', () => {
    const { result } = renderHook(() => usecontrolCheck())
    expect(result.current.getControlCheck(ControlCheck.NO)).toEqual('Non')
    expect(result.current.getControlCheck(ControlCheck.YES)).toEqual('Oui')
    expect(result.current.getControlCheck(ControlCheck.NOT_APPLICABLE)).toEqual('Non contrôlé')
  })
})
