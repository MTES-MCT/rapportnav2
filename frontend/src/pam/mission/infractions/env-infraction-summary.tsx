import React from 'react'
import { Stack } from 'rsuite'
import { Accent, Icon, Size, THEME, IconButton, Tag, Button } from '@mtes-mct/monitor-ui'
import Title from '../../../ui/title'
import { controlTitle, vesselTypeToHumanString } from '../controls/utils'
import { Infraction, InfractionByTarget } from '../../mission-types'

interface EnvInfractionSummaryProps {
  infractionByTarget?: InfractionByTarget
  onAddInfractionForTarget: (infraction?: Partial<Infraction>) => void
  onEditInfractionForTarget: (infraction: Infraction) => void
  onDeleteInfraction: (infractionId: string) => void
}

const EnvInfractionSummary: React.FC<EnvInfractionSummaryProps> = ({
  infractionByTarget,
  onAddInfractionForTarget,
  onEditInfractionForTarget,
  onDeleteInfraction
}) => {
  return (
    <Stack direction="column" spacing={'0.5rem'} style={{ width: '100%' }}>
      <Stack.Item style={{ width: '100%' }}>
        <Stack direction="row" alignItems="center" justifyContent="space-between" spacing={'0.5rem'}>
          <Stack.Item>
            <Title as="h3" weight="bold" color={THEME.color.gunMetal}>
              {`${vesselTypeToHumanString(infractionByTarget?.vesselType)} - ${infractionByTarget?.vesselIdentifier}`}
            </Title>
          </Stack.Item>
          <Stack.Item>
            <Button
              onClick={() =>
                onAddInfractionForTarget({
                  target: infractionByTarget?.infractions?.find(infraction => !!infraction.target)?.target
                })
              }
              accent={Accent.SECONDARY}
              size={Size.NORMAL}
              Icon={Icon.Plus}
            >
              infraction pour cette cible
            </Button>
          </Stack.Item>
        </Stack>
      </Stack.Item>
      {infractionByTarget?.infractions.map((infraction: Infraction) => (
        <Stack.Item style={{ width: '100%' }}>
          <Stack direction="row" spacing={'0.5rem'} justifyContent="space-between">
            <Stack.Item>
              <Stack direction="row" spacing={'0.5rem'}>
                <Stack.Item>
                  <Title as="h3" weight="bold" color={THEME.color.gunMetal}>
                    {!infraction?.controlType
                      ? 'Infraction contrôle de l’environnement'
                      : controlTitle(infraction.controlType)}
                  </Title>
                </Stack.Item>
                {[1, 2].map((aa: any) => (
                  <Stack.Item>
                    <Tag accent={Accent.PRIMARY}>
                      <b>Avec PV</b>
                    </Tag>
                  </Stack.Item>
                ))}
              </Stack>
            </Stack.Item>
            <Stack.Item>
              <Stack direction="row" alignItems="baseline" spacing={'0.5rem'}>
                <Stack.Item>
                  <IconButton
                    Icon={Icon.EditUnbordered}
                    accent={Accent.SECONDARY}
                    size={Size.NORMAL}
                    role="edit-infraction"
                    onClick={() => onEditInfractionForTarget(infraction)}
                  />
                </Stack.Item>
                <Stack.Item>
                  <IconButton
                    Icon={Icon.Delete}
                    accent={Accent.SECONDARY}
                    size={Size.NORMAL}
                    onClick={() => onDeleteInfraction(infraction.id)}
                  />
                </Stack.Item>
              </Stack>
            </Stack.Item>
          </Stack>
        </Stack.Item>
      ))}
    </Stack>
  )
}

export default EnvInfractionSummary