import { render, screen } from '../../../../../../test-utils.tsx'
import MissionListPageContentWrapper from '../mission-list-page-content-wrapper.tsx'

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

  // --- pagination / filtered empty state additions ---------------------------------------------------

  const baseProps = {
    title: 'Mes rapports',
    list: <div>LIST_CONTENT</div>
  }

  it('renders the list when there are missions', () => {
    render(<MissionListPageContentWrapper {...baseProps} loading={false} hasMissions={true} />)
    expect(screen.getByText('LIST_CONTENT')).toBeInTheDocument()
  })

  it('renders the custom empty state (e.g. filtered no-result) instead of the default message', () => {
    render(
      <MissionListPageContentWrapper
        {...baseProps}
        loading={false}
        hasMissions={false}
        emptyState={<div>CUSTOM_EMPTY_STATE</div>}
      />
    )
    expect(screen.getByText('CUSTOM_EMPTY_STATE')).toBeInTheDocument()
    expect(screen.queryByText('Aucune mission pour cette période de temps.')).not.toBeInTheDocument()
    expect(screen.queryByText('LIST_CONTENT')).not.toBeInTheDocument()
  })

  it('prefers the offline message over the custom empty state when offline', () => {
    render(
      <MissionListPageContentWrapper
        {...baseProps}
        loading={false}
        hasMissions={false}
        isOffline={true}
        emptyState={<div>CUSTOM_EMPTY_STATE</div>}
      />
    )
    expect(screen.getByText('Veuillez repasser en ligne pour resynchroniser.')).toBeInTheDocument()
    expect(screen.queryByText('CUSTOM_EMPTY_STATE')).not.toBeInTheDocument()
  })

  it('shows the loader while loading', () => {
    render(<MissionListPageContentWrapper {...baseProps} loading={true} hasMissions={false} />)
    expect(screen.getByTestId('mission-list-loader')).toBeInTheDocument()
  })
})
