import Text from '@common/components/ui/text'
import { Infraction } from '@common/types/infraction-types'
import { Accent, Button, FormikTextarea, FormikToggle, Size, THEME } from '@mtes-mct/monitor-ui'
import { FieldProps, Formik } from 'formik'
import { FC } from 'react'
import { Stack } from 'rsuite'
import { setDebounceTime } from '../../../../store/slices/delay-query-reducer'
import { FormikMultiSelectNatinf } from '../../../common/components/ui/formik-multi-select-natinf'
import { useInfractionNavForm } from '../../hooks/use-infraction-nav-form'

export interface MissionInfractionFormProps {
  name: string
  onClose: () => void
  fieldFormik: FieldProps<Infraction>
}

const MissionInfractionForm: FC<MissionInfractionFormProps> = ({ name, onClose, fieldFormik }) => {
  const { initValue, handleSubmit } = useInfractionNavForm(name, fieldFormik)
  return (
    <>
      {initValue && (
        <Formik initialValues={initValue} onSubmit={handleSubmit} enableReinitialize>
          {formik => (
            <Stack direction="column" spacing={'2rem'} style={{ width: '100%' }}>
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
                <FormikMultiSelectNatinf name="natinf" />
              </Stack.Item>
              <Stack.Item style={{ width: '100%' }}>
                <FormikTextarea label="Observations" name="observations" role="observations" />
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
                      //disabled={!initValue.controlType || !initValue.natinfs?.length}
                      //TODO:  check cette règle disabled={!infraction?.controlType || !infraction?.natinfs?.length}
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

export default MissionInfractionForm
