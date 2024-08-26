import React from 'react'
import { Accent, Button, Checkbox, Icon, Label, MultiRadio, Size, THEME } from '@mtes-mct/monitor-ui'
import { FishAction, GearControl } from '../../../../../../common/types/fish-mission-types.ts'
import Text from '../../../../../../common/components/ui/text.tsx'
import { Stack } from 'rsuite'
import { BOOLEAN_AS_OPTIONS } from '../action-control-fish.tsx'
import FishInfractionSummary from '../../infractions/fish-infraction-summary.tsx'

interface FishControlEnginesSectionProps {
  action: FishAction
}

const FishControlEnginesSection: React.FC<FishControlEnginesSectionProps> = ({ action }) => {
  return (
    <Stack direction="column" alignItems="flex-start" spacing={'0.2rem'}>
      <Stack.Item>
        <Label>Engins à bord</Label>
      </Stack.Item>
      {action.gearOnboard?.map((gearControl: GearControl) => (
        <Stack.Item style={{ backgroundColor: THEME.color.white, width: '100%', padding: '1rem' }}>
          <Stack direction="column" alignItems="flex-start" spacing={'1rem'}>
            <Stack.Item>
              <Text as="h3" weight="bold">
                {`${gearControl.gearCode} - ${gearControl.gearName}`}
              </Text>
            </Stack.Item>
            <Stack.Item>
              <MultiRadio
                isReadOnly={true}
                isInline
                value={gearControl?.gearWasControlled ?? undefined}
                label="Engin contrôlé"
                name="emitsAis"
                onChange={function noRefCheck() {}}
                options={BOOLEAN_AS_OPTIONS}
              />
            </Stack.Item>
            <Stack.Item>
              <Stack direction="row" alignItems="flex-start" spacing={'1rem'}>
                <Stack.Item>
                  <Label>Maillage déclaré</Label>
                  <Text as="h3" weight="medium">
                    {gearControl.declaredMesh} mm
                  </Text>
                </Stack.Item>
                <Stack.Item>
                  <Label>Maillage déclaré</Label>
                  <Text as="h3" weight="medium">
                    {gearControl.controlledMesh} mm
                  </Text>
                </Stack.Item>
                <Stack.Item>
                  <Label>&nbsp;</Label>
                  <Checkbox
                    readOnly={true}
                    name="hasUncontrolledMesh"
                    label="Maillage non mesuré"
                    checked={!!gearControl.hasUncontrolledMesh}
                  />
                </Stack.Item>
              </Stack>
            </Stack.Item>
            <Stack.Item>
              <Label>Observations (hors infractions) sur les obligations déclaratives / autorisations</Label>
              <Text as="h3" weight="medium">
                {!!gearControl?.comments ? gearControl.comments : 'Aucune observation'}
              </Text>
            </Stack.Item>
            <Stack.Item>
              <Button accent={Accent.SECONDARY} size={Size.NORMAL} Icon={Icon.Plus} disabled={true} isFullWidth={false}>
                Ajouter une infraction engins
              </Button>
            </Stack.Item>
          </Stack>
        </Stack.Item>
      ))}

      <Stack.Item style={{ width: '100%' }}>
        <Stack direction="column" alignItems="flex-start" spacing={'0.2rem'}>
          <Stack.Item style={{ width: '100%' }}>
            <FishInfractionSummary title="Infraction engins" infractions={action.gearInfractions} />
          </Stack.Item>
        </Stack>
      </Stack.Item>
    </Stack>
  )
}

export default FishControlEnginesSection
