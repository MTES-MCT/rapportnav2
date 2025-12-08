import { describe, it, expect } from 'vitest'
import { renderHook } from '@testing-library/react'
import { useMissionCrewAbsenceReason } from '../use-crew-absence-reason.tsx'
import { MissionCrewAbsenceReason } from '../../../common/types/crew-type.ts'

describe('useMissionCrewAbsenceReason', () => {
  it('should return absence reason options', () => {
    const { result } = renderHook(() => useMissionCrewAbsenceReason())
    expect(result.current.ABSENCE_REASON_OPTIONS).toHaveLength(8)
  })

  it('should return the label for a given reason', () => {
    const { result } = renderHook(() => useMissionCrewAbsenceReason())
    expect(result.current.getAbsenceReasonLabel(MissionCrewAbsenceReason.SICK_LEAVE)).toBe('Arrêt maladie')
  })
})
