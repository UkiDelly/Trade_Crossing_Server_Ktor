package ukidelly.plugins

import io.ktor.server.application.*
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.ktorm.database.Database
import org.ktorm.logging.ConsoleLogger
import org.ktorm.logging.LogLevel
import ukidelly.user.repository.UserRepository
import ukidelly.user.service.UserService

fun Application.configureKoin() {

    val systemModule = module {
        single {
            Database.connect(
                url = environment.config.property("supabase.url").getString(),
                driver = "org.postgresql.Driver",
                user = environment.config.property("supabase.username").getString(),
                password = environment.config.property("supabase.password").getString(),
                logger = ConsoleLogger(threshold = LogLevel.INFO)
            )
        }
    }

    val userModule = module {
        single { UserService() }
        single { UserRepository() }

    }

    install(Koin) {
        modules(userModule, systemModule)
    }
}