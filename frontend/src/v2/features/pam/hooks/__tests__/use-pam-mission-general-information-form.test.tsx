import { describe, it, expect, vi, beforeEach } from 'vitest'
import { renderHook } from '@testing-library/react'

import { usePamMissionGeneralInfoForm } from '../use-pam-mission-general-information-form.tsx'
import { useMissionFinished } from '../../../common/hooks/use-mission-finished.tsx'
import { useMissionInterServices } from '../../../common/hooks/use-mission-interservices.tsx'

// Mock dependencies
vi.mock('../../../common/hooks/use-abstract-formik-form.tsx', () => ({
  useAbstractFormik: () => ({
    initValue: {},
    handleSubmit: vi.fn(),
    errors: {}
  })
}))

vi.mock('../../common/hooks/use-date.tsx', () => ({
  useDate: () => ({
    preprocessDateForPicker: vi.fn(date => new Date(date)),
    postprocessDateFromPicker: vi.fn(date => date.toString())
  })
}))

vi.mock('../../../common/hooks/use-mission-finished.tsx', () => ({
  useMissionFinished: vi.fn()
}))

vi.mock('../../../common/hooks/use-mission-interservices.tsx', () => ({
  useMissionInterServices: vi.fn()
}))

describe('usePamMissionGeneralInformationForm', () => {
  describe('usePamMissionGeneralInfoForm - validationSchema', () => {
    const baseMission = { missionId: '1' }

    beforeEach(() => {
      vi.clearAllMocks()
    })

    const renderWithStates = (isFinished: boolean, isInter: boolean) => {
      vi.mocked(useMissionFinished).mockReturnValue(isFinished)
      vi.mocked(useMissionInterServices).mockReturnValue(isInter)

      const { result } = renderHook(() => usePamMissionGeneralInfoForm(vi.fn(), baseMission as any))
      return result.current.validationSchema
    }

    it('should not require conditional fields if mission not finished', async () => {
      const schema = renderWithStates(false, false)
      const valid = await schema.isValid({})
      expect(valid).toBe(true)
    })

    it('should require all conditional fields if mission is finished and not interservices', async () => {
      const schema = renderWithStates(true, false)
      const valid = await schema.isValid({
        // missing required fields
        dates: [new Date(), new Date()]
      })
      expect(valid).toBe(false)

      // Test one specific error
      await expect(schema.validate({})).rejects.toThrow(/crew is a required field/)
    })

    it('should not require those fields if mission is finished but interservices', async () => {
      const schema = renderWithStates(true, true)
      const valid = await schema.isValid({
        dates: [new Date(), new Date()]
      })
      expect(valid).toBe(true)
    })

    it('should validate successfully when all required fields are present', async () => {
      const schema = renderWithStates(true, false)
      const valid = await schema.isValid({
        dates: [new Date(), new Date()],
        distanceInNauticalMiles: 10,
        consumedGOInLiters: 20,
        consumedFuelInLiters: 30,
        nbrOfRecognizedVessel: 1,
        observations: null,
        crew: [{}]
      })
      expect(valid).toBe(true)
    })
  })
})
