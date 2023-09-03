package ukidelly.modules

import io.ktor.server.application.*
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import ukidelly.api.v1.feed.repository.FeedRepository
import ukidelly.api.v1.feed.service.FeedService
import ukidelly.api.v1.trade_post.comment.service.TradePostCommentService
import ukidelly.api.v1.trade_post.repository.TradePostRepository
import ukidelly.api.v1.trade_post.service.TradePostService
import ukidelly.api.v1.user.repository.UserRepository
import ukidelly.api.v1.user.service.UserService
import ukidelly.database.DataBaseFactory
import ukidelly.database.models.comment.TradePostCommentRepository

fun Application.configureKoin() {

    val databaseModule = module {
        single { DataBaseFactory }
        single { SupabaseClient() }
    }

    val repositoryModule = module {
        single { UserRepository() }
        single { TradePostRepository() }
        single { TradePostCommentRepository() }
        single { FeedRepository() }
    }

    val serviceModule = module {

        single { UserService() }
        single { TradePostService() }
        single { TradePostCommentService() }
        single { FeedService() }
    }

    install(Koin) {
        modules(databaseModule, repositoryModule, serviceModule)
    }
}