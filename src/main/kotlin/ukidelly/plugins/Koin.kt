package ukidelly.plugins

import io.ktor.server.application.*
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import ukidelly.repository.UserRepository
import ukidelly.service.UserService

fun Application.configureKoin() {

    val userModule = module {
        single { UserService() }
        single { UserRepository() }


    }

    install(Koin) {
        modules(userModule)
    }
}