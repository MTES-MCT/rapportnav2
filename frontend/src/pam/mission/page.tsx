import React from 'react'
import PageWrapper from '../../ui/page-wrapper'
import Mission from './mission'
import { useNavigate, useParams } from 'react-router-dom'
import MissionPageHeader from './page-header'
import MissionPageFooter from './page-footer'

const MissionsPage: React.FC = () => {
  const navigate = useNavigate()
  let { missionId } = useParams()

  const handleClose = () => {
    navigate('..')
  }

  return (
    <PageWrapper
      showMenu={false}
      header={<MissionPageHeader missionName={`Mission #${missionId}`} onClickClose={handleClose} />}
      footer={<MissionPageFooter missionName={`Mission #${missionId}`} onClickClose={handleClose} />}
    >
      <Mission />
    </PageWrapper>
  )
}

export default MissionsPage
