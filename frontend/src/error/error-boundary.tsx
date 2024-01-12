import { Component } from 'react'

import { FrontendError } from './frontend-error.ts'

import type { ErrorInfo, ReactNode } from 'react'

export type FrontendErrorBoundaryProps = {
    children: ReactNode
}
export type FrontendErrorBoundaryState = {
    hasError: boolean
    isHandled: boolean
    message: string | undefined
}

export class FrontendErrorBoundary extends Component<FrontendErrorBoundaryProps, FrontendErrorBoundaryState> {
    constructor(props: FrontendErrorBoundaryProps) {
        super(props)

        this.state = {
            hasError: false,
            isHandled: false,
            message: undefined
        }
    }

    static getDerivedStateFromError(error: any) {
        if (error instanceof FrontendError) {
            return {
                hasError: true,
                isHandled: true,
                message: error.message
            }
        }

        return {
            hasError: true,
            isHandled: false,
            message: String(error)
        }
    }

    override componentDidCatch(error: Error, errorInfo: ErrorInfo) {
        if (error instanceof FrontendError) {
            console.group('FrontendErrorBoundary > Handled Error')
            console.debug('error', error)
            console.debug('originalError', error.originalError)
            console.groupEnd()
        } else {
            console.group('FrontendErrorBoundary > Unhandled Error')
            console.debug('error', error)
            console.debug('errorInfo', errorInfo)
            console.groupEnd()
        }
    }

    override render() {
        const {children} = this.props
        const {hasError, message} = this.state

        if (hasError) {
            return (
                <div
                    style={{
                        alignItems: 'center',
                        display: 'flex',
                        flexGrow: 1,
                        height: '100%',
                        justifyContent: 'center',
                        width: '100%'
                    }}
                >
                    Une erreur est survenue.
                    Essayez de recharger la page.
                    Si l'erreur persiste, veuillez contacter l'équipe RapportNav avec une capture d'écran.
                    {message ? `Unhandled Exception: ${message}.` : ''}
                </div>
            )
        }

        return children
    }
}
