package ukidelly.modules

import io.ktor.server.application.*
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import ukidelly.api.v1.post.service.PostService
import ukidelly.api.v1.user.service.UserService
import ukidelly.database.DataBaseFactory

fun Application.configureKoin() {

    val databaseModule = module {
        single { DataBaseFactory }
    }

    val serviceModule = module {
        single { UserService() }
        single { PostService() }

    }

    install(Koin) {
        modules(databaseModule, serviceModule)
    }
}