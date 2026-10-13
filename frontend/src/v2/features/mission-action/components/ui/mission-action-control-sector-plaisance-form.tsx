import { FormikSelect } from '@mtes-mct/monitor-ui'
import { Field, FieldProps, FormikProps } from 'formik'
import { FC } from 'react'
import { Stack } from 'rsuite'
import { FormikEstablishment } from '../../../common/components/ui/formik-establishment.tsx'
import { useSector } from '../../../common/hooks/use-sector.tsx'
import { Establishment } from '../../../common/types/etablishment.ts'
import { ActionControlInput } from '../../types/action-type.ts'

const MissionActionItemSectorPlaisanceControlForm: FC<{ formik: FormikProps<ActionControlInput> }> = () => {
  const { sectorPleasureTypeOptions } = useSector()

  return (
    <Stack.Item>
      <Stack direction="column" spacing={'.5rem'}>
        <Stack.Item style={{ width: '100%' }}>
          <FormikSelect
            name="sectorEstablishmentType"
            label="Type d'établissement"
            isLight={true}
            isRequired={true}
            isErrorMessageHidden={true}
            options={sectorPleasureTypeOptions}
          />
        </Stack.Item>
        <Stack.Item style={{ width: '100%' }}>
          <Field name="establishment">
            {(field: FieldProps<Establishment>) => (
              <FormikEstablishment name="establishment" isLight={true} fieldFormik={field} />
            )}
          </Field>
        </Stack.Item>
      </Stack>
    </Stack.Item>
  )
}
export default MissionActionItemSectorPlaisanceControlForm
