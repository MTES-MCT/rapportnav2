import { FormikCheckbox, FormikEffect, FormikMultiRadio, FormikSelect, THEME } from '@mtes-mct/monitor-ui'
import { Field, FieldProps, Formik } from 'formik'
import { FC } from 'react'
import { Divider, Stack } from 'rsuite'
import { FormikDateRangePicker } from '../../../common/components/ui/formik-date-range-picker'
import { FormikForeignEstablishment } from '../../../common/components/ui/formik-foreign-establishment'
import { FormikSearchEstablishment } from '../../../common/components/ui/formik-search-establishment'
import { FormikSearchVessel } from '../../../common/components/ui/formik-search-vessel'
import { usecontrolCheck } from '../../../common/hooks/use-control-check'
import useAgentsQuery from '../../../common/services/use-agents'
import { Agent } from '../../../common/types/crew-type'
import { Inquiry, InquiryTargetType } from '../../../common/types/inquiry'
import { useInquiry } from '../../hooks/use-inquiry'
import { InquiryInput, useInquiryGeneralInformation } from '../../hooks/use-inquiry-general-information'

const InquiryGeneralInfoForm: FC<{
  inquiry: Inquiry
  onChange: (newInquiry: Inquiry) => Promise<unknown>
}> = ({ inquiry, onChange }) => {
  const { controlCheckRadioBooleanOptions } = usecontrolCheck()
  const { inquiryOriginOptions, inquiryTargetOptions } = useInquiry()

  const { data: agents } = useAgentsQuery()
  const { initValue, handleSubmit, validationSchema } = useInquiryGeneralInformation(inquiry, onChange)

  return (
    <form style={{ width: '100%' }}>
      {initValue && (
        <Formik
          enableReinitialize
          validateOnChange={false}
          initialValues={initValue}
          onSubmit={handleSubmit}
          validationSchema={validationSchema}
        >
          {({ values, validateForm, setErrors }) => (
            <Stack direction="column" alignItems="flex-start" style={{ width: '100%' }}>
              <FormikEffect
                onChange={nextValue =>
                  validateForm().then(async errors => {
                    await handleSubmit(nextValue as InquiryInput)
                    setErrors(errors)
                  })
                }
              />
              <Stack.Item style={{ width: '100%' }}>
                <Stack direction="column" spacing="16px" alignItems="flex-start" style={{ width: '100%' }}>
                  <Stack.Item style={{ width: '60%' }}>
                    <Stack direction="column" spacing={'16px'} style={{ width: '100%' }}>
                      <Stack.Item style={{ width: '100%' }}>
                        <Field name="dates">
                          {(field: FieldProps<Date[]>) => (
                            <FormikDateRangePicker label="" name="dates" isLight={false} fieldFormik={field} />
                          )}
                        </Field>
                      </Stack.Item>
                      <Stack.Item style={{ width: '100%' }}>
                        <FormikSelect
                          name="origin"
                          isLight={false}
                          isRequired={true}
                          label="Origine du contrôle"
                          isErrorMessageHidden={true}
                          options={inquiryOriginOptions}
                        />
                      </Stack.Item>
                      <Stack.Item style={{ width: '100%' }}>
                        <FormikSelect
                          name="agentId"
                          isLight={false}
                          isRequired={true}
                          data-testid="agent"
                          label="Agent en charge du contrôle"
                          options={
                            agents?.map((agent: Agent) => ({
                              value: agent.id,
                              label: `${agent.firstName} ${agent.lastName}`
                            })) ?? []
                          }
                        />
                      </Stack.Item>
                    </Stack>
                  </Stack.Item>
                  <Stack.Item style={{ width: '100%' }}>
                    <Divider style={{ backgroundColor: THEME.color.charcoal }} />
                  </Stack.Item>
                  <Stack.Item style={{ width: '60%' }}>
                    <Stack direction="column" spacing={'16px'} style={{ width: '100%' }}>
                      <Stack.Item style={{ width: '100%' }}>
                        <FormikSelect
                          name={'type'}
                          isLight={false}
                          isRequired={true}
                          label="Type de cible"
                          isErrorMessageHidden={true}
                          options={inquiryTargetOptions}
                        />
                      </Stack.Item>
                    </Stack>
                  </Stack.Item>
                  {values.type === InquiryTargetType.VEHICLE && (
                    <Stack.Item style={{ width: '100%' }}>
                      <Field name="vesselId">
                        {(field: FieldProps<number>) => (
                          <FormikSearchVessel
                            fieldFormik={field}
                            name="vessel.vesselId"
                            vessel={values?.vessel}
                            label="Nom du navire contrôlée"
                          />
                        )}
                      </Field>
                    </Stack.Item>
                  )}
                  {values.type === InquiryTargetType.COMPANY && (
                    <Stack.Item style={{ width: '100%' }}>
                      <Stack direction="column" spacing="0.2rem">
                        <Stack.Item style={{ width: '100%' }}>
                          {values.isForeignEstablishment ? (
                            <FormikForeignEstablishment name="siren" label="Nom de l'etablissement" />
                          ) : (
                            <Field name="siren">
                              {(field: FieldProps<string>) => (
                                <FormikSearchEstablishment
                                  name="siren"
                                  fieldFormik={field}
                                  label="Nom de l'etablissement"
                                />
                              )}
                            </Field>
                          )}
                        </Stack.Item>
                        <Stack.Item style={{ width: '100%' }}>
                          <FormikCheckbox
                            readOnly={false}
                            isLight
                            name="isForeignEstablishment"
                            label="Etablissement étranger"
                          />
                        </Stack.Item>
                      </Stack>
                    </Stack.Item>
                  )}
                </Stack>
              </Stack.Item>
              <Stack.Item style={{ width: '100%' }}>
                <Divider style={{ backgroundColor: THEME.color.charcoal }} />
              </Stack.Item>
              <Stack.Item style={{ width: '100%' }}>
                <Stack direction="column" spacing="2rem" alignItems="flex-start" style={{ width: '100%' }}>
                  <Stack.Item>
                    <FormikMultiRadio
                      isInline={true}
                      isRequired={true}
                      name={'isSignedByInspector'}
                      label={`Rapport signé par l'inspecteur`}
                      data-testid={`actions-mise-en-croisé`}
                      options={controlCheckRadioBooleanOptions}
                    />
                  </Stack.Item>
                </Stack>
              </Stack.Item>
            </Stack>
          )}
        </Formik>
      )}
    </form>
  )
}

export default InquiryGeneralInfoForm
