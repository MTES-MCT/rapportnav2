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
        <Stack direction="column" style={{ width: '100%' }}  alignItems={"flex-start"}>
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
                        ...(resources ?? []).map(({ id, name }) => ({ value: id, label: name }))
                      ]}
                      style={{ width: '100%' }}
                    />
                  )}
                </Field>
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
