import Text from '@common/components/ui/text'
import { GearControl } from '@common/types/fish-mission-types.ts'
import { Accent, Button, Checkbox, Icon, Label, MultiRadio, Size, THEME } from '@mtes-mct/monitor-ui'
import React from 'react'
import { Stack } from 'rsuite'
import { usecontrolCheck } from '../../../common/hooks/use-control-check'
import { MissionFishActionData } from '../../../common/types/mission-action'
import MissionInfractionFishSummary from '../../../mission-infraction/components/elements/mission-infraction-fish-summary'

interface MissionControlFishEnginesSectionProps {
  action: MissionFishActionData
}

const MissionControlFishEnginesSection: React.FC<MissionControlFishEnginesSectionProps> = ({ action }) => {
  const { controlCheckRadioBooleanOptions } = usecontrolCheck()
  return (
    <Stack direction="column" alignItems="flex-start" spacing={'0.2rem'}>
      <Stack.Item>
        <Label>Engins à bord</Label>
      </Stack.Item>
      {action.gearOnboard?.map((gearControl: GearControl, i: number) => (
        <Stack.Item
          style={{ backgroundColor: THEME.color.white, width: '100%', padding: '1rem' }}
          key={`${gearControl.gearCode}${i}`}
        >
          <Stack direction="column" alignItems="flex-start" spacing={'1rem'}>
            <Stack.Item>
              <Text as="h3" weight="bold">
                {`${gearControl.gearCode} - ${gearControl.gearName}`}
              </Text>
            </Stack.Item>
            <Stack.Item>
              <MultiRadio
                isInline
                name="emitsAis"
                readOnly={true}
                label="Engin contrôlé"
                onChange={function noRefCheck() {}}
                options={controlCheckRadioBooleanOptions}
                value={gearControl?.gearWasControlled ?? undefined}
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
                    checked={gearControl.hasUncontrolledMesh}
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
    </Stack>
  )
}

export default MissionControlFishEnginesSection
