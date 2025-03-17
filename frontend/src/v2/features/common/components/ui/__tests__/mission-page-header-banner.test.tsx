import { MissionSourceEnum } from '@common/types/env-mission-types'
import { render } from '../../../../../../test-utils'
import { CompletenessForStatsStatusEnum } from '../../../types/mission-types'
import MissionPageHeaderBanner from '../mission-page-header-banner'

describe('MissionPageHeaderBanner', () => {
  it('should match the snapshot', () => {
    const completenessForStats = {
      sources: [MissionSourceEnum.MONITORENV],
      status: CompletenessForStatsStatusEnum.INCOMPLETE
    }
    const wrapper = render(<MissionPageHeaderBanner completenessForStats={completenessForStats} />)
    expect(wrapper).toMatchSnapshot()
  })
})
