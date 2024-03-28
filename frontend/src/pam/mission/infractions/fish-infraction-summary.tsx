import React from 'react'
import { Stack } from 'rsuite'
import { Accent, Icon, IconButton, Label, Size, THEME } from '@mtes-mct/monitor-ui'
import Text from '../../../ui/text'
import InfractionTypeTag from './infraction-type-tag.tsx'
import NatinfsTag from './natinfs-tag.tsx'
import {
  GearInfraction,
  LogbookInfraction,
  OtherInfraction,
  SpeciesInfraction
} from '../../../types/fish-mission-types.ts'

interface FishInfractionSummaryProps {
  title: string
  infractions?: LogbookInfraction[] | GearInfraction[] | SpeciesInfraction[] | OtherInfraction[]
}

const FishInfractionSummary: React.FC<FishInfractionSummaryProps> = ({ infractions, title }) => {
  return (
    <>
      {!!!infractions?.length ? (
        <Stack
          direction="column"
          style={{
            width: '100%',
            backgroundColor: THEME.color.white,
            padding: '1rem'
          }}
        >
          <Stack.Item style={{ width: '100%' }}>
            <Label>Infractions</Label>
          </Stack.Item>
          <Stack.Item style={{ width: '100%' }}>
            <Text as="h3" weight="medium">
              Aucune infraction
            </Text>
          </Stack.Item>
        </Stack>
      ) : (
        infractions?.map((infraction: any, i: number) => (
          <Stack
            key={infraction.id}
            direction="column"
            spacing={'0.5rem'}
            style={{
              width: '100%',
              marginBottom: i === infractions.length - 1 ? 0 : '0.1rem',
              backgroundColor: THEME.color.white,
              padding: '1rem'
            }}
          >
            <Stack.Item style={{ width: '100%' }}>
              <Stack direction="row" alignItems="center" justifyContent="space-between" spacing={'0.5rem'}>
                <Stack.Item>
                  <Text as="h3" weight="bold" color={THEME.color.gunMetal}>
                    {`${title} ${i + 1}`}
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
                        disabled={true}
                      />
                    </Stack.Item>
                    <Stack.Item>
                      <IconButton Icon={Icon.Delete} accent={Accent.SECONDARY} size={Size.NORMAL} disabled={true} />
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
                  <NatinfsTag natinfs={infraction.natinf ? [`${infraction.natinf}`] : undefined} />
                </Stack.Item>
              </Stack>
            </Stack.Item>
            <Stack.Item style={{ width: '100%' }}>
              <Text as="h3">{infraction?.comments ? infraction?.comments : 'Aucune observation'}</Text>
            </Stack.Item>
          </Stack>
        ))
      )}
    </>
  )
}

export default FishInfractionSummary
