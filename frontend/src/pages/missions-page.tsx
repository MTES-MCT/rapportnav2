import React from 'react'
import PageWrapper from '../features/common/components/ui/page-wrapper.tsx'
import Missions from '../features/pam/missions/components/missions.tsx'

const MissionsPage: React.FC = () => {
  return (
    <PageWrapper>
      <Missions />
    </PageWrapper>
  )
}

export default MissionsPage
