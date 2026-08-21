import { vi } from 'vitest'
import { fireEvent, render, screen } from '../../../../../../test-utils.tsx'
import { MISSION_LIST_PAGE_SIZE } from '../../../services/use-missions.tsx'
import MissionListLoadMore from '../mission-list-load-more.tsx'

describe('MissionListLoadMore', () => {
  test('renders the page-size label and triggers onLoadMore when there is a next page', () => {
    const onLoadMore = vi.fn()
    render(<MissionListLoadMore hasNextPage={true} isFetchingNextPage={false} onLoadMore={onLoadMore} />)

    const label = screen.getByText(`Afficher les ${MISSION_LIST_PAGE_SIZE} missions suivantes`)
    expect(label).toBeInTheDocument()

    const button = screen.getByRole('button')
    expect(button).toBeEnabled()
    fireEvent.click(button)
    expect(onLoadMore).toHaveBeenCalledTimes(1)
  })

  test('renders nothing when there is no next page', () => {
    const onLoadMore = vi.fn()
    const { container } = render(
      <MissionListLoadMore hasNextPage={false} isFetchingNextPage={false} onLoadMore={onLoadMore} />
    )

    expect(screen.queryByRole('button')).not.toBeInTheDocument()
    expect(container).toBeEmptyDOMElement()
  })

  test('shows a loading label and is disabled while fetching the next page', () => {
    render(<MissionListLoadMore hasNextPage={true} isFetchingNextPage={true} onLoadMore={vi.fn()} />)
    expect(screen.getByText('Chargement…')).toBeInTheDocument()
    expect(screen.getByRole('button')).toBeDisabled()
  })
})
