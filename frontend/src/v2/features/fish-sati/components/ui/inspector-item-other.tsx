import { FC } from 'react'
import { SatiInspector } from '../../../common/types/sati.ts'
import InspectorItemLayout from '../layout/inspector-item-layout.tsx'
import InspectorItem from './inspector-item.tsx'

interface InspectorItemOtherProps {
  index: number
  onClose?: () => void
  onDelete?: () => void
  inspector?: SatiInspector
  onChange: (response?: SatiInspector) => void
  excludedAgentIds?: number[]
}

const InspectorItemOther: FC<InspectorItemOtherProps> = ({ index, inspector, onDelete, onChange, excludedAgentIds }) => {
  const handleSubmit = (response?: SatiInspector) => {
    onChange(response)
  }

  const handleDelete = () => {
    if (onDelete) onDelete()
  }

  return (
    <InspectorItemLayout
      title={`Inspecteur ${index}`}
      inspectorItem={
        <InspectorItem
          isPrincipal={false}
          inspector={inspector}
          onSubmit={handleSubmit}
          onDelete={handleDelete}
          readOnly={!!inspector?.id}
          excludedAgentIds={excludedAgentIds}
        />
      }
    />
  )
}
export default InspectorItemOther
