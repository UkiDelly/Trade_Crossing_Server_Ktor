package ukidelly.modules

import io.ktor.server.application.*
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import ukidelly.api.v1.feed.repository.FeedRepository
import ukidelly.api.v1.feed.service.FeedService
import ukidelly.api.v1.trade_post.comment.service.TradeFeedCommentService
import ukidelly.api.v1.trade_post.repository.TradeFeedRepository
import ukidelly.api.v1.trade_post.service.TradeFeedService
import ukidelly.api.v1.user.repository.UserRepository
import ukidelly.api.v1.user.service.UserService
import ukidelly.database.models.comment.TradeFeedCommentRepository

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