import React from 'react'
import { Stack } from 'rsuite'
import { Accent, Icon, IconButton, Size, THEME } from '@mtes-mct/monitor-ui'
import Text from '../../../ui/text'
import { ControlType } from '../../../types/control-types'
import { Infraction } from '../../../types/infraction-types'
import { infractionTitleForControlType } from './utils'
import InfractionTypeTag from './infraction-type-tag.tsx'
import NatinfsTag from './natinfs-tag.tsx'

interface InfractionSummaryProps {
  controlType: ControlType
  infractions?: Infraction[]
  onEdit: (infraction: Infraction) => void
  onDelete: (infraction: Infraction) => void
}

const InfractionSummary: React.FC<InfractionSummaryProps> = ({ infractions, controlType, onEdit, onDelete }) => {
  return (
    <>
      {infractions?.map((infraction: Infraction) => (
        <Stack key={infraction.id} direction="column" spacing={'0.5rem'} style={{ width: '100%' }}>
          <Stack.Item style={{ width: '100%' }}>
            <Stack direction="row" alignItems="center" justifyContent="space-between" spacing={'0.5rem'}>
              <Stack.Item>
                <Text as="h3" weight="bold" color={THEME.color.gunMetal}>
                  {infractionTitleForControlType(controlType)}
                </Text>
              </Stack.Item>
              <Stack.Item>
                <Stack direction="row" alignItems="baseline" spacing={'0.5rem'}>
                  <Stack.Item>
                    <IconButton
                      Icon={Icon.EditUnbordered}
                      accent={Accent.SECONDARY}
                      size={Size.NORMAL}
                      role="edit-infraction"
                      onClick={() => onEdit(infraction)}
                    />
                  </Stack.Item>
                  <Stack.Item>
                    <IconButton
                      Icon={Icon.Delete}
                      accent={Accent.SECONDARY}
                      size={Size.NORMAL}
                      role="delete-infraction"
                      onClick={() => onDelete(infraction)}
                    />
                  </Stack.Item>
                </Stack>
              </Stack.Item>
            </Stack>
          </Stack.Item>
          <Stack.Item style={{ width: '100%' }}>
            <Stack direction="row" spacing={'0.5rem'}>
              <Stack.Item>
                <InfractionTypeTag type={infraction.infractionType} />
              </Stack.Item>
              <Stack.Item>
                <NatinfsTag natinfs={infraction.natinfs} />
              </Stack.Item>
            </Stack>
          </Stack.Item>
          <Stack.Item style={{ width: '100%' }}>
            <Text as="h3">{infraction?.observations ? infraction?.observations : 'Aucune observation'}</Text>
          </Stack.Item>
        </Stack>
      ))}
    </>
  )
}

export default InfractionSummary
