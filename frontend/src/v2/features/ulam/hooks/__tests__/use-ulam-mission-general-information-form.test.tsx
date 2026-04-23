import { describe, it, expect, vi } from 'vitest'
import { MissionTypeEnum } from '@common/types/env-mission-types.ts'
import { useUlamMissionGeneralInfoForm } from '../use-ulam-mission-general-information-form.tsx'
import { MissionReportTypeEnum } from '../../../common/types/mission-types.ts'
import { renderHook } from '../../../../../test-utils.tsx'

vi.mock('../../../common/hooks/use-abstract-formik-form.tsx', () => ({
  useAbstractFormik: () => ({
    initValue: {},
    handleSubmit: vi.fn()
  })
}))

vi.mock('../../../common/hooks/use-date.tsx', () => ({
  useDate: () => ({
    preprocessDateForPicker: vi.fn(date => new Date(date)),
    postprocessDateFromPicker: vi.fn(date => date?.toString())
  })
}))

vi.mock('../../../common/hooks/use-mission-finished.tsx', () => ({
  useMissionFinished: () => true
}))

describe('useUlamMissionGeneralInfoForm', () => {
  describe('validationSchema', () => {
    const getValidationSchema = () => {
      const { result } = renderHook(() => useUlamMissionGeneralInfoForm(vi.fn(), {} as any))
      return result.current.validationSchema
    }

    const validDates = [new Date('2024-01-01'), new Date('2024-01-02')]

    describe('missionReportType', () => {
      it('should require missionReportType', async () => {
        const schema = getValidationSchema()
        await expect(schema.validateAt('missionReportType', {})).rejects.toThrow('Type de rapport obligatoire')
      })

      it('should accept valid missionReportType', async () => {
        const schema = getValidationSchema()
        const valid = await schema.validateAt('missionReportType', {
          missionReportType: MissionReportTypeEnum.FIELD_REPORT
        })
        expect(valid).toBe(MissionReportTypeEnum.FIELD_REPORT)
      })
    })

    describe('dates', () => {
      it('should require dates', async () => {
        const schema = getValidationSchema()
        await expect(schema.validateAt('dates', {})).rejects.toThrow('Date et heure de début et de fin obligatoire')
      })

      it('should reject dates where end is before start', async () => {
        const schema = getValidationSchema()
        const invalidDates = [new Date('2024-01-02'), new Date('2024-01-01')]
        const valid = await schema.isValid({ dates: invalidDates })
        expect(valid).toBe(false)
      })

      it('should accept valid dates where end is after start', async () => {
        const schema = getValidationSchema()
        const result = await schema.validateAt('dates', { dates: validDates })
        expect(result).toEqual(validDates)
      })
    })

    describe('reinforcementType', () => {
      it('should not require reinforcementType when missionReportType is FIELD_REPORT', async () => {
        const schema = getValidationSchema()
        const valid = await schema.isValid({
          missionReportType: MissionReportTypeEnum.FIELD_REPORT,
          dates: validDates,
          missionTypes: [MissionTypeEnum.LAND]
        })
        expect(valid).toBe(true)
      })

      it('should require reinforcementType when missionReportType is EXTERNAL_REINFORCEMENT_TIME_REPORT', async () => {
        const schema = getValidationSchema()
        await expect(
          schema.validate({
            missionReportType: MissionReportTypeEnum.EXTERNAL_REINFORCEMENT_TIME_REPORT,
            dates: validDates
          })
        ).rejects.toThrow('Nature du renfort obligatoire')
      })
    })

    describe('missionTypes', () => {
      it('should not require missionTypes when missionReportType is OFFICE_REPORT', async () => {
        const schema = getValidationSchema()
        const valid = await schema.isValid({
          missionReportType: MissionReportTypeEnum.OFFICE_REPORT,
          dates: validDates
        })
        expect(valid).toBe(true)
      })

      it('should require missionTypes when missionReportType is FIELD_REPORT', async () => {
        const schema = getValidationSchema()
        await expect(
          schema.validate({
            missionReportType: MissionReportTypeEnum.FIELD_REPORT,
            dates: validDates
          })
        ).rejects.toThrow('Type de mission obligatoire')
      })
    })

    describe('nbHourAtSea', () => {
      it('should not require nbHourAtSea when missionTypes does not include SEA', async () => {
        const schema = getValidationSchema()
        const valid = await schema.isValid({
          missionReportType: MissionReportTypeEnum.FIELD_REPORT,
          dates: validDates,
          missionTypes: [MissionTypeEnum.LAND]
        })
        expect(valid).toBe(true)
      })

      it('should not require nbHourAtSea when missionTypes is undefined', async () => {
        const schema = getValidationSchema()
        const valid = await schema.isValid({
          missionReportType: MissionReportTypeEnum.OFFICE_REPORT,
          dates: validDates
        })
        expect(valid).toBe(true)
      })

      it('should require nbHourAtSea when missionTypes includes SEA', async () => {
        const schema = getValidationSchema()
        await expect(
          schema.validate({
            missionReportType: MissionReportTypeEnum.FIELD_REPORT,
            dates: validDates,
            missionTypes: [MissionTypeEnum.SEA]
          })
        ).rejects.toThrow("Nombre d'heures en mer obligatoire")
      })

      it('should require nbHourAtSea when missionTypes includes SEA among other types', async () => {
        const schema = getValidationSchema()
        await expect(
          schema.validate({
            missionReportType: MissionReportTypeEnum.FIELD_REPORT,
            dates: validDates,
            missionTypes: [MissionTypeEnum.LAND, MissionTypeEnum.SEA]
          })
        ).rejects.toThrow("Nombre d'heures en mer obligatoire")
      })

      it('should reject negative nbHourAtSea', async () => {
        const schema = getValidationSchema()
        await expect(
          schema.validate({
            missionReportType: MissionReportTypeEnum.FIELD_REPORT,
            dates: validDates,
            missionTypes: [MissionTypeEnum.SEA],
            nbHourAtSea: -1
          })
        ).rejects.toThrow("Le nombre d'heures en mer ne peut pas être négatif")
      })

      it('should reject negative nbHourAtSea even when missionTypes does not include SEA', async () => {
        const schema = getValidationSchema()
        await expect(
          schema.validate({
            missionReportType: MissionReportTypeEnum.FIELD_REPORT,
            dates: validDates,
            missionTypes: [MissionTypeEnum.LAND],
            nbHourAtSea: -5
          })
        ).rejects.toThrow("Le nombre d'heures en mer ne peut pas être négatif")
      })

      it('should accept zero nbHourAtSea when missionTypes includes SEA', async () => {
        const schema = getValidationSchema()
        const valid = await schema.isValid({
          missionReportType: MissionReportTypeEnum.FIELD_REPORT,
          dates: validDates,
          missionTypes: [MissionTypeEnum.SEA],
          nbHourAtSea: 0
        })
        expect(valid).toBe(true)
      })

      it('should accept positive nbHourAtSea when missionTypes includes SEA', async () => {
        const schema = getValidationSchema()
        const valid = await schema.isValid({
          missionReportType: MissionReportTypeEnum.FIELD_REPORT,
          dates: validDates,
          missionTypes: [MissionTypeEnum.SEA],
          nbHourAtSea: 10
        })
        expect(valid).toBe(true)
      })
    })
  })
})
