package ukidelly.modules

import io.ktor.server.application.*
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import ukidelly.api.v1.user.service.UserService
import ukidelly.database.DataBaseFactory
import ukidelly.database.models.user.UserDao

fun Application.configureKoin() {

    val databaseModule = module {
        single { DataBaseFactory }
    }

    val userModule = module {
        single { UserService() }
        single { UserDao() }

    }

    install(Koin) {
        modules(userModule, databaseModule)
    }
}