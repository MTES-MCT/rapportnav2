import { FC, ReactNode } from 'react';
import styled from 'styled-components';
import { THEME } from '@mtes-mct/monitor-ui';

interface TextProps {
    as: 'h1' | 'h2' | 'h3' | 'h4';
    color?: string;
    weight?: 'normal' | 'medium' | 'bold';
    fontStyle?: 'normal' | 'italic';
    decoration?: 'normal' | 'underline';
    children: ReactNode;
}

const fontWeights = {
    normal: '400',
    medium: '500',
    bold: '700'
};

type StyledTextProps = Pick<TextProps, 'children' | 'color' | 'weight' | 'fontStyle' | 'decoration'>;

const BaseText = styled.p<StyledTextProps>`
  color: ${(props) => props.color || THEME.color.charcoal};
  font-weight: ${(props) => fontWeights[props.weight || 'normal']};
  font-style: ${(props) => props.fontStyle || 'normal'};
  text-decoration: ${(props) => props.decoration || 'none'};
  letter-spacing: 0px;
  text-align: left;
  line-spacing: 18px;
  character-spacing: 0;
`;

const H1 = styled(BaseText)<{ fontSize: number }>`
  font-size: ${(props) => props.fontSize}px;
  font-weight: bold;
`;

const H2 = styled(BaseText)<{ fontSize: number }>`
  font-size: ${(props) => props.fontSize}px;
  font-weight: bold;
`;

const H3 = styled(BaseText)<{ fontSize: number }>`
  font-size: ${(props) => props.fontSize}px;
`;

const H4 = styled(BaseText)<{ fontSize: number }>`
  font-size: ${(props) => props.fontSize}px;
`;

const TextComponentMap: Record<TextProps['as'], FC<StyledTextProps & { fontSize: number }>> = {
    h1: H1,
    h2: H2,
    h3: H3,
    h4: H4
};

const Text: FC<TextProps> = ({children, as, color, weight, fontStyle, decoration, ...rest}) => {
    const fontSize = as === 'h1' ? 22 : as === 'h2' ? 16 : as === 'h3' ? 13 : 11;
    const StyledComponent = TextComponentMap[as];

    if (!StyledComponent) {
        return null;
    }

    return (
        <StyledComponent
            color={color}
            weight={weight}
            fontStyle={fontStyle}
            decoration={decoration}
            fontSize={fontSize}
            {...rest}
        >
            {children}
        </StyledComponent>
    );
};

export default Text;
