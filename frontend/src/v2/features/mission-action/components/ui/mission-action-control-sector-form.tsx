import { FormikMultiRadio, FormikSelect } from '@mtes-mct/monitor-ui'
import { Field, FieldProps, FormikProps } from 'formik'
import { FC, useEffect, useRef } from 'react'
import { Stack } from 'rsuite'
import { FormikEstablishment } from '../../../common/components/ui/formik-establishment.tsx'
import { FormikSelectFishAuction } from '../../../common/components/ui/formik-select-fish-auction.tsx'
import { useSector } from '../../../common/hooks/use-sector.tsx'
import { Establishment } from '../../../common/types/etablishment.ts'
import { SectorFishingType, SectorType } from '../../../common/types/sector-types.ts'
import { ActionControlInput } from '../../types/action-type.ts'
import { FormikSearchPort } from '../../../common/components/ui/formik-search-port.tsx'

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

  const previousSectorType = useRef(formik.values.sectorType)

  // Reset all dependent fields when sectorType changes
  useEffect(() => {
    if (previousSectorType.current !== formik.values.sectorType) {
      previousSectorType.current = formik.values.sectorType
      formik.setFieldValue('sectorEstablishmentType', undefined)
      formik.setFieldValue('establishment', undefined)
      formik.setFieldValue('portLocode', undefined)
      formik.setFieldValue('fishAuction', undefined)
    }
  }, [formik.values.sectorType])

  // Reset location/establishment fields when sectorEstablishmentType changes
  useEffect(() => {
    if (shouldShowControlLocation) {
      if (formik.values.establishment) {
        formik.setFieldValue('establishment', undefined)
      }
    } else {
      if (formik.values.portLocode) {
        formik.setFieldValue('portLocode', undefined)
      }
      if (formik.values.fishAuction) {
        formik.setFieldValue('fishAuction', undefined)
      }
      if (formik.values.zipCode) {
        formik.setFieldValue('zipCode', undefined)
      }
      if (formik.values.city) {
        formik.setFieldValue('city', undefined)
      }
    }
    if (isLandingSite && formik.values.fishAuction) {
      formik.setFieldValue('fishAuction', undefined)
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
              <FormikSelectFishAuction name="fishAuction" label="Criée" isLight={true} />
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
