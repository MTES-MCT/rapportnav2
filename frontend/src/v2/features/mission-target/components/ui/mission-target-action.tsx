import { Accent, Icon, IconButton, Size } from '@mtes-mct/monitor-ui'
import React from 'react'
import { Stack } from 'rsuite'
import { MissionSourceEnum } from '../../../common/types/mission-types'

interface MissionTargetActionProps {
  showDetail: boolean
  disabledAdd?: boolean
  onEdit: () => void
  onDelete: () => void
  source?: MissionSourceEnum
  onAddInfraction: () => void
  onShowDetail: () => void
}

const MissionTargetAction: React.FC<MissionTargetActionProps> = ({
  source,
  onEdit,
  onDelete,
  showDetail,
  disabledAdd,
  onShowDetail,
  onAddInfraction
}) => {
  const handleEdit = () => {
    if (onEdit) onEdit()
  }

  const handleDelete = () => {
    if (onDelete) onDelete()
  }

  const handleAddInfraction = () => {
    if (onAddInfraction) onAddInfraction()
  }

  return (
    <Stack direction="row" alignItems="baseline" spacing={'0.5rem'} justifyContent={'flex-end'}>
      <Stack.Item style={{ width: '100%' }}>
        <IconButton
          Icon={Icon.Plus}
          role="show-target"
          disabled={disabledAdd}
          accent={Accent.SECONDARY}
          onClick={handleAddInfraction}
          title={'Infraction pour cette cible'}
        />
      </Stack.Item>
      {source === MissionSourceEnum.MONITORENV && (
        <Stack.Item style={{ justifyContent: 'flex-end' }}>
          <IconButton
            disabled={false}
            size={Size.NORMAL}
            role="display-target"
            onClick={onShowDetail}
            accent={Accent.SECONDARY}
            title={'Afficher cette cible'}
            Icon={showDetail ? Icon.Hide : Icon.Display}
          />
        </Stack.Item>
      )}
      {source !== MissionSourceEnum.MONITORENV && (
        <Stack.Item>
          <IconButton
            size={Size.NORMAL}
            role="edit-target"
            onClick={handleEdit}
            Icon={Icon.EditUnbordered}
            accent={Accent.SECONDARY}
            title={'Editer cette cible'}
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
            title={'Supprimer cette cible'}
          />
        </Stack.Item>
      )}
    </Stack>
  )
}

export default MissionTargetAction
