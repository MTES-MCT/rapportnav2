import { FormikCheckbox, FormikNumberInput, FormikSelect } from '@mtes-mct/monitor-ui'
import { FormikProps } from 'formik'
import { FC } from 'react'
import { Stack } from 'rsuite'
import { useMissionType } from '../../../common/hooks/use-mission-type.tsx'

const AdminGeneralInfosForm: FC<{ formik: FormikProps<any> }> = ({ formik }) => {
  const { reportTypeOptions, jdpTypeOptions, reinforcementTypeOptions } = useMissionType()
  return (
    <Stack direction="column" spacing={'1rem'} style={{ width: '100%' }}>
      <Stack.Item style={{ width: '100%' }}>
        <Stack direction="row" spacing={'1rem'}>
          <Stack.Item style={{ flex: 1 }}>
            <FormikNumberInput name="serviceId" label="Id du service" isErrorMessageHidden={true} />
          </Stack.Item>
          <Stack.Item style={{ flex: 1 }}>
            <FormikSelect
              name="missionReportType"
              label="Type de rapport"
              options={reportTypeOptions}
              isErrorMessageHidden={true}
            />
          </Stack.Item>
        </Stack>
      </Stack.Item>

      <Stack.Item style={{ width: '100%' }}>
        <Stack direction="row" spacing={'1rem'}>
          <Stack.Item style={{ flex: 1 }}>
            <FormikSelect
              name="reinforcementType"
              label="Type de renfort"
              options={reinforcementTypeOptions}
              isErrorMessageHidden={true}
            />
          </Stack.Item>
          <Stack.Item style={{ flex: 1 }}>
            <FormikSelect name="jdpType" label="Type JDP" options={jdpTypeOptions} isErrorMessageHidden={true} />
          </Stack.Item>
        </Stack>
      </Stack.Item>

      <Stack.Item style={{ width: '100%' }}>
        <Stack direction="row" spacing={'1rem'}>
          <Stack.Item style={{ flex: 1 }}>
            <FormikNumberInput
              name="distanceInNauticalMiles"
              label="Distance (milles nautiques)"
              isErrorMessageHidden={true}
            />
          </Stack.Item>
          <Stack.Item style={{ flex: 1 }}>
            <FormikNumberInput name="nbHourAtSea" label="Heures en mer" isErrorMessageHidden={true} />
          </Stack.Item>
        </Stack>
      </Stack.Item>

      <Stack.Item style={{ width: '100%' }}>
        <Stack direction="row" spacing={'1rem'}>
          <Stack.Item style={{ flex: 1 }}>
            <FormikNumberInput name="consumedGOInLiters" label="GO consommé (litres)" isErrorMessageHidden={true} />
          </Stack.Item>
          <Stack.Item style={{ flex: 1 }}>
            <FormikNumberInput name="consumedFuelInLiters" label="Fuel consommé (litres)" isErrorMessageHidden={true} />
          </Stack.Item>
        </Stack>
      </Stack.Item>

      <Stack.Item style={{ width: '100%' }}>
        <Stack direction="row" spacing={'1rem'}>
          <Stack.Item style={{ flex: 1 }}>
            <FormikNumberInput
              name="operatingCostsInEuro"
              label="Coûts opérationnels (€)"
              isErrorMessageHidden={true}
            />
          </Stack.Item>
          <Stack.Item style={{ flex: 1 }}>
            <FormikNumberInput name="fuelCostsInEuro" label="Coûts carburant (€)" isErrorMessageHidden={true} />
          </Stack.Item>
        </Stack>
      </Stack.Item>

      <Stack.Item style={{ width: '100%' }}>
        <Stack direction="row" spacing={'1rem'}>
          <Stack.Item style={{ flex: 1 }}>
            <FormikNumberInput
              name="nbrOfRecognizedVessel"
              label="Nombre de navires reconnus"
              isErrorMessageHidden={true}
            />
          </Stack.Item>
        </Stack>
      </Stack.Item>

      <Stack.Item style={{ width: '100%' }}>
        <Stack direction="row" spacing={'1rem'} alignItems="center">
          <Stack.Item>
            <FormikCheckbox name="isMissionArmed" label="Mission armée" isErrorMessageHidden={true} />
          </Stack.Item>
          <Stack.Item>
            <FormikCheckbox
              name="isWithInterMinisterialService"
              label="Avec service interministériel"
              isErrorMessageHidden={true}
            />
          </Stack.Item>
          <Stack.Item>
            <FormikCheckbox name="isResourcesNotUsed" label="Ressources non utilisées" isErrorMessageHidden={true} />
          </Stack.Item>
        </Stack>
      </Stack.Item>
    </Stack>
  )
}
export default AdminGeneralInfosForm
