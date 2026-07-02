import { FC } from 'react'
import { SatiInspector } from '../../../common/types/sati.ts'
import InspectorItemLayout from '../layout/inspector-item-layout.tsx'
import InspectorItem from './inspector-item.tsx'

interface InspectorItemPrincipalProps {
  inspector?: SatiInspector
  onChange: (response?: SatiInspector) => void
}

const InspectorItemPrincipal: FC<InspectorItemPrincipalProps> = ({ inspector, onChange }) => {
  const handleChange = (response?: SatiInspector) => {
    onChange(response)
  }
  return (
    <InspectorItemLayout
      title={`Inspecteur 1`}
      inspectorItem={
        <InspectorItem readOnly={false} isPrincipal={true} inspector={inspector} onChange={handleChange} />
      }
    />
  )
}
export default InspectorItemPrincipal
