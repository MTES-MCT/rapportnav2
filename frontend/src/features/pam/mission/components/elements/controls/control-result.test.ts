import { ControlResult } from '../../../../../common/types/control-types.ts'
import { ControlResultExtraOptions, controlResultOptions } from './control-result.ts'

describe('controlResultOptions', () => {
  it('should return default options when no extra options are provided', () => {
    const result = controlResultOptions()
    expect(result).toEqual([
      { label: 'Oui', value: ControlResult.YES },
      { label: 'Non', value: ControlResult.NO },
      { label: 'Non contrôlé', value: ControlResult.NOT_CONTROLLED }
    ])
  })

  it('should include extra options when provided', () => {
    const result = controlResultOptions([ControlResultExtraOptions.NOT_CONCERNED])
    expect(result).toEqual([
      { label: 'Oui', value: ControlResult.YES },
      { label: 'Non', value: ControlResult.NO },
      { label: 'Non contrôlé', value: ControlResult.NOT_CONTROLLED },
      { label: 'Non concerné', value: ControlResult.NOT_CONCERNED }
    ])
  })

  // it('should handle invalid extra options gracefully', () => {
  //   // If an invalid option is provided, it should not affect the result
  //   const result = controlResultOptions(['INVALID_OPTION' as any])
  //   expect(result).toEqual([
  //     { label: 'Oui', value: ControlResult.YES },
  //     { label: 'Non', value: ControlResult.NO },
  //     { label: 'Non contrôlé', value: ControlResult.NOT_CONTROLLED }
  //   ])
  // })
})
