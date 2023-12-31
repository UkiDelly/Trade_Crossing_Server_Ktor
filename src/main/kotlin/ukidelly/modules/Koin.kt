package ukidelly.modules

import io.ktor.server.application.*
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import ukidelly.repositories.*
import ukidelly.services.*

fun Application.configureKoin() {

  val databaseModule = module {
    single { SupabaseServerClient(environment.config) }
  }

  val repositoryModule = module {
    single { UserRepository() }
    single { TradeFeedRepository() }
    single { TradeFeedCommentRepository() }
    single { FeedRepository() }
    single { FeedCommentRepository() }
  }


  val serviceModule = module {
    single { UserService(get<UserRepository>()) }
    single { TradeFeedCommentService(get<TradeFeedCommentRepository>()) }
    single { TradeFeedService(get<TradeFeedRepository>(), get<TradeFeedCommentService>()) }
    single { FeedService(get<FeedRepository>()) }
    single { FeedCommentService(get<FeedCommentRepository>()) }
  }

  install(Koin) {
    allowOverride(true)
    modules(databaseModule, repositoryModule, serviceModule)
  }
}