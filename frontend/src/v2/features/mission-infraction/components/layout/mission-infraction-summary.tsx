import Text from '@common/components/ui/text.tsx'
import { ControlType } from '@common/types/control-types.ts'
import { Accent, Icon, IconButton, Size, THEME } from '@mtes-mct/monitor-ui'
import React, { createElement, FunctionComponent } from 'react'
import { Stack } from 'rsuite'
import { setDebounceTime } from '../../../../store/slices/delay-query-reducer.ts'
import { Infraction } from '../../../common/types/target-types.ts'
import { useInfraction } from '../../hooks/use-infraction.tsx'
import { FishInfraction } from '../../types/infraction-input.tsx'
import MissionInfractionEmpty from '../ui/mission-infraction-empty.tsx'
import MissionInfractionTypeTag from '../ui/mission-infraction-type-tag.tsx'

interface MissionInfractionSummaryProps {
  title?: string
  showIndex?: boolean
  isNavAction?: boolean
  controlType?: ControlType
  isActionDisabled?: boolean
  infractions: FishInfraction[] | Infraction[]
  onEdit?: (id?: string) => void
  onDelete?: (id?: string) => void
  summary: FunctionComponent<{ infraction: FishInfraction | Infraction }>
}

const MissionInfractionSummary: React.FC<MissionInfractionSummaryProps> = ({
  title,
  onEdit,
  summary,
  onDelete,
  showIndex,
  controlType,
  infractions,
  isActionDisabled
}) => {
  const { getInfractionByControlTypeTitle } = useInfraction()
  const titleControlType = controlType ? getInfractionByControlTypeTitle(controlType) : ''

  const handleDelete = (id?: string) => {
    if (!onDelete) return
    onDelete(id)
    setDebounceTime(0)
  }

  return (
    <>
      {infractions?.length === 0 ? (
        <MissionInfractionEmpty />
      ) : (
        infractions.map((infraction, index) => (
          <Stack
            key={`${infraction.id}-${index}`}
            direction="column"
            spacing={'0.5rem'}
            style={{
              width: '100%',
              padding: '1rem',
              backgroundColor: THEME.color.cultured,
              marginBottom: index === infractions.length - 1 ? 0 : '0.1rem'
            }}
          >
            <Stack.Item style={{ width: '100%' }}>
              <Stack direction="row" alignItems="center" justifyContent="space-between" spacing={'0.5rem'}>
                <Stack.Item>
                  <Text as="h3" weight="bold" color={THEME.color.gunMetal}>
                    {`${title ?? titleControlType} ${showIndex ? index + 1 : ''}`}
                  </Text>
                </Stack.Item>
                <Stack.Item>
                  <Stack direction="row" alignItems="baseline" spacing={'0.5rem'}>
                    <Stack.Item>
                      <IconButton
                        size={Size.NORMAL}
                        role="edit-infraction"
                        accent={Accent.SECONDARY}
                        Icon={Icon.EditUnbordered}
                        disabled={isActionDisabled}
                        onClick={() => {
                          if (onEdit) onEdit(infraction.id)
                        }}
                      />
                    </Stack.Item>
                    <Stack.Item>
                      <IconButton
                        Icon={Icon.Delete}
                        size={Size.NORMAL}
                        role="delete-infraction"
                        accent={Accent.SECONDARY}
                        disabled={isActionDisabled}
                        onClick={() => handleDelete(infraction.id)}
                      />
                    </Stack.Item>
                  </Stack>
                </Stack.Item>
              </Stack>
            </Stack.Item>
            <Stack.Item style={{ width: '100%' }}>
              <Stack direction="row" spacing={'0.5rem'}>
                <Stack.Item>
                  <MissionInfractionTypeTag type={infraction.infractionType} />
                </Stack.Item>
                <Stack.Item>{createElement(summary, { infraction })}</Stack.Item>
              </Stack>
            </Stack.Item>
            <Stack.Item style={{ width: '100%' }}>
              <Text as="h3">{infraction?.observations ? infraction?.observations : 'Aucune observation'}</Text>
            </Stack.Item>
          </Stack>
        ))
      )}
    </>
  )
}

export default MissionInfractionSummary
