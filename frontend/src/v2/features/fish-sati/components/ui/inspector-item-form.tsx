import { FormikMultiRadio } from '@mtes-mct/monitor-ui'
import { FC } from 'react'
import { Stack } from 'rsuite'
import { FormikSelectInput } from '../../../common/components/ui/formik-select-input.tsx'
import { FormikTextInput } from '../../../common/components/ui/formik-text-input.tsx'
import { useAgent } from '../../../common/hooks/use-agent.tsx'
import { useAuthority } from '../../../common/hooks/use-authority.tsx'
import { SatiInspector } from '../../../common/types/sati.ts'

interface InspectorItemFormProps {
  readOnly?: boolean
  isPrincipal?: boolean
  values?: SatiInspector
}

const InspectorItemForm: FC<InspectorItemFormProps> = ({ values, readOnly, isPrincipal }) => {
  const { agents } = useAgent()
  const { authorityTypeOptions } = useAuthority()

  return (
    <Stack direction="column" spacing=".8rem" alignItems="flex-start" style={{ width: '100%' }}>
      <Stack.Item style={{ width: '100%' }}>
        <FormikMultiRadio
          isInline
          readOnly={readOnly}
          name="authorityType"
          options={authorityTypeOptions}
          label="Autorité investie du pouvoir de nomination"
        />
      </Stack.Item>

      <Stack.Item style={{ width: '100%' }}>
        {values?.isOutOfUnit ? (
          <Stack direction="row" spacing=".5rem" justifyContent={'flex-start'} style={{ width: '100%' }}>
            <Stack.Item style={{ flex: 1 }}>
              <FormikTextInput label="Nom" isLight={readOnly} readOnly={readOnly} name="party.contact.lastName" />
            </Stack.Item>
            <Stack.Item style={{ flex: 1 }}>
              <FormikTextInput label="Prénom" isLight={readOnly} readOnly={readOnly} name="party.contact.firstName" />
            </Stack.Item>
          </Stack>
        ) : (
          <FormikSelectInput
            isRequired
            name="agentId"
            options={agents}
            isLight={readOnly}
            readOnly={readOnly}
            isErrorMessageHidden
            label="Agent en charge du contrôle"
          />
        )}
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <FormikTextInput
          name="cardId"
          isLight={readOnly}
          placeholder="ex : FS7414"
          readOnly={!values?.isOutOfUnit || isPrincipal}
          label="Identifiant unique de l’inspecteur (n° de la carte de service...)"
        />
      </Stack.Item>
    </Stack>
  )
}
export default InspectorItemForm
