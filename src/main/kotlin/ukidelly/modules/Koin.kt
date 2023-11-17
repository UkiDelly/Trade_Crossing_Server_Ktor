package ukidelly.modules

import io.ktor.server.application.*
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import ukidelly.repositories.FeedRepository
import ukidelly.repositories.TradeFeedCommentRepository
import ukidelly.repositories.TradeFeedRepository
import ukidelly.repositories.UserRepository
import ukidelly.services.FeedService
import ukidelly.services.TradeFeedCommentService
import ukidelly.services.TradeFeedService
import ukidelly.services.UserService

fun Application.configureKoin() {

    val databaseModule = module {
        single { SupabaseServerClient(environment.config) }
    }

    val repositoryModule = module {
        single { UserRepository() }
        single { TradeFeedRepository() }
        single { TradeFeedCommentRepository() }
        single { FeedRepository() }
    }


    val serviceModule = module {
        single { UserService(get<UserRepository>()) }
        single { TradeFeedCommentService(get<TradeFeedCommentRepository>()) }
        single { TradeFeedService(get<TradeFeedRepository>(), get<TradeFeedCommentService>()) }
        single { FeedService(get<FeedRepository>()) }
    }

    install(Koin) {
        allowOverride(true)
        modules(databaseModule, repositoryModule, serviceModule)
    }
}