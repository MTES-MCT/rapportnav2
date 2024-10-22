import { ActionTypeEnum, MissionSourceEnum } from '@common/types/env-mission-types'
import { FC } from 'react'
import MissionActionWrapper from '../../mission-action/components/layout/mission-action-wrapper'
import useActionByIdQuery from '../../mission-action/services/use-action-by-id'
import MissionActionItemPam from './mission-action-item-pam'

interface MissionActionProps {
  actionId?: string
  missionId?: string
}

const MissionActionPam: FC<MissionActionProps> = ({ missionId, actionId }) => {
  const {
    data: action,
    loading,
    error
  } = useActionByIdQuery(actionId, missionId, MissionSourceEnum.RAPPORTNAV, ActionTypeEnum.CONTROL)
  return (
    <MissionActionWrapper
      action={action}
      isError={error}
      isLoading={loading}
      missionId={action?.missionId}
      item={MissionActionItemPam}
    />
  )
}

export default MissionActionPam
