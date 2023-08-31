import React from 'react'
import PageWrapper from '../../ui/page-wrapper'
import Mission from './mission'
import { useNavigate, useParams } from 'react-router-dom'
import MissionPageHeader from './mission-page-header'
import MissionPageFooter from './mission-page-footer'

const MissionsPage: React.FC = () => {
  const navigate = useNavigate()
  let { missionsId } = useParams()

  const handleClose = () => {
    navigate('..')
  }

  return (
    <PageWrapper
      showMenu={false}
      header={<MissionPageHeader missionName={`Mission #${missionsId}`} onClickClose={handleClose} />}
      footer={<MissionPageFooter missionName={`Mission #${missionsId}`} onClickClose={handleClose} />}
    >
      <Mission />
    </PageWrapper>
  )
}

export default MissionsPage
