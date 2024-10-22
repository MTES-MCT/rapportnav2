import Text from '@common/components/ui/text.tsx'
import { FishAction } from '@common/types/fish-mission-types.ts'
import { controlCheckMultiRadioOptions } from '@features/pam/mission/components/elements/actions/action-control-fish'
import { Accent, Button, Icon, Label, MultiRadio, Size, THEME } from '@mtes-mct/monitor-ui'
import React from 'react'
import { Stack } from 'rsuite'

interface MissionControlFishAdministrativeSectionProps {
  action: FishAction
}

const MissionControlFishAdministrativeSection: React.FC<MissionControlFishAdministrativeSectionProps> = ({
  action
}) => {
  return (
    <Stack direction="column" alignItems="flex-start" spacing={'0.2rem'}>
      <Stack.Item>
        <Label>Obligations déclaratives et autorisations de pêche</Label>
      </Stack.Item>
      <Stack.Item style={{ backgroundColor: THEME.color.white, width: '100%', padding: '1rem' }}>
        <Stack direction="column" alignItems="flex-start" spacing={'1rem'}>
          <Stack.Item>
            <MultiRadio
              readOnly={true}
              isInline
              label="Bonne émission VMS"
              value={action?.emitsVms ?? undefined}
              name="emitsVms"
              onChange={function noRefCheck() {}}
              options={controlCheckMultiRadioOptions}
            />
          </Stack.Item>
          <Stack.Item>
            <MultiRadio
              readOnly={true}
              isInline
              value={action?.emitsAis ?? undefined}
              label="Bonne émission AIS"
              name="emitsAis"
              onChange={function noRefCheck() {}}
              options={controlCheckMultiRadioOptions}
            />
          </Stack.Item>
          <Stack.Item>
            <MultiRadio
              readOnly={true}
              isInline
              label="Déclarations journal de pêche conformes à l'activité du navire"
              value={action?.logbookMatchesActivity ?? undefined}
              name="logbookMatchesActivity"
              onChange={function noRefCheck() {}}
              options={controlCheckMultiRadioOptions}
            />
          </Stack.Item>
          <Stack.Item>
            <MultiRadio
              isInline
              readOnly={true}
              name="licencesMatchActivity"
              onChange={function noRefCheck() {}}
              options={controlCheckMultiRadioOptions}
              value={action?.licencesMatchActivity ?? undefined}
              label="Autorisations de pêche conformes à l'activité du navire (zone, engins, espèces)"
            />
          </Stack.Item>
          <Stack.Item>
            <Button accent={Accent.SECONDARY} size={Size.NORMAL} Icon={Icon.Plus} disabled={true} isFullWidth={false}>
              Ajouter une infraction obligations déclaratives / autorisations
            </Button>
          </Stack.Item>
        </Stack>
      </Stack.Item>
      INFRACTION!!!
      <Stack.Item style={{ backgroundColor: THEME.color.white, width: '100%', padding: '1rem' }}>
        <Stack direction="column" alignItems="flex-start" spacing={'0.5rem'}>
          <Stack.Item>
            <Label>Observations (hors infractions) sur les obligations déclaratives / autorisations</Label>
          </Stack.Item>
          <Stack.Item>
            <Text as="h3" weight="medium">
              {action.licencesAndLogbookObservations ?? 'Aucune observation'}
            </Text>
          </Stack.Item>
        </Stack>
      </Stack.Item>
    </Stack>
  )
}

export default MissionControlFishAdministrativeSection

/**
 * 
 *  <Stack.Item style={{ width: '100%' }}>
        <FishInfractionSummary
          title="Infraction obligations déclaratives et autorisations"
          infractions={action.logbookInfractions}
        />
      </Stack.Item>
 */
