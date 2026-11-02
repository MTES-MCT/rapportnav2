import { FormikSelect } from '@mtes-mct/monitor-ui'
import { FormikProps, useFormikContext } from 'formik'
import { FC, useEffect } from 'react'
import { Stack } from 'rsuite'
import { EstablishmentForm } from '../../../common/components/ui/establishment-form.tsx'
import { FormikSearchPort } from '../../../common/components/ui/formik-search-port.tsx'
import { FormikSelectFishAuction } from '../../../common/components/ui/formik-select-fish-auction.tsx'
import { useSector } from '../../../common/hooks/use-sector.tsx'
import { Establishment } from '../../../common/types/etablishment.ts'
import { MissionActionData } from '../../../common/types/mission-action-data.ts'
import { SatiParty } from '../../../common/types/sati.ts'
import { SectorFishingType } from '../../../common/types/sector-types.ts'
import { ActionControlInput, ActionNavControlInput } from '../../../mission-action/types/action-type.ts'
import { isFishAuction, isLandingSite } from '../../../mission-action/validation-schema/control-sector-fishing.ts'
import FishingSectorControlInfosOperator from './fishing-sector-control-infos-operator.tsx'
import FishingSectorControlInfosOwner from './fishing-sector-control-infos-owner.tsx'

type EstablishmentMode = 'establishment' | 'landing' | 'auction'

const FIELDS_TO_CLEAR_BY_MODE: Record<EstablishmentMode, (keyof ActionControlInput)[]> = {
  establishment: ['portLocode', 'fishAuction', 'zipCode', 'city'],
  landing: ['establishment', 'fishAuction'],
  auction: ['establishment', 'portLocode']
}

interface FishingSectorControlInfosProps {
  values: MissionActionData
  formik: FormikProps<ActionNavControlInput>
}

const FishingSectorControlInfos: FC<FishingSectorControlInfosProps> = ({ formik }) => {
  const { values, setFieldValue } = useFormikContext<ActionNavControlInput>()
  const { sectorFishingTypeOptions, sectorEstablishmenSubTypeOptions } = useSector()

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
      <Stack direction="column" spacing={'.8rem'}>
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
        {formik.values?.sectorEstablishmentType === SectorFishingType.GMS && (
          <Stack.Item style={{ width: '100%' }}>
            <FormikSelect
              isLight={true}
              isRequired={true}
              isErrorMessageHidden={true}
              label="Type de GMS"
              name="sati.establishment.subType"
              options={sectorEstablishmenSubTypeOptions}
            />
          </Stack.Item>
        )}

        <Stack.Item style={{ width: '100%' }}>
          {isAuction && <FormikSelectFishAuction name="fishAuction" label="Criée" isLight={true} />}
          {isLanding && <FormikSearchPort name="portLocode" isLight={true} label="Lieu de contrôle" />}
          {!isAuction && !isLanding && (
            <EstablishmentForm
              isLight={true}
              name="establishment"
              establishment={values?.establishment}
              onChange={(value?: Establishment) => setFieldValue('establishment', value)}
            />
          )}
        </Stack.Item>
        <Stack.Item style={{ width: '100%' }}>
          <FishingSectorControlInfosOwner
            owner={values?.sati?.establishment?.owner}
            onChange={(value?: SatiParty) => setFieldValue('sati.establishment.owner', value)}
          />
        </Stack.Item>
        <Stack.Item style={{ width: '100%' }}>
          <FishingSectorControlInfosOperator
            operator={values?.sati?.establishment?.operator}
            onDelete={() => setFieldValue('sati.establishment.operator', null)}
            onChange={(value: SatiParty) => setFieldValue('sati.establishment.operator', value)}
          />
        </Stack.Item>
      </Stack>
    </Stack.Item>
  )
}

export default FishingSectorControlInfos
