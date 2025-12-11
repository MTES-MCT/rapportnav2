import { render } from '../../../../../../test-utils'
import MissionListPageContentWrapper from '../mission-list-page-content-wrapper'
import { expect } from 'vitest'
import { screen } from '../../../../../../test-utils.tsx'

describe('MissionListPageContentWrapper', () => {
  it('should match the snapshot', () => {
    const wrapper = render(
      <MissionListPageContentWrapper
        title="my title"
        loading={false}
        hasMissions={false}
        list={<>My lists</>}
        filters={<>Title</>}
        actions={<>Actions</>}
      />
    )
    expect(wrapper).toMatchSnapshot()
  })

  it('should show offline message when no missions and offline', () => {
    render(
      <MissionListPageContentWrapper
        title="my title"
        loading={false}
        hasMissions={false}
        list={<>My lists</>}
        filters={<>Title</>}
        actions={<>Actions</>}
        isOffline={true}
      />
    )
    expect(screen.getByText('Veuillez repasser en ligne pour resynchroniser.')).toBeInTheDocument()
  })

  it('should show no missions message when no missions and online', () => {
    render(
      <MissionListPageContentWrapper
        title="my title"
        loading={false}
        hasMissions={false}
        list={<>My lists</>}
        filters={<>Title</>}
        actions={<>Actions</>}
        isOffline={false}
      />
    )
    expect(screen.getByText('Aucune mission pour cette période de temps.')).toBeInTheDocument()
  })
})
