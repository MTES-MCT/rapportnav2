import { FieldArrayRenderProps } from 'formik'
import { FC } from 'react'
import { Stack } from 'rsuite'
import { SatiInspector } from '../../../common/types/sati.ts'
import InspectorItemNew from './inspector-item-new.tsx'
import InspectorFormOther from './inspector-item-other.tsx'

interface InspectorListProps {
  inspectors?: SatiInspector[]
  fieldArray: FieldArrayRenderProps
  excludedAgentIds?: number[]
}

const InspectorList: FC<InspectorListProps> = ({ fieldArray, inspectors, excludedAgentIds }) => {
  const handleDelete = (index: number) => {
    fieldArray.remove(index)
  }

  const handleSubmit = (index: number, response?: SatiInspector) => {
    fieldArray.replace(index, response)
  }

  const handleAdd = (response?: SatiInspector) => {
    fieldArray.push(response)
  }

  return (
    <Stack direction="column" spacing="1rem" alignItems="flex-start" style={{ width: '100%' }}>
      <Stack.Item style={{ width: '100%' }}>
        {inspectors?.map((inspector, index) => (
          <InspectorFormOther
            index={index + 2}
            inspector={inspector}
            key={`inspector-${index}`}
            onDelete={() => handleDelete(index)}
            onChange={v => handleSubmit(index, v)}
            excludedAgentIds={excludedAgentIds}
          />
        ))}
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <InspectorItemNew onSubmit={handleAdd} excludedAgentIds={excludedAgentIds} />
      </Stack.Item>
    </Stack>
  )
}

export default InspectorList
