import React, { FC, useState } from 'react'
import { Field, FieldProps, Formik } from 'formik'
import {
  InterMinisterialService,
  MissionGeneralInfoExtended,
} from '../../../common/types/mission-types.ts'
import {
  Accent,
  Button,
  FormikCheckbox,
  FormikEffect, FormikTextarea,
  Icon,
  Size
} from '@mtes-mct/monitor-ui'
import {  Stack } from 'rsuite'
import MissionCrewUlam from './mission-crew-ulam.tsx'
import {
  useMissionGeneralInformationsExtendedForm
} from '../../../common/hooks/use-mission-general-informations-extended-form.tsx'
import ControlUnitResource from './controlUnitResource/control-unit-resource.tsx'
import MissionObservationsUnit from '../../../common/components/elements/mission-observations-unit.tsx'

export interface MissionGeneralInformationExtendedFormProps {
  name: string,
  fieldFormik: FieldProps<MissionGeneralInfoExtended>
}

const MissionGeneralInformationExtendedForm: FC<MissionGeneralInformationExtendedFormProps> = ({name, fieldFormik}) => {
  const { initValue, handleSubmit } = useMissionGeneralInformationsExtendedForm(name, fieldFormik)
  const [interMinisterialServices, setInterMinisterialServices] = useState<InterMinisterialService[]>([])

  const addInterMinisterialService = () => {
    const service: InterMinisterialService = { name: "" }
    setInterMinisterialServices(prevServices => [...prevServices, service])
  }

  const onClickRemoveMinisterialService = (index: number) => {
    setInterMinisterialServices(prevServices =>
      prevServices.filter((_, i) => i !== index)
    )
  }

  return (
    <>
      {initValue && (
        <Formik initialValues={initValue} onSubmit={handleSubmit}>
          {(formik) => (
            <Stack direction="column" spacing="1em" style={{width: "100%"}} alignItems={'flex-start'}>
              <FormikEffect onChange={newValues => handleSubmit(newValues)} />

              <Stack.Item style={{ width: '100%' }}>
                <MissionCrewUlam />
              </Stack.Item>

              <Stack.Item style={{ width: '100%' }}>
                <FormikCheckbox
                  name={"isMissionArmed"}
                  label={"Mission armée"}
                />
              </Stack.Item>

              <Stack.Item style={{ width: '100%' }}>
                <FormikCheckbox
                  name={"isWithInterMinisterialService"}
                  label={"Mission conjointe (avec un autre service)"}
                />
              </Stack.Item>

              {formik.values.isWithInterMinisterialService && (
                <Stack.Item style={{ width: '100%', marginBottom: '1.5rem' }}>
                  <Button
                    Icon={Icon.Plus}
                    size={Size.SMALL}
                    isFullWidth={true}
                    accent={Accent.SECONDARY}
                    onClick={addInterMinisterialService}
                  >
                    Ajouter une administration
                  </Button>
                </Stack.Item>
              )}

              <Stack style={{marginTop: '1.5rem'}}>
                <Stack.Item style={{width:'100%', paddingRight: 20}}>
                  <FormikTextarea
                    isRequired={true}
                    isLight={false}
                    name="observations"
                    data-testid="mission-general-observation"
                    label="Observation générale à l'échelle de la mission (remarques, résumé)"
                    isErrorMessageHidden={true}
                  />
                </Stack.Item>
              </Stack>
            </Stack>
          )}
        </Formik>
      )}
    </>

  )
}

export default MissionGeneralInformationExtendedForm
