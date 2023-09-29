import React from 'react'
import PageWrapper from '../../ui/page-wrapper'
import Mission from './mission'
import { useNavigate, useParams } from 'react-router-dom'
import MissionPageHeader from './page-header'
import MissionPageFooter from './page-footer'
import { useApolloClient } from '@apollo/client'

const MissionsPage: React.FC = () => {
  const navigate = useNavigate()
  let { missionId } = useParams()
  const apolloClient = useApolloClient()

  const exitMission = () => {
    // TODO centralise the following into a class - also used in use-auth()
    // reset apollo store
    apolloClient.resetStore()
    // flush apollo persist cache
    apolloClient.cache.evict({})

    navigate('..')
  }

  return (
    <PageWrapper
      showMenu={false}
      header={<MissionPageHeader missionName={`Mission #${missionId}`} onClickClose={exitMission} />}
      footer={<MissionPageFooter missionName={`Mission #${missionId}`} exitMission={exitMission} />}
    >
      <Mission />
    </PageWrapper>
  )
}

export default MissionsPage
