import Text from '@common/components/ui/text.tsx'
import { Accent, Icon, IconButton, Size, THEME } from '@mtes-mct/monitor-ui'
import React, { JSX } from 'react'
import { Stack } from 'rsuite'

interface InfractionSummaryProps {
  index: number
  title?: string
  tags: JSX.Element
  observations?: string
  footerTag?: JSX.Element
  isActionDisabled?: boolean
  onEdit?: () => void
  onDelete?: () => void
}

const InfractionSummary: React.FC<InfractionSummaryProps> = ({
  index,
  tags,
  title,
  onEdit,
  onDelete,
  footerTag,
  observations,
  isActionDisabled
}) => {
  return (
    <Stack
      key={`infraction-summary-${index}`}
      direction="column"
      spacing={'0.5rem'}
      style={{
        width: '100%',
        padding: '1rem',
        backgroundColor: THEME.color.cultured
      }}
      data-testid="infraction-summary"
    >
      <Stack.Item style={{ width: '100%' }}>
        <Stack direction="row" alignItems="center" justifyContent="space-between" spacing={'0.5rem'}>
          <Stack.Item style={{ width: '70%' }}>
            <Text as="h3" weight="bold" color={THEME.color.gunMetal}>
              {title}
            </Text>
          </Stack.Item>
          <Stack.Item style={{ width: '30%', display: 'flex', justifyContent: 'flex-end' }}>
            <Stack direction="row" alignItems="baseline" spacing={'0.5rem'}>
              {onEdit && (
                <Stack.Item>
                  <IconButton
                    onClick={onEdit}
                    size={Size.NORMAL}
                    role="edit-infraction"
                    accent={Accent.SECONDARY}
                    Icon={Icon.EditUnbordered}
                    disabled={isActionDisabled}
                  />
                </Stack.Item>
              )}
              {onDelete && (
                <Stack.Item>
                  <IconButton
                    Icon={Icon.Delete}
                    size={Size.NORMAL}
                    onClick={onDelete}
                    role="delete-infraction"
                    accent={Accent.SECONDARY}
                    disabled={isActionDisabled}
                  />
                </Stack.Item>
              )}
            </Stack>
          </Stack.Item>
        </Stack>
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>{tags}</Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <Stack direction="row" alignItems="center" spacing={'0.5rem'}>
          <Stack.Item style={{ width: '70%' }}>
            <Text as="h3" color={THEME.color.slateGray}>
              {observations ?? 'Aucune observation'}
            </Text>
          </Stack.Item>
          <Stack.Item style={{ width: '30%', display: 'flex', justifyContent: 'flex-end' }}>{footerTag}</Stack.Item>
        </Stack>
      </Stack.Item>
    </Stack>
  )
}

export default InfractionSummary
