import React from 'react'
import { Accent, Icon, Tag, THEME } from '@mtes-mct/monitor-ui'
import {
    actionTargetTypeLabels,
    ActionTypeEnum,
    EnvActionControl,
    MissionSourceEnum
} from '../../../types/env-mission-types'
import { FlexboxGrid, Stack } from 'rsuite'
import { Action, ActionControl, ActionStatus as NavActionStatus } from '../../../types/action-types'
import { FishAction, formatMissionActionTypeForHumans } from '../../../types/fish-mission-types'
import { StatusColorTag } from '../status/status-selection-dropdown'
import { mapStatusToText, statusReasonToHumanString } from '../status/utils'
import { controlMethodToHumanString, vesselTypeToHumanString } from '../controls/utils'
import ControlsToCompleteTag from '../controls/controls-to-complete-tag'
import Text from '../../../ui/text'
import { useParams } from 'react-router-dom'
import { vesselNameOrUnknown } from "../actions/utils.ts";


const ActionSurveillance: React.FC = () => {
    // Implementation for ActionSurveillance
    return null
}

export default ActionSurveillance
