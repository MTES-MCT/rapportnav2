import { useFormikContext } from 'formik'
import { FC } from 'react'
import { Stack } from 'rsuite'
import { SatiModuleType, SatiParty, SatiVessel } from '../../../common/types/sati.ts'
import { ActionFishControlInput } from '../../../mission-action/types/action-type.ts'
import FishControlInfosAgent from './fish-control-infos-agent.tsx'
import FishControlInfosBeneficiary from './fish-control-infos-beneficiary.tsx'
import FishControlInfosBoat from './fish-control-infos-boat.tsx'
import FishControlInfosMaster from './fish-control-infos-master.tsx'
import FishControlInfosOperator from './fish-control-infos-operator.tsx'
import FishControlInfosOwner from './fish-control-infos-owner.tsx'

interface FishControlInfosProps {}

const FishControlInfos: FC<FishControlInfosProps> = () => {
  const { values, setFieldValue } = useFormikContext<ActionFishControlInput>()
  return (
    <div style={{ width: '100%' }}>
      <Stack
        direction="column"
        justifyContent="flex-start"
        alignItems="flex-start"
        spacing={'1.5rem'}
        style={{ marginBottom: '2rem' }}
      >
        <Stack.Item style={{ width: '100%' }}>
          <FishControlInfosBoat name="sati.vessel" vessel={values?.sati?.vessel} />
        </Stack.Item>
        <Stack.Item style={{ width: '100%' }}>
          <FishControlInfosOwner name="sati.vessel.owner" owner={values?.sati?.vessel?.owner} />
        </Stack.Item>
        {values?.sati?.vessel?.operator?.contact?.fullName && (
          <Stack.Item style={{ width: '100%' }}>
            <FishControlInfosOperator operator={values?.sati?.vessel?.operator} />
          </Stack.Item>
        )}
        <Stack.Item style={{ width: '100%' }}>
          <FishControlInfosMaster
            vessel={values?.sati?.vessel}
            onchange={(value?: SatiVessel) => setFieldValue('sati.vessel', value)}
          />
        </Stack.Item>
        <Stack.Item style={{ width: '100%' }}>
          <FishControlInfosAgent
            agent={values?.sati?.vessel?.agent}
            onDelete={() => setFieldValue('sati.vessel.agent', null)}
            onChange={(value: SatiParty) => setFieldValue('sati.vessel.agent', value)}
          />
        </Stack.Item>

        {values?.sati?.module === SatiModuleType.M3 && (
          <Stack.Item style={{ width: '100%' }}>
            <FishControlInfosBeneficiary
              beneficiary={values?.sati?.vessel?.beneficiary}
              onDelete={() => setFieldValue('sati.vessel.beneficiary', null)}
              onChange={(value: SatiParty) => setFieldValue('sati.vessel.beneficiary', value)}
            />
          </Stack.Item>
        )}
      </Stack>
    </div>
  )
}

export default FishControlInfos
