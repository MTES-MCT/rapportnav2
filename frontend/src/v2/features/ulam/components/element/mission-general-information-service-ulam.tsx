import { Accent, Button, Icon, Size } from '@mtes-mct/monitor-ui'
import { FieldArrayRenderProps } from 'formik'
import React, { useState } from 'react'
import { Stack } from 'rsuite'
import { InterMinisterialService } from 'src/v2/features/common/types/mission-types.ts'
import MissionGeneralInformationServiceForm from '../ui/mission-general-information-service-form'

interface MissionGeneralInformationServiceUlamProps {
  name: string
  fieldArray: FieldArrayRenderProps
}

const MissionGeneralInformationServiceUlam: React.FC<MissionGeneralInformationServiceUlamProps> = ({
  name,
  fieldArray
}) => {
  const [openForm, setOpenForm] = useState<boolean>(false)
  const handleDelete = (index: number) => fieldArray.remove(index)
  const handleAdd = (newValue?: any) => fieldArray.push(newValue)

  const handleUpdate = (values: any[]) => fieldArray.form.setFieldValue(name, values)

  const [interMinisterialServices, setInterMinisterialServices] = useState<InterMinisterialService[]>([])
  const addInterMinisterialService = () => {
    const service: InterMinisterialService = { name: '' }
    setInterMinisterialServices(prevServices => [...prevServices, service])
  }

  const onClickRemoveMinisterialService = (index: number) => {
    setInterMinisterialServices(prevServices => prevServices.filter((_, i) => i !== index))
  }

  //TODO: define with Clémence e realistic scenario

  return (
    <Stack direction="column" style={{ width: '100%' }}>
      <Stack.Item style={{ width: '100%' }}>
        {fieldArray.form.values.services?.map((service: any, index: number) => (
          <div key={`${service}-${index}`}>{service}</div>
        ))}
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <MissionGeneralInformationServiceForm service={{} as any} handleEdit={s => console.log(s)} />
      </Stack.Item>
      <Stack.Item style={{ width: '100%', marginTop: 16 }}>
        <Button
          Icon={Icon.Plus}
          size={Size.SMALL}
          isFullWidth={true}
          accent={Accent.SECONDARY}
          onClick={addInterMinisterialService}
        >
          Ajouter une administration
        </Button>
      </Stack.Item>
    </Stack>
  )
}

export default MissionGeneralInformationServiceUlam
