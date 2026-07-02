import { Accent, FormikCheckbox, Icon, IconButton, Size, THEME } from '@mtes-mct/monitor-ui'
import { FC } from 'react'
import { Stack } from 'rsuite'

interface InspectorItemHeaderProps {
  edit: boolean
  onEdit: () => void
  onDelete: () => void
}

const InspectorItemHeader: FC<InspectorItemHeaderProps> = ({ edit, onEdit, onDelete }) => {
  return (
    <Stack direction="row" spacing="0.75rem" justifyContent="flex-start">
      <Stack.Item style={{ width: '50%', display: 'flex', justifyContent: 'flex-start', alignItems: 'start' }}>
        <FormikCheckbox readOnly={!edit} label="Etablissement étranger" name="isOutOfUnit" />
      </Stack.Item>
      <Stack.Item style={{ width: '50%', display: 'flex', justifyContent: 'flex-end' }}>
        {!edit && (
          <Stack direction="row" spacing="0.75rem" justifyContent="flex-start">
            <Stack.Item>
              <IconButton
                title="Editer"
                onClick={onEdit}
                size={Size.NORMAL}
                accent={Accent.SECONDARY}
                Icon={Icon.EditUnbordered}
                color={THEME.color.charcoal}
              />
            </Stack.Item>
            <Stack.Item>
              <IconButton
                title="Supprimer"
                onClick={onDelete}
                Icon={Icon.Delete}
                size={Size.NORMAL}
                accent={Accent.SECONDARY}
                color={THEME.color.maximumRed}
              />
            </Stack.Item>
          </Stack>
        )}
      </Stack.Item>
    </Stack>
  )
}

export default InspectorItemHeader
