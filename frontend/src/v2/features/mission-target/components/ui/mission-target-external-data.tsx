import Text from '@common/components/ui/text'
import { formalNoticeLabels, infractionTypeLabels, transformFormatToOptions } from '@common/types/env-mission-types.ts'
import { Checkbox, Legend, MultiRadio, THEME } from '@mtes-mct/monitor-ui'
import React from 'react'
import { Stack } from 'rsuite'
import MissionNatinfs from '../../../common/components/elements/mission-natinfs'
import { useVessel } from '../../../common/hooks/use-vessel'
import { TargetExternalData } from '../../../common/types/target-types'
import MissionTargetEnvSummary from './mission-target-env-summary'

interface MissionTargetExternalDataProps {
  showDetail?: boolean
  externalData: TargetExternalData
}

const MissionTargetExternalData: React.FC<MissionTargetExternalDataProps> = ({ showDetail, externalData }) => {
  const { getVesselTypeName, getVesselSize } = useVessel()

  return (
    <Stack
      direction="column"
      style={{
        width: '100%',
        paddingTop: '.5em',
        paddingBottom: '.5em',
        backgroundColor: THEME.color.white
      }}
      alignItems="flex-start"
      data-testid="mission-target-external-data"
    >
      <Stack.Item style={{ width: '100%' }}>
        {!showDetail && (
          <MissionTargetEnvSummary infractionType={externalData?.infractionType} natinfs={externalData?.natinfs} />
        )}
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        {showDetail && (
          <Stack
            direction="column"
            spacing={'2rem'}
            style={{
              width: '100%',
              padding: '1rem'
            }}
          >
            <Stack.Item style={{ width: '100%' }}>
              <Stack direction="row" spacing={'2rem'} style={{ width: '100%' }}>
                <Stack.Item style={{ width: '100%' }}>
                  {/*TODO hide for personne physique*/}
                  <Legend>Immatriculation</Legend>
                  <Text as="h3" weight="medium">
                    {externalData?.registrationNumber ?? '--'}
                  </Text>
                </Stack.Item>
                {/*TODO hide for personne physique*/}
                <Stack.Item style={{ width: '100%' }}>
                  <Legend>Type de navire</Legend>
                  <Text as="h3" weight="medium">
                    {getVesselTypeName(externalData?.vesselType) ?? '--'}
                  </Text>
                </Stack.Item>
                {/*TODO hide for personne physique*/}
                <Stack.Item style={{ width: '100%' }}>
                  <Legend>Taille du navire</Legend>
                  <Text as="h3" weight="medium">
                    {getVesselSize(externalData?.vesselSize) ?? '--'}
                  </Text>
                </Stack.Item>
              </Stack>
            </Stack.Item>
            <Stack.Item style={{ width: '100%' }}>
              <Legend>Identité de la personne contrôlée</Legend>
              <Text as="h3" weight="medium">
                {externalData?.controlledPersonIdentity ?? '--'}
              </Text>
            </Stack.Item>
            <Stack.Item style={{ width: '100%' }}>
              <MultiRadio
                isInline
                readOnly={true}
                name="infractionType"
                label="Type d'infraction"
                value={externalData.infractionType ?? undefined}
                options={transformFormatToOptions(infractionTypeLabels)}
              />
            </Stack.Item>
            <Stack.Item style={{ width: '100%' }}>
              <MultiRadio
                isInline
                readOnly={true}
                value={externalData?.formalNotice ?? undefined}
                label="Mise en demeure"
                name="formalNotice"
                options={transformFormatToOptions(formalNoticeLabels)}
              />
            </Stack.Item>
            <Stack.Item style={{ width: '100%' }}>
              <Legend>NATINF</Legend>
              <MissionNatinfs natinfs={externalData?.natinfs} />
            </Stack.Item>
            <Stack.Item style={{ width: '100%' }}>
              <Stack direction="row" spacing={'2rem'}>
                <Stack.Item>
                  <Legend>Tribunal compétent</Legend>
                  <Text as="h3" weight="medium" data-testid={'relevantCourt'}>
                    {externalData?.relevantCourt || '--'}
                  </Text>
                </Stack.Item>
                <Stack.Item>
                  <Legend>&nbsp;</Legend>
                  <Checkbox name="toProcess" checked={!!externalData?.toProcess} label="À traiter" />
                </Stack.Item>
              </Stack>
            </Stack.Item>
            <Stack.Item style={{ width: '100%' }}>
              <Legend>Observations</Legend>
              <Text as="h3" weight="medium" data-testid={'observations'}>
                {externalData?.observations && externalData?.observations !== '' ? externalData?.observations : '--'}
              </Text>
            </Stack.Item>
          </Stack>
        )}
      </Stack.Item>
    </Stack>
  )
}

export default MissionTargetExternalData
