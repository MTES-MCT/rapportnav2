import { ActionStatusType } from '@common/types/action-types'
import { MissionSourceEnum } from '@common/types/env-mission-types'
import { useStore } from '@tanstack/react-store'
import { render } from '@testing-library/react'
import { describe, expect, it, vi } from 'vitest'
import { ActionType } from '../../../../common/types/action-type'
import { MissionAction } from '../../../../common/types/mission-action'
import InquiryActionHeader from '../inquiry-action-header'
import { CompletenessForStatsStatusEnum } from '../../../../common/types/mission-types.ts'

const action: MissionAction = {
  source: MissionSourceEnum.RAPPORT_NAV,
  id: 'nav-id',
  missionId: '2',
  actionType: ActionType.INQUIRY,
  isCompleteForStats: false,
  status: ActionStatusType.ANCHORED,
  summaryTags: ['tag2'],
  controlsToComplete: [],
  completenessForStats: { status: CompletenessForStatsStatusEnum.INCOMPLETE },
  data: {
    startDateTimeUtc: '2025-01-22T10:00:00Z',
    endDateTimeUtc: '2025-01-22T11:00:00Z',
    observations: 'nav obs',
    controlMethod: 'method',
    vesselIdentifier: 'id',
    vesselType: 'type',
    vesselSize: 'size',
    isVesselRescue: true,
    isPersonRescue: false,
    reason: 'reason',
    nbrOfHours: 2
  }
}

// Mock dependencies
vi.mock('@tanstack/react-store', () => ({
  useStore: vi.fn()
}))

vi.mock('../../hooks/use-inquiry-timeline-registry', () => ({
  getTimeline: () => ({
    icon: '📋',
    title: 'Action'
  })
}))

vi.mock('../../hooks/use-inquiry-timeline-registry')
vi.mock('../../../common/components/layout/action-header-wrapper', () => ({
  default: ({ title, icon }: any) => (
    <div data-testid="header-wrapper">
      {title} {icon}
    </div>
  )
}))

vi.mock('../../../../common/components/elements/action-header-action', () => ({
  default: (props: any) => <div data-testid="header-wrapper">Action Header ....</div>
}))

describe('InquiryActionHeader', () => {
  beforeEach(() => {
    vi.mocked(useStore).mockReturnValue(1)
  })

  it('renders with required props', () => {
    const wrapper = render(<InquiryActionHeader inquiryId="123" action={action} />)
    expect(wrapper).toMatchSnapshot()
  })
})
