import Text from '@common/components/ui/text.tsx'
import { ControlCheck } from '@common/types/fish-mission-types.ts'
import { FC } from 'react'
import { Stack } from 'rsuite'
import { useCountry } from '../../../common/hooks/use-countries.tsx'
import { SatiVessel } from '../../../common/types/sati.ts'
import GangwayPresent from '../ui/gangway-present.tsx'
import JpeForm from '../ui/jpe-form.tsx'
import JpeSummary from '../ui/jpe-summary.tsx'
import LabeledValue from '../ui/labeled-value.tsx'

interface FishControlInfosProps {
  name?: string
  vessel?: SatiVessel
  gangwayPresent?: ControlCheck
}

const FishControlInfosBoat: FC<FishControlInfosProps> = ({ name, vessel, gangwayPresent }) => {
  const { getCountryFlag } = useCountry()

  return (
    <Stack direction="column" spacing="1rem" alignItems="flex-start" style={{ width: '100%' }}>
      <Stack.Item style={{ width: '100%', backgroundColor: 'white', padding: 16 }}>
        <Stack direction="row" justifyContent="flex-start" alignItems="flex-end" spacing=".5rem">
          <Stack.Item>
            <img src={getCountryFlag('FRA')} style={{ height: 18.2, width: 26 }} alt="" />
          </Stack.Item>
          <Stack.Item>
            <Text as="h3">{vessel?.name}</Text>
          </Stack.Item>
        </Stack>
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <Stack direction="row" spacing={'.8rem'} wrap={true}>
          <Stack.Item>
            <LabeledValue value={vessel?.immat} label="CFR" />
          </Stack.Item>
          <Stack.Item>
            <LabeledValue value={vessel?.extRef} label="Marq. ext." />
          </Stack.Item>
          <Stack.Item>
            <LabeledValue value={vessel?.ircs} label="Call Sign" />
          </Stack.Item>
          <Stack.Item>
            <LabeledValue value={vessel?.length} label="Longueur" />
          </Stack.Item>
          <Stack.Item>
            <LabeledValue value={vessel?.type} label="Type" />
          </Stack.Item>
          <Stack.Item>
            <LabeledValue value={vessel?.imo} label="OMI" />
          </Stack.Item>
        </Stack>
      </Stack.Item>
      <Stack.Item style={{ width: '100%', padding: 16, backgroundColor: 'white' }}>
        {!!vessel?.jpe?.pnoId ? <JpeSummary name={name} /> : <JpeForm name={name} />}
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <GangwayPresent withGangWay={gangwayPresent === ControlCheck.YES} />
      </Stack.Item>
    </Stack>
  )
}

export default FishControlInfosBoat
