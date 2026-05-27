import Text from '@common/components/ui/text.tsx'
import { useGlobalRoutes } from '@router/use-global-routes.tsx'
import { useSelector } from '@tanstack/react-store'
import React from 'react'
import MissionListPageHeaderWrapper from '../features/common/components/layout/mission-list-page-header-wrapper.tsx'
import MissionListPageWrapper from '../features/common/components/layout/mission-list-page-wrapper.tsx'
import MissionListPageSidebarWrapper from '../features/common/components/ui/mission-list-page-sidebar.tsx'
import MissionListPageTitle from '../features/common/components/ui/mission-list-page-title.tsx'
import useMetabaseEmbedQuery from '../features/metabase/services/use-metabase-embed.tsx'
import { store } from '../store'

const MetabasePage: React.FC = () => {
  const { getSidebarItems } = useGlobalRoutes()
  const user = useSelector(store, state => state.user)
  const { data, isLoading, error } = useMetabaseEmbedQuery()

  return (
    <MissionListPageWrapper
      header={<MissionListPageHeaderWrapper title={<MissionListPageTitle user={user} />} />}
      sidebar={<MissionListPageSidebarWrapper defaultItemKey="metabase" items={getSidebarItems()} />}
      footer={undefined}
    >
      <div
        style={{
          display: 'flex',
          flexDirection: 'column',
          width: '100%',
          height: '100%',
          minHeight: 'calc(100vh - 120px)',
          padding: '1rem'
        }}
      >
        {isLoading && <Text as="p">Chargement du dashboard...</Text>}
        {error && <Text as="p">Erreur lors du chargement du dashboard Metabase.</Text>}
        {data?.iframeUrl && (
          <iframe
            title="Metabase"
            src={data.iframeUrl}
            width="100%"
            style={{ flex: 1, border: 'none', minHeight: '600px' }}
            sandbox="allow-scripts allow-same-origin"
          />
        )}
      </div>
    </MissionListPageWrapper>
  )
}

export default MetabasePage
