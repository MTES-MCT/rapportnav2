import { QueryClientProvider } from '@tanstack/react-query'
import { queryClient } from '../../../../../../query-client'
import { render } from '../../../../../../test-utils'
import MissionListPageHeaderWrapper from '../mission-list-page-header-wrapper'

describe('MissionListPageHeaderWrapper', () => {
  it('should match the snapshot', () => {
    const wrapper = render(
      <QueryClientProvider client={queryClient}>
        <MissionListPageHeaderWrapper title={<>Title</>} actions={<>Actions</>} />
      </QueryClientProvider>
    )
    expect(wrapper).toMatchSnapshot()
  })
})
