import { Accent, Button, FormikMultiSelect, Icon, Size } from '@mtes-mct/monitor-ui'
import { Field, FieldArrayRenderProps, FieldProps } from 'formik'
import React from 'react'
import { Stack } from 'rsuite'
import useControlUnitResourcesQuery from '../../services/use-control-unit-resources.tsx'

interface MissionGeneralInformationControlUnitRessourceProps {
  name: string
  fieldArray: FieldArrayRenderProps
}

const MissionGeneralInformationControlUnitRessource: React.FC<MissionGeneralInformationControlUnitRessourceProps> = ({
  name,
  fieldArray
}) => {
  const { data: resources } = useControlUnitResourcesQuery()

  const handleDelete = (index: number) => fieldArray.remove(index)
  const handleAdd = (newValue?: string) => fieldArray.push(newValue)

  return (
    <Stack direction="column" style={{ width: '100%' }}>
      <Stack.Item style={{ width: '100%' }}>
        <Field name={name}>
          {({ field, form }: FieldProps<string>) => (
            <FormikMultiSelect
              {...field}
              style={{ width: '100%' }}
              label="Moyen(s) utilisé(s)"
              placeholder=""
              isRequired={true}
              searchable={true}
              options={
                resources?.map((resource: any) => ({
                  value: resource.id,
                  label: `${resource.name}`
                })) ?? []
              }
              value={field.value}
              onChange={(value: any) => form.setFieldValue(field.name, value)}
            />
          )}
        </Field>
      </Stack.Item>
      <Stack.Item style={{ width: '100%', marginTop: 16 }}>
        <Button
          Icon={Icon.Plus}
          size={Size.SMALL}
          isFullWidth={true}
          accent={Accent.SECONDARY}
          onClick={() => console.log('Do nothing')}
        >
          Ajouter un moyen
        </Button>
      </Stack.Item>
    </Stack>
  )
}

export default MissionGeneralInformationControlUnitRessource
