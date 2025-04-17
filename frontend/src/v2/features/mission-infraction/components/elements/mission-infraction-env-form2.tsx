import Text from '@common/components/ui/text'
import { ControlType } from '@common/types/control-types'
import { VehicleTypeEnum } from '@common/types/env-mission-types'
import {
  Accent,
  Button,
  FormikSelect,
  FormikTextarea,
  FormikTextInput,
  FormikToggle,
  Size,
  THEME
} from '@mtes-mct/monitor-ui'
import { Formik } from 'formik'
import { isEmpty } from 'lodash'
import { FC } from 'react'
import { Stack } from 'rsuite'
import { setDebounceTime } from '../../../../store/slices/delay-query-reducer'
import { FormikMultiSelectNatinf } from '../../../common/components/ui/formik-multi-select-natinf'
import { TargetType } from '../../../common/types/target-types'
import { useControlRegistry } from '../../../mission-control/hooks/use-control-registry'
import { TargetInfraction, useInfractionEnvForm2 } from '../../hooks/use-infraction-env-form2'
import { MissionInfractionFormikControlledPersonInput } from '../ui/mission-infraction-formik-controlled-person-input'
import MissionInfractionVesselForm from '../ui/mission-infraction-vessel-form'

export interface MissionInfractionEnvForm2Props {
  onClose: () => void
  withTarget?: boolean
  value: TargetInfraction
  vehicleType?: VehicleTypeEnum
  availableControlTypes?: ControlType[]
  targetType?: TargetType
  onSubmit: (value?: TargetInfraction) => Promise<unknown>
}

const MissionInfractionEnvForm2: FC<MissionInfractionEnvForm2Props> = ({
  value,
  onClose,
  onSubmit,
  withTarget,
  targetType,
  vehicleType,
  availableControlTypes
}) => {
  const { controlTypeOptions, getDisabledControlTypes } = useControlRegistry()
  const { initValue, handleSubmit, validationSchema } = useInfractionEnvForm2(
    value,
    targetType,
    vehicleType,
    withTarget
  )
  return (
    <>
      {initValue && (
        <Formik
          initialValues={initValue}
          onSubmit={handleSubmit}
          validationSchema={validationSchema}
          enableReinitialize
          validateOnChange={true}
        >
          {formik => (
            <Stack direction="column" spacing={'2rem'} style={{ width: '100%' }}>
              <Stack.Item style={{ width: '100%' }}>
                <FormikSelect
                  name="infraction.controlType"
                  isRequired={true}
                  isErrorMessageHidden={true}
                  options={controlTypeOptions}
                  label="Type de contrôle avec infraction"
                  disabledItemValues={getDisabledControlTypes(availableControlTypes)}
                />
              </Stack.Item>
              {withTarget && (
                <Stack.Item style={{ width: '100%' }}>
                  <Stack direction="column" spacing={'2rem'} style={{ width: '100%' }}>
                    {formik.values.target.isVessel && (
                      <Stack.Item style={{ width: '100%' }} data-testid={'stack-vessel-infraction-env'}>
                        <MissionInfractionVesselForm size={'target.vesselSize'} type={'target.vesselType'} />
                      </Stack.Item>
                    )}
                    {formik.values.target.isTargetVehicule && (
                      <Stack.Item style={{ width: '100%' }}>
                        <FormikTextInput
                          isRequired={true}
                          label="Immatriculation"
                          role="vesselIdentifier"
                          isErrorMessageHidden={true}
                          name="target.vesselIdentifier"
                          data-testid={'vessel-identifier'}
                        />
                      </Stack.Item>
                    )}
                    <Stack.Item style={{ width: '100%', marginTop: '10px' }}>
                      <Stack direction="row" alignItems="baseline" spacing={'0.5rem'}>
                        <Stack.Item style={{ width: '100%' }}>
                          <MissionInfractionFormikControlledPersonInput
                            actionTarget={targetType}
                            role="identityControlledPerson"
                            name="target.identityControlledPerson"
                          />
                        </Stack.Item>
                      </Stack>
                    </Stack.Item>
                  </Stack>
                </Stack.Item>
              )}

              <Stack.Item style={{ width: '100%' }}>
                <Stack direction="row" alignItems="center" spacing={'0.5rem'}>
                  <Stack.Item>
                    <FormikToggle size="sm" name="withReport" label="" />
                  </Stack.Item>
                  <Stack.Item style={{ marginTop: 8 }}>
                    <Text as="h3" weight="bold" color={THEME.color.gunMetal}>
                      PV émis
                    </Text>
                  </Stack.Item>
                </Stack>
              </Stack.Item>
              <Stack.Item style={{ width: '100%' }}>
                <FormikMultiSelectNatinf name="infraction.natinfs" />
              </Stack.Item>
              <Stack.Item style={{ width: '100%' }}>
                <FormikTextarea label="Observations" name="infraction.observations" role="observations" />
              </Stack.Item>
              <Stack.Item style={{ width: '100%' }}>
                <Stack justifyContent="flex-end" spacing={'1rem'} style={{ width: '100%' }}>
                  <Stack.Item>
                    <Button size={Size.NORMAL} onClick={onClose} role="cancel-infraction" accent={Accent.TERTIARY}>
                      Annuler
                    </Button>
                  </Stack.Item>
                  <Stack.Item>
                    <Button
                      size={Size.NORMAL}
                      accent={Accent.PRIMARY}
                      role="validate-infraction"
                      onClick={async () => {
                        setDebounceTime(0)
                        handleSubmit(formik.values, formik.errors, onSubmit)
                      }}
                      disabled={!isEmpty(formik.errors)}
                    >
                      Valider l'infraction
                    </Button>
                  </Stack.Item>
                </Stack>
              </Stack.Item>
            </Stack>
          )}
        </Formik>
      )}
    </>
  )
}

export default MissionInfractionEnvForm2
