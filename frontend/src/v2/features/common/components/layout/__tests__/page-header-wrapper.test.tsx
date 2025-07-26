import { vi } from 'vitest'
import { render } from '../../../../../../test-utils'
import PageHeaderWrapper from '../page-header-wrapper'

const onClose = vi.fn()

describe('PageHeaderWrapper', () => {
  it('should show banner message', async () => {
    const wrapper = render(
      <PageHeaderWrapper
        banner={<>Banner</>}
        date={<>Date</>}
        tags={<>tags</>}
        utcTime={<>UTC time</>}
        onClickClose={onClose}
      />
    )
    expect(wrapper).toMatchSnapshot()
  })
})

/**
 * 
 * 
    const mission = {
      status: MissionStatusEnum.ENDED,
      completenessForStats: {
        sources: [MissionSourceEnum.MONITORENV],
        status: CompletenessForStatsStatusEnum.COMPLETE
      }
    } as Mission2

    render(<MissionPageHeader onClickClose={onClose} mission={mission} />)
    waitFor(() => expect(screen.getByText('La mission est terminée et ses données sont complètes')).toBeInTheDocument())
 */
