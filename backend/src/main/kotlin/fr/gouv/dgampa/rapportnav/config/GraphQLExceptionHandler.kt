package fr.gouv.dgampa.rapportnav.config

import graphql.ErrorType
import graphql.GraphQLError
import graphql.GraphqlErrorBuilder
import graphql.schema.DataFetchingEnvironment
import org.slf4j.LoggerFactory
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter
import org.springframework.stereotype.Component

@Component
class GraphQLExceptionHandler : DataFetcherExceptionResolverAdapter() {
    companion object {
        private val log = LoggerFactory.getLogger(this::class.java)
    }

    override fun resolveToSingleError(e: Throwable, env: DataFetchingEnvironment): GraphQLError? {
        return when (e) {
            // TODO add more cases
            is Exception -> toGraphQLError(e, env)
            else -> super.resolveToSingleError(e, env)
        }
    }

    private fun toGraphQLError(e: Throwable, env: DataFetchingEnvironment): GraphQLError? {
        log.warn("Exception while handling request: ${e.message}", e)
        return GraphqlErrorBuilder.newError()
            .message(e.message)
            .errorType(ErrorType.DataFetchingException)
            .path(env.executionStepInfo.path)
            .location(env.field.sourceLocation)
            .build()
    }


}
