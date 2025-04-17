import { Accent, Icon, IconButton, Size } from '@mtes-mct/monitor-ui'
import React from 'react'
import { Stack } from 'rsuite'
import WithIconButton from '../../../common/components/ui/with-icon-button'
import { MissionSourceEnum } from '../../../common/types/mission-types'

interface MissionTargetEnvActionProps {
  showDetail: boolean
  enableForm?: boolean
  onDelete?: () => void
  source?: MissionSourceEnum
  setShowDetail: (showDetail: boolean) => void
}

const MissionTargetEnvAction: React.FC<MissionTargetEnvActionProps> = ({
  source,
  onDelete,
  showDetail,
  enableForm,
  setShowDetail
}) => {
  const handleDelete = () => {
    if (onDelete) onDelete()
  }

  return (
    <Stack direction="row" alignItems="baseline" spacing={'0.5rem'} justifyContent={'flex-end'}>
      <Stack.Item style={{ width: '100%' }}>
        <WithIconButton
          icon={Icon.Plus}
          onClick={() => setShowDetail(!showDetail)}
          label={'Infraction pour cette cible'}
          disabled={showDetail || !enableForm}
        />
      </Stack.Item>
      {source === MissionSourceEnum.MONITORENV && (
        <Stack.Item style={{ justifyContent: 'flex-end' }}>
          <IconButton
            disabled={false}
            size={Size.NORMAL}
            accent={Accent.SECONDARY}
            role="display-target"
            Icon={showDetail ? Icon.Hide : Icon.Display}
            onClick={() => setShowDetail(!showDetail)}
          />
        </Stack.Item>
      )}
      {source !== MissionSourceEnum.MONITORENV && (
        <Stack.Item>
          <IconButton
            Icon={Icon.Delete}
            size={Size.NORMAL}
            role="delete-target"
            onClick={handleDelete}
            accent={Accent.SECONDARY}
          />
        </Stack.Item>
      )}
    </Stack>
  )
}

export default MissionTargetEnvAction
