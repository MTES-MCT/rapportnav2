import { Checkbox, FormikEffect, FormikSearchProps } from '@mtes-mct/monitor-ui'
import { Formik } from 'formik'
import { useState } from 'react'
import { Stack } from 'rsuite'
import styled from 'styled-components'
import { useAbstractFormik } from '../../hooks/use-abstract-formik-form'
import { useEstablishmentAddress } from '../../hooks/use-establishment-address'
import { Establishment } from '../../types/etablishment'
import AddressForm from './address-form'
import { EstablishmentFormName } from './establishment-form-name'
import { EstablishmentSearch } from './establishment-search'
import { EstablishmentWarning } from './establishment-warning'

type EstablishmentFormProps = {
  establishment?: Establishment
  onChange: (establisment?: Establishment) => Promise<unknown>
}

export const EstablishmentForm = styled(
  ({
    name,
    establishment,
    onChange,
    ...props
  }: Omit<FormikSearchProps, 'options' | 'label'> & EstablishmentFormProps) => {
    const [warning, setWarning] = useState<boolean>(false)
    const { initValue, handleSubmit } = useAbstractFormik(
      establishment,
      value => value,
      value => value,
      ['isNotSameAddress', 'isNotListed', 'isForeign', 'isHeadquarter']
    )
    const { resetAddress, resetCompany, establishmentToAddress, addressToEstablishment } = useEstablishmentAddress()
    return (
      <>
        {initValue && (
          <Formik
            enableReinitialize
            validateOnChange={false}
            initialValues={initValue}
            onSubmit={nextValue => handleSubmit(nextValue)}
          >
            {({ values, setFieldValue, setValues }) => (
              <Stack direction="column" spacing="0.2rem" style={{ width: '100%' }}>
                <FormikEffect onChange={nextValue => handleSubmit(nextValue, onChange)} />
                <Stack.Item style={{ width: '100%' }}>
                  {values.isForeign ? (
                    <EstablishmentFormName
                      name="name"
                      isLight={true}
                      initValue={values.name}
                      label="Etablissement contrôlé"
                      handleSubmit={v => setFieldValue('name', v)}
                    />
                  ) : (
                    <EstablishmentSearch
                      name=""
                      {...props}
                      disabled={warning}
                      establishment={values}
                      label="Etablissement contrôlé"
                      handleSubmit={v => setValues({ ...values, ...v })}
                    />
                  )}
                </Stack.Item>
                {warning && (
                  <Stack.Item style={{ width: '100%' }}>
                    <EstablishmentWarning
                      onNo={() => setWarning(false)}
                      onYes={() => {
                        setWarning(false)
                        setValues({ ...values, name: '', isForeign: true })
                      }}
                    />
                  </Stack.Item>
                )}
                <Stack.Item style={{ width: '100%' }}>
                  <Checkbox
                    isLight
                    readOnly={false}
                    name="isForeign"
                    disabled={warning}
                    checked={values.isForeign}
                    label="Etablissement non présent dans la liste"
                    onChange={v => {
                      if (v) {
                        setWarning(true)
                      } else {
                        setValues({ ...resetCompany(values), isForeign: v })
                      }
                    }}
                  />
                </Stack.Item>
                <Stack.Item style={{ width: '100%' }}>
                  <Checkbox
                    isLight
                    readOnly={false}
                    disabled={warning}
                    name="isNotSameAddress"
                    checked={values.isNotSameAddress}
                    label="L’adresse de contrôle est différente du siège social de l’établissement"
                    onChange={v => {
                      setValues({ ...(v ? resetAddress(values) : values), isNotSameAddress: v })
                    }}
                  />
                </Stack.Item>
                {values.isNotSameAddress && (
                  <Stack.Item style={{ width: '100%', marginTop: '0.8rem' }}>
                    <AddressForm
                      address={establishmentToAddress(values)}
                      onChange={address => {
                        const v = addressToEstablishment(address, values)
                        console.log(v)
                        setValues(v)
                      }}
                    />
                  </Stack.Item>
                )}
              </Stack>
            )}
          </Formik>
        )}
      </>
    )
  }
)(() => ({}))
