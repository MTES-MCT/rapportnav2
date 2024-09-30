import { FishAction, SpeciesControl } from '@common/types/fish-mission-types.ts'
import { Accent, Button, Checkbox, Icon, Label, MultiRadio, Size, THEME } from '@mtes-mct/monitor-ui'
import React from 'react'
import { Stack } from 'rsuite'
import Text from '../../../../../../common/components/ui/text.tsx'
import FishInfractionSummary from '../../infractions/fish-infraction-summary.tsx'
import { BOOLEAN_AS_OPTIONS, controlCheckMultiRadioOptions } from '../action-control-fish.tsx'

interface FishControlSpeciesSectionProps {
  action: FishAction
}

const FishControlSpeciesSection: React.FC<FishControlSpeciesSectionProps> = ({ action }) => {
  return (
    <Stack direction="column" alignItems="flex-start" spacing={'0.2rem'}>
      <Stack.Item>
        <Label>Espèces à bord</Label>
      </Stack.Item>

      <Stack.Item style={{ backgroundColor: THEME.color.white, width: '100%', padding: '1rem' }}>
        <Stack direction="column" alignItems="flex-start" spacing={'1rem'}>
          <Stack.Item>
            <MultiRadio
              isInline
              readOnly={true}
              options={BOOLEAN_AS_OPTIONS}
              name="speciesWeightControlled"
              label="Poids des espèces vérifié"
              onChange={function noRefCheck() {}}
              value={action?.speciesWeightControlled ?? undefined}
            />
          </Stack.Item>
          <Stack.Item>
            <MultiRadio
              isInline
              readOnly={true}
              options={BOOLEAN_AS_OPTIONS}
              name="speciesSizeControlled"
              label="Taille des espèces vérifiée"
              onChange={function noRefCheck() {}}
              value={action?.speciesSizeControlled ?? undefined}
            />
          </Stack.Item>
          <Stack.Item>
            <MultiRadio
              isInline
              readOnly={true}
              onChange={function noRefCheck() {}}
              options={controlCheckMultiRadioOptions}
              name="separateStowageOfPreservedSpecies"
              label="Arrimage séparé des espèces soumises à plan"
              value={action?.separateStowageOfPreservedSpecies ?? undefined}
            />
          </Stack.Item>
          {action.speciesOnboard?.map((species: SpeciesControl) => (
            <Stack.Item key={species.speciesCode}>
              <Stack direction="column" alignItems="flex-start" spacing={'0.25rem'}>
                <Stack.Item>
                  <Text as="h3" weight="medium">
                    {species.speciesCode}
                  </Text>
                </Stack.Item>
                <Stack.Item>
                  <Stack direction="row" alignItems="flex-start" spacing={'1rem'}>
                    <Stack.Item>
                      <Label>Qté déclarée</Label>
                      <Text as="h3" weight="medium">
                        {`${species.declaredWeight || '--'} kg`}
                      </Text>
                    </Stack.Item>
                    <Stack.Item>
                      <Label>Qté estimée</Label>
                      <Text as="h3" weight="medium">
                        {`${species.controlledWeight || '--'} kg`}
                      </Text>
                    </Stack.Item>
                    <Stack.Item>
                      <Label>&nbsp;</Label>
                      <Checkbox name="underSized" label="Sous-taille" readOnly={true} checked={!!species.underSized} />
                    </Stack.Item>
                  </Stack>
                </Stack.Item>
              </Stack>
            </Stack.Item>
          ))}
          <Stack.Item style={{ backgroundColor: THEME.color.white, width: '100%' }}>
            <Button accent={Accent.SECONDARY} size={Size.NORMAL} Icon={Icon.Plus} disabled={true} isFullWidth={false}>
              Ajouter une infraction espèces
            </Button>
          </Stack.Item>
        </Stack>
      </Stack.Item>

      <Stack.Item style={{ width: '100%' }}>
        <FishInfractionSummary title="Infraction espèces" infractions={action.speciesInfractions} />
      </Stack.Item>

      <Stack.Item style={{ backgroundColor: THEME.color.white, width: '100%', padding: '1rem' }}>
        <Label>Observations (hors infraction) sur les espèces</Label>
        <Text as="h3" weight="medium">
          {!!action?.speciesObservations ? action.speciesObservations : 'Aucune observation'}
        </Text>
      </Stack.Item>
    </Stack>
  )
}

export default FishControlSpeciesSection
