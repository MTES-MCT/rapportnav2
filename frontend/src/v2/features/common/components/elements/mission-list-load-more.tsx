import { Accent, Button, Icon } from '@mtes-mct/monitor-ui'
import { FC } from 'react'
import { Stack } from 'rsuite'
import { MISSION_LIST_PAGE_SIZE } from '../../services/use-missions.tsx'

interface MissionListLoadMoreProps {
  hasNextPage: boolean
  isFetchingNextPage: boolean
  onLoadMore: () => void
}

/**
 * "Load more" control for the paginated mission list, rendered at the bottom of the scrollable list. It is
 * hidden entirely once the API reports no further page (`hasNextPage === false`); while a page is loading it
 * stays visible but disabled.
 */
const MissionListLoadMore: FC<MissionListLoadMoreProps> = ({ hasNextPage, isFetchingNextPage, onLoadMore }) => {
  if (!hasNextPage) {
    return null
  }

  return (
    <Stack justifyContent="center" style={{ width: '100%', padding: '1.5rem 0' }}>
      <Stack.Item>
        <Button
          accent={Accent.SECONDARY}
          Icon={Icon.Display}
          disabled={isFetchingNextPage}
          onClick={onLoadMore}
        >
          {isFetchingNextPage ? 'Chargement…' : `Afficher les ${MISSION_LIST_PAGE_SIZE} missions suivantes`}
        </Button>
      </Stack.Item>
    </Stack>
  )
}

export default MissionListLoadMore
