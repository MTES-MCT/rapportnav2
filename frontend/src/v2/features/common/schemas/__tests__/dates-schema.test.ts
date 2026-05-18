import { describe, expect, it } from 'vitest'
import { object } from 'yup'
import getDateRangeSchema, { DateRangeDefaultSchema, getSingleDateSchema } from '../dates-schema'

describe('DateRangeDefaultSchema', () => {
  it('requires exactly 2 dates', async () => {
    const schema = object().shape(DateRangeDefaultSchema)
    const invalidData = { dates: [new Date('2024-01-15')] }
    await expect(schema.validate(invalidData)).rejects.toThrow('Les dates de début et de fin doivent être renseignées')
  })

  it('requires dates to be defined', async () => {
    const schema = object().shape(DateRangeDefaultSchema)
    const invalidData = { dates: undefined }
    await expect(schema.validate(invalidData)).rejects.toThrow('Les dates sont requises')
  })

  it('accepts valid dates array with 2 elements', async () => {
    const schema = object().shape(DateRangeDefaultSchema)
    const validData = { dates: [new Date('2024-01-15'), new Date('2024-01-16')] }
    await expect(schema.validate(validData)).resolves.toBeDefined()
  })
})

describe('getDateRangeSchema', () => {
  describe('backward compatibility', () => {
    it('accepts boolean true parameter for isMissionFinished', async () => {
      const schema = object().shape(getDateRangeSchema(true))
      const validData = { dates: [new Date('2024-01-15'), new Date('2024-01-16')] }
      await expect(schema.validate(validData)).resolves.toBeDefined()
    })

    it('accepts boolean false parameter for isMissionFinished', async () => {
      const schema = object().shape(getDateRangeSchema(false))
      const validData = { dates: null }
      await expect(schema.validate(validData)).resolves.toBeDefined()
    })

    it('accepts undefined parameter', async () => {
      const schema = object().shape(getDateRangeSchema())
      const validData = { dates: null }
      await expect(schema.validate(validData)).resolves.toBeDefined()
    })
  })

  describe('when mission is finished', () => {
    it('requires exactly 2 dates', async () => {
      const schema = object().shape(getDateRangeSchema({ isMissionFinished: true }))
      const invalidData = { dates: [new Date('2024-01-15')] }
      await expect(schema.validate(invalidData)).rejects.toThrow('Les dates de début et de fin doivent être renseignées')
    })

    it('requires end date to be defined', async () => {
      const schema = object().shape(getDateRangeSchema({ isMissionFinished: true }))
      const invalidData = { dates: [new Date('2024-01-15'), null] }
      await expect(schema.validate(invalidData)).rejects.toThrow("La date et l'heure de fin doit être renseignée")
    })

    it('requires end date to be after start date', async () => {
      const schema = object().shape(getDateRangeSchema({ isMissionFinished: true }))
      const invalidData = { dates: [new Date('2024-01-16'), new Date('2024-01-15')] }
      await expect(schema.validate(invalidData)).rejects.toThrow("La date et l'heure de fin doit être antérieure à la date de début")
    })

    it('accepts valid dates where end is after start', async () => {
      const schema = object().shape(getDateRangeSchema({ isMissionFinished: true }))
      const validData = { dates: [new Date('2024-01-15'), new Date('2024-01-16')] }
      await expect(schema.validate(validData)).resolves.toBeDefined()
    })

    it('skips is-after-start validation when start date is null', async () => {
      const schema = object().shape(getDateRangeSchema({ isMissionFinished: true }))
      const data = { dates: [null, new Date('2024-01-16')] }
      // Should not throw is-after-start error, but may fail other validations
      const result = schema.validate(data).catch(e => e.message)
      await expect(result).resolves.not.toContain("La date et l'heure de fin doit être antérieure à la date de début")
    })
  })

  describe('when mission is not finished', () => {
    it('allows null dates', async () => {
      const schema = object().shape(getDateRangeSchema({ isMissionFinished: false }))
      const validData = { dates: null }
      await expect(schema.validate(validData)).resolves.toBeDefined()
    })

    it('allows empty array', async () => {
      const schema = object().shape(getDateRangeSchema({ isMissionFinished: false }))
      const validData = { dates: [] }
      await expect(schema.validate(validData)).resolves.toBeDefined()
    })

    it('accepts valid Date objects', async () => {
      const schema = object().shape(getDateRangeSchema({ isMissionFinished: false }))
      const validData = { dates: [new Date('2024-01-15'), new Date('2024-01-16')] }
      await expect(schema.validate(validData)).resolves.toBeDefined()
    })
  })

  describe('string dates handling', () => {
    const missionStartDate = '2024-01-01T00:00:00Z'
    const missionEndDate = '2024-01-31T23:59:59Z'

    it('validates string dates within mission boundaries', async () => {
      const schema = object().shape(
        getDateRangeSchema({
          isMissionFinished: false,
          missionStartDate,
          missionEndDate
        })
      )

      const validData = { dates: ['2024-01-15T10:00:00Z', '2024-01-15T12:00:00Z'] }
      await expect(schema.validate(validData)).resolves.toBeDefined()
    })

    it('rejects string dates outside mission boundaries', async () => {
      const schema = object().shape(
        getDateRangeSchema({
          isMissionFinished: false,
          missionStartDate,
          missionEndDate
        })
      )

      const invalidData = { dates: ['2023-12-15T10:00:00Z', '2024-01-15T12:00:00Z'] }
      await expect(schema.validate(invalidData)).rejects.toThrow()
    })
  })

  describe('mission date boundary validation', () => {
    const missionStartDate = '2024-01-01T00:00:00Z'
    const missionEndDate = '2024-01-31T23:59:59Z'

    it('returns valid when action dates are within mission dates', async () => {
      const schema = object().shape(
        getDateRangeSchema({
          isMissionFinished: true,
          missionStartDate,
          missionEndDate
        })
      )

      const validData = {
        dates: [new Date('2024-01-15T10:00:00Z'), new Date('2024-01-15T12:00:00Z')]
      }

      await expect(schema.validate(validData)).resolves.toBeDefined()
    })

    it('returns invalid when action start is before mission start', async () => {
      const schema = object().shape(
        getDateRangeSchema({
          isMissionFinished: true,
          missionStartDate,
          missionEndDate
        })
      )

      const invalidData = {
        dates: [new Date('2023-12-31T10:00:00Z'), new Date('2024-01-15T12:00:00Z')]
      }

      await expect(schema.validate(invalidData)).rejects.toThrow("Les dates de l'action doivent être comprises dans les dates de la mission")
    })

    it('returns invalid when action start is after mission end', async () => {
      const schema = object().shape(
        getDateRangeSchema({
          isMissionFinished: true,
          missionStartDate,
          missionEndDate
        })
      )

      const invalidData = {
        dates: [new Date('2024-02-15T10:00:00Z'), new Date('2024-02-15T12:00:00Z')]
      }

      await expect(schema.validate(invalidData)).rejects.toThrow("Les dates de l'action doivent être comprises dans les dates de la mission")
    })

    it('returns invalid when action end is after mission end', async () => {
      const schema = object().shape(
        getDateRangeSchema({
          isMissionFinished: true,
          missionStartDate,
          missionEndDate
        })
      )

      const invalidData = {
        dates: [new Date('2024-01-30T10:00:00Z'), new Date('2024-02-15T12:00:00Z')]
      }

      await expect(schema.validate(invalidData)).rejects.toThrow("Les dates de l'action doivent être comprises dans les dates de la mission")
    })

    it('accepts dates exactly at mission boundaries', async () => {
      const schema = object().shape(
        getDateRangeSchema({
          isMissionFinished: true,
          missionStartDate,
          missionEndDate
        })
      )

      const validData = {
        dates: [new Date('2024-01-01T00:00:00Z'), new Date('2024-01-31T23:59:59Z')]
      }

      await expect(schema.validate(validData)).resolves.toBeDefined()
    })

    it('validates only start date when end date is undefined in array', async () => {
      const schema = object().shape(
        getDateRangeSchema({
          isMissionFinished: false,
          missionStartDate,
          missionEndDate
        })
      )

      const validData = {
        dates: [new Date('2024-01-15T10:00:00Z'), undefined]
      }

      await expect(schema.validate(validData)).resolves.toBeDefined()
    })

    it('returns valid when mission has no end date and action is after mission start', async () => {
      const schema = object().shape(
        getDateRangeSchema({
          isMissionFinished: true,
          missionStartDate,
          missionEndDate: undefined
        })
      )

      const validData = {
        dates: [new Date('2024-01-15T10:00:00Z'), new Date('2024-02-15T12:00:00Z')]
      }

      await expect(schema.validate(validData)).resolves.toBeDefined()
    })

    it('skips validation when mission has no start date', async () => {
      const schema = object().shape(
        getDateRangeSchema({
          isMissionFinished: true,
          missionStartDate: undefined,
          missionEndDate
        })
      )

      const validData = {
        dates: [new Date('2023-01-15T10:00:00Z'), new Date('2023-01-15T12:00:00Z')]
      }

      await expect(schema.validate(validData)).resolves.toBeDefined()
    })

    it('returns valid when dates array is empty', async () => {
      const schema = object().shape(
        getDateRangeSchema({
          isMissionFinished: false,
          missionStartDate,
          missionEndDate
        })
      )

      const validData = { dates: [] }
      await expect(schema.validate(validData)).resolves.toBeDefined()
    })

    it('returns valid when dates is null and mission is not finished', async () => {
      const schema = object().shape(
        getDateRangeSchema({
          isMissionFinished: false,
          missionStartDate,
          missionEndDate
        })
      )

      const validData = { dates: null }
      await expect(schema.validate(validData)).resolves.toBeDefined()
    })
  })
})

