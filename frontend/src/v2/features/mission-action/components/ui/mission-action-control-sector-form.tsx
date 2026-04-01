import { FormikMultiRadio, FormikSelect } from '@mtes-mct/monitor-ui'
import { Field, FieldProps, FormikProps } from 'formik'
import { FC, useEffect } from 'react'
import { Stack } from 'rsuite'
import { FormikEstablishment } from '../../../common/components/ui/formik-establishment.tsx'
import { useSector } from '../../../common/hooks/use-sector.tsx'
import { Establishment } from '../../../common/types/etablishment.ts'
import { SectorFishingType, SectorType } from '../../../common/types/sector-types.ts'
import { ActionControlInput } from '../../types/action-type.ts'
import { FormikSearchPort } from '../../../common/components/ui/formik-search-port.tsx'
import { SearchCity } from '../../../common/components/ui/search-city.tsx'

const MissionActionItemSectorControlForm: FC<{ formik: FormikProps<ActionControlInput> }> = ({ formik }) => {
  const { sectorTypeOptions, getSectionEtablishmentTypeOptions } = useSector()

  const shouldShowControlLocation =
    formik.values.sectorType === SectorType.FISHING &&
    (formik.values.sectorEstablishmentType === SectorFishingType.LANDING_SITE ||
      formik.values.sectorEstablishmentType === SectorFishingType.FISH_AUCTION)

  const isLandingSite =
    formik.values.sectorType === SectorType.FISHING &&
    formik.values.sectorEstablishmentType === SectorFishingType.LANDING_SITE

  const isFishAuction =
    formik.values.sectorType === SectorType.FISHING &&
    formik.values.sectorEstablishmentType === SectorFishingType.FISH_AUCTION

  useEffect(() => {
    if (shouldShowControlLocation) {
      if (formik.values.establishment) {
        formik.setFieldValue('establishment', undefined)
      }
    } else {
      if (formik.values.zipCode) {
        formik.setFieldValue('zipCode', undefined)
      }
      if (formik.values.city) {
        formik.setFieldValue('city', undefined)
      }
      if (formik.values.portLocode) {
        formik.setFieldValue('portLocode', undefined)
      }
    }
    if (isLandingSite) {
      if (formik.values.zipCode) formik.setFieldValue('zipCode', undefined)
      if (formik.values.city) formik.setFieldValue('city', undefined)
    }
    if (isFishAuction && formik.values.portLocode) {
      formik.setFieldValue('portLocode', undefined)
    }
  }, [shouldShowControlLocation, isLandingSite, isFishAuction])

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
            {formik.values.sectorEstablishmentType === SectorFishingType.FISH_AUCTION ? (
              <Field name="zipCode">
                {(field: FieldProps<string>) => (
                  <SearchCity
                    name="zipCode"
                    label="Lieu de contrôle"
                    isLight={true}
                    value={{ zipCode: formik.values.zipCode, city: formik.values.city }}
                    onChange={val => {
                      formik.setFieldValue('zipCode', val?.zipCode)
                      formik.setFieldValue('city', val?.city)
                    }}
                    fieldFormik={field}
                  />
                )}
              </Field>
            ) : formik.values.sectorEstablishmentType === SectorFishingType.LANDING_SITE ? (
              <Field name="portLocode">
                {(field: FieldProps<string>) => (
                  <FormikSearchPort name="portLocode" isLight={true} label="Lieu de contrôle" fieldFormik={field} />
                )}
              </Field>
            ) : (
              <Field name="establishment">
                {(field: FieldProps<Establishment>) => (
                  <FormikEstablishment name="establishment" isLight={true} fieldFormik={field} />
                )}
              </Field>
            )}
          </Stack.Item>
        </Stack.Item>
      </Stack>
    </Stack.Item>
  )
}
export default MissionActionItemSectorControlForm
