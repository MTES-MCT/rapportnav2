import Text from '@common/components/ui/text'
import React from 'react'

type GeneralInformationHeaderProps = {
  title: string
}

const GeneralInformationHeader: React.FC<GeneralInformationHeaderProps> = ({ title }) => {
  return (
    <Text as="h2" weight="bold">
      {`Informations générales ${title}`}
    </Text>
  )
}

export default GeneralInformationHeader
