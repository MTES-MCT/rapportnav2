import { render } from '../../../../../../test-utils'
import { CompletenessForStatsStatusEnum, MissionSourceEnum } from '../../../types/mission-types'
import { NetworkSyncStatus } from '../../../types/network-types'
import ActionHeaderCompletenessForStats from '../../ui/action-header-completeness-for-stats'

describe('ActionHeaderCompletenessForStats', () => {
  it('should match the snapshot', () => {
    const wrapper = render(
      <ActionHeaderCompletenessForStats
        isMissionFinished={false}
        networkSyncStatus={NetworkSyncStatus.SYNC}
        completenessForStats={{
          status: CompletenessForStatsStatusEnum.COMPLETE,
          sources: [MissionSourceEnum.RAPPORT_NAV]
        }}
      />
    )
    expect(wrapper).toMatchSnapshot()
  })
})
