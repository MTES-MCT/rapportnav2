import Text from '@common/components/ui/text'
import { Infraction, InfractionTypeEnum } from '@common/types/env-mission-types.ts'
import { Accent, Button, FormikTextarea, FormikToggle, Size, THEME } from '@mtes-mct/monitor-ui'
import { Formik } from 'formik'
import { isEqual } from 'lodash'
import { FC, useEffect, useState } from 'react'
import { Stack } from 'rsuite'
import { FormikMultiSelectNatinf } from '../../../common/components/ui/formik-multi-select-natinf'
import useNatinfListQuery from '../../../common/services/use-natinf-service'

export type InfractionData = {
  report: boolean
} & Infraction

export interface MissionActionInfractionFormProps {
  onCancel: () => void
  infraction?: Infraction
  onChange: (field: Infraction) => void
}

const MissionActionInfractionForm: FC<MissionActionInfractionFormProps> = ({ infraction, onChange, onCancel }) => {
  const { data: natinfs } = useNatinfListQuery()
  const [initValue, setInitValue] = useState<InfractionData>()

  useEffect(() => {
    if (!infraction) return
    const report = infraction?.infractionType === InfractionTypeEnum.WITH_REPORT
    setInitValue({ ...infraction, report })
  }, [infraction])

  const handleSubmit = async (value: InfractionData): Promise<void> => {
    if (isEqual(value, initValue)) return
    const { report, ...newValue } = value
    const infractionType = report ? InfractionTypeEnum.WITH_REPORT : InfractionTypeEnum.WITHOUT_REPORT
    setInitValue(value)
    onChange({ ...newValue, infractionType })
  }

  return (
    <>
      {initValue && (
        <Formik initialValues={initValue} onSubmit={handleSubmit}>
          <Stack direction="column" spacing={'2rem'} style={{ width: '100%' }}>
            <Stack.Item style={{ width: '100%' }}>
              <Stack direction="row" alignItems="baseline" spacing={'0.5rem'}>
                <Stack.Item>
                  <FormikToggle
                    size="sm"
                    name="report"
                    label=""
                    //checked={infraction?.infractionType === InfractionTypeEnum.WITH_REPORT}
                    //defaultValue={InfractionTypeEnum.WITHOUT_REPORT}
                    //role="toggle-infraction-type"
                  />
                </Stack.Item>
                <Stack.Item>
                  <Text as="h3" weight="bold" color={THEME.color.gunMetal}>
                    PV émis
                  </Text>
                </Stack.Item>
              </Stack>
            </Stack.Item>
            <Stack.Item style={{ width: '100%' }}>
              <FormikMultiSelectNatinf name="natinf" natinfOptions={natinfs ?? []} />
            </Stack.Item>
            <Stack.Item style={{ width: '100%' }}>
              <FormikTextarea label="Observations" name="observations" role="observations" />
            </Stack.Item>
            <Stack.Item style={{ width: '100%' }}>
              <Stack justifyContent="flex-end" spacing={'1rem'} style={{ width: '100%' }}>
                <Stack.Item>
                  <Button size={Size.NORMAL} onClick={onCancel} role="cancel-infraction" accent={Accent.TERTIARY}>
                    Annuler
                  </Button>
                </Stack.Item>
                <Stack.Item>
                  <Button
                    type="submit"
                    size={Size.NORMAL}
                    accent={Accent.PRIMARY}
                    role="validate-infraction"
                    disabled={infraction?.natinf?.length === 0}
                    //disabled={!infraction?.controlType || !infraction?.natinfs?.length}
                  >
                    Valider l'infraction
                  </Button>
                </Stack.Item>
              </Stack>
            </Stack.Item>
          </Stack>
        </Formik>
      )}
    </>
  )
}

export default MissionActionInfractionForm
