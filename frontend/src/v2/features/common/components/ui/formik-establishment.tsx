import { Checkbox, FormikSearchProps } from '@mtes-mct/monitor-ui'
import { FieldProps, Formik } from 'formik'
import { Stack } from 'rsuite'
import styled from 'styled-components'
import { useAbstractFormikSubForm } from '../../hooks/use-abstract-formik-sub-form'
import { Establishment } from '../../types/etablishment'
import { ForeignEstablishment } from './foreign-establishment'
import { SearchEstablishment } from './search-establishment'

type FormikEstablishmentProps = {
  name: string
  fieldFormik: FieldProps<Establishment>
}

export const FormikEstablishment = styled(
  ({ name, fieldFormik }: Omit<FormikSearchProps, 'options' | 'label'> & FormikEstablishmentProps) => {
    const label = "Nom de l'etablissement"
    const { initValue, handleSubmit } = useAbstractFormikSubForm(
      name,
      fieldFormik,
      value => value,
      value => value,
      ['isForeign']
    )
    return (
      <>
        {initValue && (
          <Formik initialValues={initValue} enableReinitialize onSubmit={handleSubmit} validateOnChange={false}>
            {({ values }) => (
              <Stack direction="column" spacing="0.2rem" style={{ width: '100%' }}>
                <Stack.Item style={{ width: '100%' }}>
                  {values.isForeign ? (
                    <ForeignEstablishment
                      label={label}
                      name={`name`}
                      establishment={values}
                      handleSubmit={handleSubmit}
                    />
                  ) : (
                    <SearchEstablishment name="" label={label} establishment={values} handleSubmit={handleSubmit} />
                  )}
                </Stack.Item>
                <Stack.Item style={{ width: '100%' }}>
                  <Checkbox
                    checked={values.isForeign}
                    readOnly={false}
                    isLight
                    name={`isForeign`}
                    label="Etablissement étranger"
                    onChange={v => {
                      if (v === values.isForeign) return
                      handleSubmit({ id: values?.id, isForeign: v })
                    }}
                  />
                </Stack.Item>
              </Stack>
            )}
          </Formik>
        )}
      </>
    )
  }
)(() => ({}))
