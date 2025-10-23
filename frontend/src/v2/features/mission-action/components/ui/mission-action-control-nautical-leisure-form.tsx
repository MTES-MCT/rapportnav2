import Text from '@common/components/ui/text'
import { FormikNumberInput, FormikSelect, THEME } from '@mtes-mct/monitor-ui'
import { FormikProps } from 'formik'
import { FC } from 'react'
import { Stack } from 'rsuite'
import { StyledFormikToggle } from '../../../common/components/ui/formik-styled-toogle'
import { LEISURE_TYPES, LeisureType } from '../../../common/types/leisure-fishing-gear-type'
import { ActionControlInput } from '../../types/action-type'

const MissionActionNauticalLeisureControlForm: FC<{ formik: FormikProps<ActionControlInput> }> = ({ formik }) => {
  return (
    <Stack.Item style={{ width: '100%' }}>
      <Stack direction="column" spacing={'1rem'} alignItems="flex-start">
        <Stack.Item style={{ width: '70%' }}>
          <FormikSelect
            name="leisureType"
            label="Type de loisir"
            isLight={true}
            isRequired={true}
            options={Object.keys(LeisureType)?.map(key => ({
              value: key,
              label: LEISURE_TYPES[key as keyof typeof LeisureType]
            }))}
          />
        </Stack.Item>
        <Stack.Item style={{ width: '100%' }}>
          <Stack direction="column" spacing={'.1rem'}>
            <Stack.Item style={{ width: '100%' }}>
              <Stack direction="row" spacing={'.5rem'}>
                <Stack.Item style={{ width: '100%' }}>
                  <FormikNumberInput
                    isLight={true}
                    isRequired={true}
                    name="nbrOfControl"
                    label="Nombre total de contrôles"
                    isErrorMessageHidden={true}
                  />
                </Stack.Item>
                <Stack.Item style={{ width: '100%' }}>
                  <FormikNumberInput
                    isLight={true}
                    isRequired={true}
                    name="nbrOfControl300m"
                    max={formik.values.nbrOfControl}
                    label="Nb dans la bande des 300m"
                    isErrorMessageHidden={true}
                  />
                </Stack.Item>
                <Stack.Item style={{ width: '70%' }}>
                  <FormikNumberInput
                    isLight={true}
                    isRequired={true}
                    label="Nb en AMP"
                    name="nbrOfControlAmp"
                    isErrorMessageHidden={true}
                    max={formik.values.nbrOfControl}
                  />
                </Stack.Item>
              </Stack>
            </Stack.Item>
            <Stack.Item style={{ width: '100%' }}>
              <Text as="h3" color={THEME.color.maximumRed}>
                {(formik.errors?.nbrOfControl300m || formik.errors?.nbrOfControlAmp) &&
                  `Nombre en AMP, Nombre dans la bande 300m, doivent être inférieur au Nombre total de contrôles`}
              </Text>
            </Stack.Item>
          </Stack>
        </Stack.Item>
        <Stack.Item style={{ width: '100%' }}>
          <StyledFormikToggle
            name="isControlDuringSecurityDay"
            label="Contrôle(s) réalisé(s) dans le cadre d'une journée sécurité mer"
          />
        </Stack.Item>
      </Stack>
    </Stack.Item>
  )
}
export default MissionActionNauticalLeisureControlForm
