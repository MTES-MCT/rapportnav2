import { InfractionTypeEnum } from '@common/types/env-mission-types'
import { renderHook } from '@testing-library/react'
import { describe, expect, it } from 'vitest'
import { TargetInfraction } from '../../../common/types/target-types'
import { useInfractionForm } from '../use-infraction-form'

describe('useInfractionForm validationSchema', () => {
  // Stable reference: useAbstractFormik runs `useEffect(setInitValue, [value])`, so a fresh object
  // each render would re-fire the effect → re-render → infinite loop (hangs the test).
  const initialValue = {} as TargetInfraction

  // editInfraction=true so the infraction rules are part of the schema
  const infractionSchema = () => {
    const { result } = renderHook(() =>
      useInfractionForm(initialValue, undefined, undefined, false, false, true)
    )
    return result.current.validationSchema!
  }

  it('rejects an infraction without a report type (infractionType)', async () => {
    await expect(infractionSchema().validate({ infraction: { natinfs: ['4473'] } })).rejects.toThrow()
  })

  it('rejects an infraction without natinfs', async () => {
    await expect(
      infractionSchema().validate({ infraction: { natinfs: [], infractionType: InfractionTypeEnum.WITH_REPORT } })
    ).rejects.toThrow()
  })

  it.each([InfractionTypeEnum.WITHOUT_REPORT, InfractionTypeEnum.WITH_REPORT, InfractionTypeEnum.WAITING])(
    'accepts an infraction with natinfs and report type %s',
    async infractionType => {
      await expect(
        infractionSchema().validate({ infraction: { natinfs: ['4473'], infractionType } })
      ).resolves.toBeTruthy()
    }
  )
})
