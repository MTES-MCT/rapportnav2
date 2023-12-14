import React from 'react'
import {Accent, Tag} from '@mtes-mct/monitor-ui'
import {InfractionTypeEnum, infractionTypeLabels} from "../../../types/env-mission-types.ts";
import Text from "../../../ui/text.tsx";

interface InfractionTagProps {
    type?: InfractionTypeEnum
}

const InfractionTypeTag: React.FC<InfractionTagProps> = ({type}) => {
    const label = !type ? infractionTypeLabels[InfractionTypeEnum.WITHOUT_REPORT] : infractionTypeLabels[type]
    return <Tag accent={Accent.PRIMARY}>
        <Text as="h3" weight='medium'>{label.libelle}</Text>
    </Tag>
}

export default InfractionTypeTag
