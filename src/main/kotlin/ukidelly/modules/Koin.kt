package ukidelly.modules

import io.ktor.server.application.*
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import ukidelly.api.v1.user.repository.UserRepository
import ukidelly.api.v1.user.service.UserService

fun Application.configureKoin() {


//    val databaseModule = module {
//        single {
//
//        }
//    }

    val userModule = module {
        single { UserService(get()) }
        single { UserRepository() }

    }

    install(Koin) {
        modules(userModule)
    }
}