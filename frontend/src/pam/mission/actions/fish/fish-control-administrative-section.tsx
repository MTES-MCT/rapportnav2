import React from 'react'
import { Accent, Button, Icon, Label, MultiRadio, Size, THEME } from '@mtes-mct/monitor-ui'
import { Stack } from 'rsuite'
import { controlCheckMultiRadioOptions } from '../action-control-fish'
import { FishAction } from '../../../../types/fish-mission-types'
import Text from '../../../../ui/text'
import FishInfractionSummary from "../../infractions/fish-infraction-summary.tsx";

interface FishControlAdministrativeSectionProps {
  action: FishAction
}

const FishControlAdministrativeSection: React.FC<FishControlAdministrativeSectionProps> = ({action}) => {
  return (
    <Stack direction="column" alignItems="flex-start" spacing={'0.2rem'}>
      <Stack.Item>
        <Label>Obligations déclaratives et autorisations de pêche</Label>
      </Stack.Item>
      <Stack.Item style={{backgroundColor: THEME.color.white, width: '100%', padding: '1rem'}}>
        <Stack direction="column" alignItems="flex-start" spacing={'1rem'}>
          <Stack.Item>
            <MultiRadio
              isReadOnly={true}
              isInline
              label="Bonne émission VMS"
              value={action?.emitsVms}
              name="emitsVms"
              onChange={function noRefCheck() {
              }}
              options={controlCheckMultiRadioOptions}
            />
          </Stack.Item>
          <Stack.Item>
            <MultiRadio
              isReadOnly={true}
              isInline
              value={action?.emitsAis}
              label="Bonne émission AIS"
              name="emitsAis"
              onChange={function noRefCheck() {
              }}
              options={controlCheckMultiRadioOptions}
            />
          </Stack.Item>
          <Stack.Item>
            <MultiRadio
              isReadOnly={true}
              isInline
              label="Déclarations journal de pêche conformes à l'activité du navire"
              value={action?.logbookMatchesActivity}
              name="logbookMatchesActivity"
              onChange={function noRefCheck() {
              }}
              options={controlCheckMultiRadioOptions}
            />
          </Stack.Item>
          <Stack.Item>
            <MultiRadio
              isReadOnly={true}
              isInline
              label="Autorisations de pêche conformes à l'activité du navire (zone, engins, espèces)"
              value={action?.licencesMatchActivity}
              name="licencesMatchActivity"
              onChange={function noRefCheck() {
              }}
              options={controlCheckMultiRadioOptions}
            />
          </Stack.Item>
          <Stack.Item>
            <Button accent={Accent.SECONDARY} size={Size.NORMAL} Icon={Icon.Plus} disabled={true}
                    isFullWidth={false}>
              Ajouter une infraction obligations déclaratives / autorisations
            </Button>
          </Stack.Item>
        </Stack>
      </Stack.Item>
      <Stack.Item style={{width: '100%'}}>
        <FishInfractionSummary title="Infraction obligations déclaratives et autorisations"
                               infractions={action.logbookInfractions}/>
      </Stack.Item>
      <Stack.Item style={{backgroundColor: THEME.color.white, width: '100%', padding: '1rem'}}>
        <Stack direction="column" alignItems="flex-start" spacing={'0.5rem'}>
          <Stack.Item>
            <Label>Observations (hors infractions) sur les obligations déclaratives / autorisations</Label>
          </Stack.Item>
          <Stack.Item>
            <Text as="h3" weight="medium">
              {!!action?.licencesAndLogbookObservations ? action.licencesAndLogbookObservations : 'Aucune observation'}
            </Text>
          </Stack.Item>
        </Stack>
      </Stack.Item>
    </Stack>
  )
}

export default FishControlAdministrativeSection
