import { FormikSelect } from '@mtes-mct/monitor-ui'
import { Field, FieldProps, FormikProps } from 'formik'
import { FC, useEffect } from 'react'
import { Stack } from 'rsuite'
import { FormikEstablishment } from '../../../common/components/ui/formik-establishment.tsx'
import { FormikSearchPort } from '../../../common/components/ui/formik-search-port.tsx'
import { FormikSelectFishAuction } from '../../../common/components/ui/formik-select-fish-auction.tsx'
import { useSector } from '../../../common/hooks/use-sector.tsx'
import { Establishment } from '../../../common/types/etablishment.ts'
import { ActionControlInput } from '../../types/action-type.ts'
import { isFishAuction, isLandingSite } from '../../validation-schema/control-sector-fishing.ts'

type EstablishmentMode = 'establishment' | 'landing' | 'auction'

const FIELDS_TO_CLEAR_BY_MODE: Record<EstablishmentMode, (keyof ActionControlInput)[]> = {
  establishment: ['portLocode', 'fishAuction', 'zipCode', 'city'],
  landing: ['establishment', 'fishAuction'],
  auction: ['establishment', 'portLocode']
}

const MissionActionItemSectorFishingControlForm: FC<{ formik: FormikProps<ActionControlInput> }> = ({ formik }) => {
  const { sectorFishingTypeOptions } = useSector()

  const isLanding = isLandingSite(formik.values.sectorEstablishmentType)
  const isAuction = isFishAuction(formik.values.sectorEstablishmentType)
  const mode: EstablishmentMode = isLanding ? 'landing' : isAuction ? 'auction' : 'establishment'

  useEffect(() => {
    FIELDS_TO_CLEAR_BY_MODE[mode].forEach(field => {
      if (formik.values[field]) formik.setFieldValue(field, undefined)
    })
  }, [mode])

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
            options={sectorFishingTypeOptions}
          />
        </Stack.Item>
        <Stack.Item style={{ width: '100%' }}>
          {isAuction && <FormikSelectFishAuction name="fishAuction" label="Criée" isLight={true} />}
          {isLanding && <FormikSearchPort name="portLocode" isLight={true} label="Lieu de contrôle" />}
          {!isAuction && !isLanding && (
            <Field name="establishment">
              {(field: FieldProps<Establishment>) => (
                <FormikEstablishment name="establishment" isLight={true} fieldFormik={field} />
              )}
            </Field>
          )}
        </Stack.Item>
      </Stack>
    </Stack.Item>
  )
}
export default MissionActionItemSectorFishingControlForm
