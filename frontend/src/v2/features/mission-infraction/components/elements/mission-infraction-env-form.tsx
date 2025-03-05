import Text from '@common/components/ui/text'
import { ControlType } from '@common/types/control-types'
import { ActionTargetTypeEnum } from '@common/types/env-mission-types'
import { InfractionByTarget } from '@common/types/infraction-types'
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
import { FieldProps, Formik } from 'formik'
import { isEmpty } from 'lodash'
import { FC } from 'react'
import { Stack } from 'rsuite'
import { setDebounceTime } from '../../../../store/slices/delay-query-reducer'
import { FormikMultiSelectNatinf } from '../../../common/components/ui/formik-multi-select-natinf'
import { useControlRegistry } from '../../../mission-control/hooks/use-control-registry'
import { useInfractionEnvForm } from '../../hooks/use-infraction-env-form'
import { MissionInfractionFormikControlledPersonInput } from '../ui/mission-infraction-formik-controlled-person-input'
import MissionInfractionVesselForm from '../ui/mission-infraction-vessel-form'

export interface MissionInfractionEnvFormProps {
  name: string
  onClose: () => void
  hideTarget?: boolean
  availableControlTypes?: ControlType[]
  actionTargetType?: ActionTargetTypeEnum
  fieldFormik: FieldProps<InfractionByTarget>
}

const MissionInfractionEnvForm: FC<MissionInfractionEnvFormProps> = ({
  name,
  onClose,
  hideTarget,
  fieldFormik,
  actionTargetType,
  availableControlTypes
}) => {
  const { controlTypeOptions, getDisabledControlTypes } = useControlRegistry()
  const { initValue, handleSubmit, validationSchema } = useInfractionEnvForm(name, fieldFormik, actionTargetType)

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
                  isRequired={true}
                  isErrorMessageHidden={true}
                  name="infractions[0].controlType"
                  options={controlTypeOptions}
                  label="Type de contrôle avec infraction"
                  disabledItemValues={getDisabledControlTypes(availableControlTypes)}
                />
              </Stack.Item>
              {!hideTarget && (
                <Stack.Item style={{ width: '100%' }}>
                  <Stack direction="column" spacing={'2rem'} style={{ width: '100%' }}>
                    {formik.values.isVessel && (
                      <Stack.Item style={{ width: '100%' }} data-testid={'stack-vessel-infraction-env'}>
                        <MissionInfractionVesselForm
                          size={'infractions[0].target.vesselSize'}
                          type={'infractions[0].target.vesselType'}
                        />
                      </Stack.Item>
                    )}
                    {formik.values.isTargetVehicule && (
                      <Stack.Item style={{ width: '100%' }}>
                        <FormikTextInput
                          isRequired={true}
                          label="Immatriculation"
                          role="vesselIdentifier"
                          isErrorMessageHidden={true}
                          data-testid={'vessel-identifier'}
                          name="infractions[0].target.vesselIdentifier"
                        />
                      </Stack.Item>
                    )}
                    <Stack.Item style={{ width: '100%', marginTop: '10px' }}>
                      <Stack direction="row" alignItems="baseline" spacing={'0.5rem'}>
                        <Stack.Item style={{ width: '100%' }}>
                          <MissionInfractionFormikControlledPersonInput
                            actionTarget={actionTargetType}
                            role="identityControlledPerson"
                            name="infractions[0].target.identityControlledPerson"
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
                <FormikMultiSelectNatinf name="infractions[0].natinfs" />
              </Stack.Item>
              <Stack.Item style={{ width: '100%' }}>
                <FormikTextarea label="Observations" name="infractions[0].observations" role="observations" />
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
                        handleSubmit(formik.values).then(() => onClose())
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

export default MissionInfractionEnvForm
