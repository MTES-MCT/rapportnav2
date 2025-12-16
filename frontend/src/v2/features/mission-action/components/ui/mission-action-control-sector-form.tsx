import { FormikMultiRadio, FormikSelect } from '@mtes-mct/monitor-ui'
import { Field, FieldProps, FormikProps } from 'formik'
import { FC } from 'react'
import { Stack } from 'rsuite'
import { Establishment } from 'src/v2/features/common/types/etablishment.ts'
import { FormikEstablishment } from '../../../common/components/ui/formik-establishment.tsx'
import { useSector } from '../../../common/hooks/use-sector.tsx'
import { ActionControlInput } from '../../types/action-type.ts'

const MissionActionItemSectorControlForm: FC<{ formik: FormikProps<ActionControlInput> }> = ({ formik }) => {
  const { sectorTypeOptions, getSectionEtablishmentTypeOptions } = useSector()

  return (
    <Stack.Item>
      <Stack direction="column" spacing={'.5rem'}>
        <Stack.Item style={{ width: '100%' }}>
          <Stack direction="column" spacing={'1rem'} alignItems="flex-start">
            <Stack.Item style={{ width: '100%' }}>
              <FormikMultiRadio label="" name="sectorType" options={sectorTypeOptions} isErrorMessageHidden={true} />
            </Stack.Item>
            <Stack.Item style={{ width: '70%' }}>
              <FormikSelect
                name="sectorEstablishmentType"
                label="Type d'établissement"
                isLight={true}
                isRequired={true}
                isErrorMessageHidden={true}
                options={getSectionEtablishmentTypeOptions(formik.values.sectorType)}
              />
            </Stack.Item>
          </Stack>
        </Stack.Item>
        <Stack.Item style={{ width: '100%' }}>
          <Stack.Item style={{ width: '100%' }}>
            <Field name="establishment">
              {(field: FieldProps<Establishment>) => (
                <FormikEstablishment name="establishment" isLight={true} fieldFormik={field} />
              )}
            </Field>
          </Stack.Item>
        </Stack.Item>
      </Stack>
    </Stack.Item>
  )
}
export default MissionActionItemSectorControlForm
