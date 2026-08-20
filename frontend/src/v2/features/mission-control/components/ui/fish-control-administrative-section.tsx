import Text from '@common/components/ui/text.tsx'
import { Label, THEME } from '@mtes-mct/monitor-ui'
import { isEmpty } from 'lodash'
import React from 'react'
import { Stack } from 'rsuite'
import { MissionFishActionData } from '../../../common/types/mission-action'
import { ConformityRow, ConformityTable } from './conformity-table.tsx'

interface FishControlAdministrativeSectionProps {
  action: MissionFishActionData
}

type ConformityField =
  | 'emitsVms'
  | 'emitsAis'
  | 'portEntranceAndLandingAuthorized'
  | 'logbookOpenedPriorToControl'
  | 'logbookMatchesActivity'
  | 'licencesMatchActivity'
  | 'europeanFishingLicenceValid'
  | 'stowagePlanPresent'
  | 'onboardWeighingPermit'
  | 'weighingCertificateAndSystemsValid'

const CONFORMITY_ROWS: ConformityRow<ConformityField>[] = [
  { field: 'emitsVms', label: 'Bonne émission VMS' },
  { field: 'emitsAis', label: 'Bonne émission AIS' },
  { field: 'portEntranceAndLandingAuthorized', label: 'Accès au port / autorisation de débarquement conformes' },
  { field: 'logbookOpenedPriorToControl', label: 'Journal de pêche ouvert avant le contrôle' },
  { field: 'logbookMatchesActivity', label: "Déclarations journal de pêche conformes à l'activité du navire" },
  {
    field: 'licencesMatchActivity',
    label: "Autorisations de pêche (AEP, ANP, licences locales) conformes à l'activité du navire"
  },
  { field: 'europeanFishingLicenceValid', label: 'Licence de pêche européenne valide' },
  { field: 'stowagePlanPresent', label: "Plan d'arrimage présent et conforme" },
  { field: 'onboardWeighingPermit', label: 'Autorisation pour la pesée à bord', dividerBefore: true },
  {
    hideIfNotYes: true,
    field: 'weighingCertificateAndSystemsValid',
    label: 'Certificat de pesée présent et systèmes de pesée à bord valides'
  }
]

const FishControlAdministrativeSection: React.FC<FishControlAdministrativeSectionProps> = ({ action }) => {
  return (
    <Stack direction="column" alignItems="flex-start" spacing={'0.2rem'} style={{ width: '100%' }}>
      <Stack.Item>
        <Label>Conformité du navire</Label>
      </Stack.Item>
      <Stack.Item
        style={{
          width: '100%',
          padding: '16px',
          backgroundColor: THEME.color.white,
          border: `1px solid ${THEME.color.lightGray}`
        }}
      >
        <ConformityTable rows={CONFORMITY_ROWS} values={action} />
      </Stack.Item>

      {!isEmpty(action?.licencesAndLogbookObservations) && (
        <Stack.Item style={{ backgroundColor: THEME.color.white, width: '100%', padding: '1rem' }}>
          <Stack direction="column" alignItems="flex-start" spacing={'0.5rem'}>
            <Stack.Item>
              <Label>Observations (hors infractions) sur les obligations déclaratives / autorisations</Label>
            </Stack.Item>
            <Stack.Item>
              <Text as="h3" weight="medium">
                {action?.licencesAndLogbookObservations}
              </Text>
            </Stack.Item>
          </Stack>
        </Stack.Item>
      )}
    </Stack>
  )
}

export default FishControlAdministrativeSection
