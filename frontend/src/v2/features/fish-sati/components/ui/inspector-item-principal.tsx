import { FC } from 'react'
import { SatiInspector } from '../../../common/types/sati.ts'
import InspectorItemLayout from '../layout/inspector-item-layout.tsx'
import InspectorItem from './inspector-item.tsx'

interface InspectorItemPrincipalProps {
  inspector?: SatiInspector
  onChange: (response?: SatiInspector) => void
  excludedAgentIds?: number[]
}

const InspectorItemPrincipal: FC<InspectorItemPrincipalProps> = ({ inspector, onChange, excludedAgentIds }) => {
  const handleChange = (response?: SatiInspector) => {
    onChange(response)
  }
  return (
    <InspectorItemLayout
      title={`Inspecteur en charge`}
      inspectorItem={
        <InspectorItem
          readOnly={false}
          isPrincipal={true}
          inspector={inspector}
          onChange={handleChange}
          excludedAgentIds={excludedAgentIds}
        />
      }
    />
  )
}
export default InspectorItemPrincipal
