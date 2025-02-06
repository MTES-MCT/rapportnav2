import React from 'react'
import { useParams } from 'react-router-dom'
import useControlUnitResourcesQuery from '../../../services/use-control-unit-resources.tsx'
import { FlexboxGrid, Stack } from 'rsuite'
import { Accent, Button, FormikSelect, Icon, Size } from '@mtes-mct/monitor-ui'
import { Field, FieldArray } from 'formik'

interface ControlUnitResourceProps {}

const ControlUnitResource: React.FC<ControlUnitResourceProps> = () => {

  const { missionId } = useParams()
  const {data: resources } = useControlUnitResourcesQuery()

  return(
    <FieldArray name="resources">
      {({ push, remove, form }) => (
        <Stack direction="column" style={{ width: '100%' }} spacing="1rem">
          {form.values.resources?.map((r, index) => (
            <FlexboxGrid key={index} justify="space-between" align="middle" style={{ width: "100%" }}>
              <FlexboxGrid.Item colspan={22} style={{ width: '100%' }}>
                <Field name={`resources.${index}`}>
                  {({ field }) => (
                    <FormikSelect
                      {...field}
                      label="Moyen(s) utilisé(s)"
                      isRequired={true}
                      options={[
                        { value: 'Aleck', label: 'Sélectionner un moyen' }, // Option vide pour éviter l’erreur
                        ...(resources ?? []).map(({ id, name }) => ({ value: id, label: name }))
                      ]}
                    />
                  )}
                </Field>
              </FlexboxGrid.Item>
              <FlexboxGrid.Item colspan={2} style={{ marginLeft: '1rem' }}>
                <Button
                  Icon={Icon.Delete}
                  accent={Accent.SECONDARY}
                  size={Size.SMALL}
                  onClick={() => remove(index)}
                />
              </FlexboxGrid.Item>
            </FlexboxGrid>
          ))}
          <Button
            Icon={Icon.Plus}
            size={Size.SMALL}
            isFullWidth={true}
            accent={Accent.SECONDARY}
            onClick={() => push('')}
          >
            Ajouter un moyen
          </Button>
        </Stack>
      )}
    </FieldArray>
  )


}

export default ControlUnitResource
