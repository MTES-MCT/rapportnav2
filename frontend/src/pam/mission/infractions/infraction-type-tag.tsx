import React from 'react'
import {Accent, Tag} from '@mtes-mct/monitor-ui'
import {InfractionTypeEnum, infractionTypeLabels} from "../../../types/env-mission-types.ts";
import Text from "../../../ui/text.tsx";
import {INFRACTION_TYPE_LABEL, InfractionType} from "../../../types/fish-mission-types.ts";

interface InfractionTagProps {
    type?: InfractionTypeEnum | InfractionType;
}

const isInfractionTypeEnum = (value: any): value is InfractionTypeEnum => {
    return Object.values(InfractionTypeEnum).includes(value);
};

const isInfractionType = (value: any): value is InfractionType => {
    return Object.values(InfractionType).includes(value);
};


const InfractionTypeTag: React.FC<InfractionTagProps> = ({type}) => {
    let label;

    if (type === undefined) {
        label = infractionTypeLabels[InfractionTypeEnum.WITHOUT_REPORT].libelle;
    } else if (isInfractionTypeEnum(type)) {
        label = infractionTypeLabels[type].libelle;
    } else if (isInfractionType(type)) {
        label = INFRACTION_TYPE_LABEL[type];
    } else {
        label = infractionTypeLabels[InfractionTypeEnum.WITHOUT_REPORT].libelle;
    }

    return (
        <Tag accent={Accent.PRIMARY}>
            <Text as="h3" weight="medium">
                {label}
            </Text>
        </Tag>
    );
}

export default InfractionTypeTag
