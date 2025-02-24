import Text from '@common/components/ui/text'
import { formalNoticeLabels, infractionTypeLabels, transformFormatToOptions } from '@common/types/env-mission-types.ts'
import { Infraction } from '@common/types/infraction-types.ts'
import { Checkbox, Legend, MultiRadio, THEME } from '@mtes-mct/monitor-ui'
import React from 'react'
import { Stack } from 'rsuite'
import MissionNatinfFullNameList from '../../../common/components/elements/mission-natinf-fullname-list'
import { useVessel } from '../../../common/hooks/use-vessel'

interface MissionInfractionEnvSummaryByCacemFormProps {
  infraction?: Infraction
}

const MissionInfractionEnvSummaryByCacemForm: React.FC<MissionInfractionEnvSummaryByCacemFormProps> = ({
  infraction
}) => {
  const { getVesselTypeName, getVesselSize } = useVessel()
  return (
    <Stack
      direction="column"
      spacing={'2rem'}
      style={{
        width: '100%',
        padding: '1rem',
        backgroundColor: THEME.color.white,
        marginBottom: '10px'
      }}
    >
      <Stack.Item style={{ width: '100%' }}>
        <Stack direction="row" spacing={'2rem'} style={{ width: '100%' }}>
          <Stack.Item style={{ width: '100%' }}>
            <Legend>Immatriculation</Legend>
            <Text as="h3" weight="medium">
              {infraction?.target?.vesselIdentifier}
            </Text>
          </Stack.Item>
          <Stack.Item style={{ width: '100%' }}>
            <Legend>Type de navire</Legend>
            <Text as="h3" weight="medium">
              {getVesselTypeName(infraction?.target?.vesselType)}
            </Text>
          </Stack.Item>
          <Stack.Item style={{ width: '100%' }}>
            <Legend>Taille du navire</Legend>
            <Text as="h3" weight="medium">
              {getVesselSize(infraction?.target?.vesselSize)}
            </Text>
          </Stack.Item>
        </Stack>
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <Legend>Identité de la personne contrôlée</Legend>
        <Text as="h3" weight="medium">
          {infraction?.target?.identityControlledPerson}
        </Text>
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <MultiRadio
          isInline
          readOnly={true}
          name="infractionType"
          label="Type d'infraction"
          value={infraction?.target?.infractionType ?? undefined}
          options={transformFormatToOptions(infractionTypeLabels)}
        />
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <MultiRadio
          isInline
          readOnly={true}
          value={infraction?.target?.formalNotice ?? undefined}
          label="Mise en demeure"
          name="formalNotice"
          options={transformFormatToOptions(formalNoticeLabels)}
        />
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <Legend>NATINF</Legend>
        <MissionNatinfFullNameList natinfs={infraction?.natinfs} />
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <Stack direction="row" spacing={'2rem'}>
          <Stack.Item>
            <Legend>Tribunal compétent</Legend>
            <Text as="h3" weight="medium" data-testid={'relevantCourt'}>
              {infraction?.target?.relevantCourt || '--'}
            </Text>
          </Stack.Item>
          <Stack.Item>
            <Legend>&nbsp;</Legend>
            <Checkbox name="toProcess" checked={!!infraction?.target?.toProcess} label="À traiter" />
          </Stack.Item>
        </Stack>
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <Legend>Observations</Legend>
        <Text as="h3" weight="medium" data-testid={'observations'}>
          {infraction?.target?.observations ?? '--'}
        </Text>
      </Stack.Item>
    </Stack>
  )
}

export default MissionInfractionEnvSummaryByCacemForm