describe('getSingleDateSchema', () => {
  const missionStartDate = '2024-01-01T00:00:00Z'
  const missionEndDate = '2024-01-31T23:59:59Z'

  describe('when isMissionFinished is true', () => {
    it('requires a date with specific error message', async () => {
      const schema = object().shape({
        date: getSingleDateSchema({
          isMissionFinished: true,
          missionStartDate,
          missionEndDate
        })
      })

      const invalidData = { date: null }
      await expect(schema.validate(invalidData)).rejects.toThrow('La date doit être bien renseignée')
    })

    it('requires a date when undefined', async () => {
      const schema = object().shape({
        date: getSingleDateSchema({
          isMissionFinished: true,
          missionStartDate,
          missionEndDate
        })
      })

      const invalidData = { date: undefined }
      await expect(schema.validate(invalidData)).rejects.toThrow('La date doit être bien renseignée')
    })

    it('returns valid when date is within mission dates', async () => {
      const schema = object().shape({
        date: getSingleDateSchema({
          isMissionFinished: true,
          missionStartDate,
          missionEndDate
        })
      })

      const validData = { date: new Date('2024-01-15T10:00:00Z') }
      await expect(schema.validate(validData)).resolves.toBeDefined()
    })

    it('returns invalid when date is before mission start with specific error message', async () => {
      const schema = object().shape({
        date: getSingleDateSchema({
          isMissionFinished: true,
          missionStartDate,
          missionEndDate
        })
      })

      const invalidData = { date: new Date('2023-12-31T10:00:00Z') }
      await expect(schema.validate(invalidData)).rejects.toThrow("Les dates de l'action doivent être comprises dans les dates de la mission")
    })

    it('returns invalid when date is after mission end with specific error message', async () => {
      const schema = object().shape({
        date: getSingleDateSchema({
          isMissionFinished: true,
          missionStartDate,
          missionEndDate
        })
      })

      const invalidData = { date: new Date('2024-02-15T10:00:00Z') }
      await expect(schema.validate(invalidData)).rejects.toThrow("Les dates de l'action doivent être comprises dans les dates de la mission")
    })

    it('accepts date exactly at mission start', async () => {
      const schema = object().shape({
        date: getSingleDateSchema({
          isMissionFinished: true,
          missionStartDate,
          missionEndDate
        })
      })

      const validData = { date: new Date('2024-01-01T00:00:00Z') }
      await expect(schema.validate(validData)).resolves.toBeDefined()
    })

    it('accepts date exactly at mission end', async () => {
      const schema = object().shape({
        date: getSingleDateSchema({
          isMissionFinished: true,
          missionStartDate,
          missionEndDate
        })
      })

      const validData = { date: new Date('2024-01-31T23:59:59Z') }
      await expect(schema.validate(validData)).resolves.toBeDefined()
    })
  })

  describe('when isMissionFinished is false', () => {
    it('allows null date', async () => {
      const schema = object().shape({
        date: getSingleDateSchema({
          isMissionFinished: false,
          missionStartDate,
          missionEndDate
        })
      })

      const validData = { date: null }
      await expect(schema.validate(validData)).resolves.toBeDefined()
    })

    it('still validates within-mission-dates when date is provided', async () => {
      const schema = object().shape({
        date: getSingleDateSchema({
          isMissionFinished: false,
          missionStartDate,
          missionEndDate
        })
      })

      const invalidData = { date: new Date('2023-12-31T10:00:00Z') }
      await expect(schema.validate(invalidData)).rejects.toThrow()
    })

    it('returns valid when date is within mission dates', async () => {
      const schema = object().shape({
        date: getSingleDateSchema({
          isMissionFinished: false,
          missionStartDate,
          missionEndDate
        })
      })

      const validData = { date: new Date('2024-01-15T10:00:00Z') }
      await expect(schema.validate(validData)).resolves.toBeDefined()
    })
  })

  describe('edge cases', () => {
    it('skips validation when mission has no start date', async () => {
      const schema = object().shape({
        date: getSingleDateSchema({
          isMissionFinished: true,
          missionStartDate: undefined,
          missionEndDate
        })
      })

      const validData = { date: new Date('2023-01-15T10:00:00Z') }
      await expect(schema.validate(validData)).resolves.toBeDefined()
    })

    it('returns valid when mission has no end date and date is after mission start', async () => {
      const schema = object().shape({
        date: getSingleDateSchema({
          isMissionFinished: true,
          missionStartDate,
          missionEndDate: undefined
        })
      })

      const validData = { date: new Date('2024-02-15T10:00:00Z') }
      await expect(schema.validate(validData)).resolves.toBeDefined()
    })

    it('accepts undefined options', async () => {
      const schema = object().shape({
        date: getSingleDateSchema()
      })

      const validData = { date: null }
      await expect(schema.validate(validData)).resolves.toBeDefined()
    })
  })
})
