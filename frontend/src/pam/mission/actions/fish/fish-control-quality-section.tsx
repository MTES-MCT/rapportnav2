import React from 'react'
import {Accent, Button, Checkbox, Icon, Label, MultiRadio, Size, THEME} from '@mtes-mct/monitor-ui'
import {Stack} from 'rsuite'
import {controlCheckMultiradioOptions} from '../action-control-fish'
import {FishAction} from '../../../../types/fish-mission-types'
import Text from '../../../../ui/text'

interface FishControlQualitySectionProps {
    action: FishAction
}

const FishControlQualitySection: React.FC<FishControlQualitySectionProps> = ({action}) => {
    return (
        <Stack direction="column" alignItems="flex-start" spacing={'0.2rem'}>
            <Stack.Item>
                <Label>Qualité du contrôle</Label>
            </Stack.Item>
            <Stack.Item style={{backgroundColor: THEME.color.white, width: '100%', padding: '1rem'}}>
                <Stack direction="column" alignItems="flex-start" spacing={'1rem'}>
                    <Stack.Item>
                        <MultiRadio
                            isReadOnly={true}
                            isInline
                            label="Navire ciblé par le CNSP"
                            value={action?.vesselTargeted}
                            name="vesselTargeted"
                            onChange={function noRefCheck() {
                            }}
                            options={controlCheckMultiradioOptions}
                        />
                    </Stack.Item>
                    <Stack.Item>
                        <Checkbox
                            readOnly={true}
                            name="unitWithoutOmegaGauge"
                            label="Unité sans jauge oméga"
                            checked={!!action.unitWithoutOmegaGauge}
                        />
                    </Stack.Item>
                    <Stack.Item>
                        <Label>Observations sur le déroulé du contrôle</Label>
                        <Text as="h3" weight="medium">
                            {!!action?.controlQualityComments ? action.controlQualityComments : 'Aucune observation'}
                        </Text>
                    </Stack.Item>
                    <Stack.Item>
                        <Checkbox
                            readOnly={true}
                            name="feedbackSheetRequired"
                            label="Fiche RETEX nécessaire"
                            checked={!!action.feedbackSheetRequired}
                        />
                    </Stack.Item>
                </Stack>
            </Stack.Item>
        </Stack>
    )
}

export default FishControlQualitySection
