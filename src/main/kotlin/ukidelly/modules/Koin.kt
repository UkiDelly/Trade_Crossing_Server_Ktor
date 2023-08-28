package ukidelly.modules

import io.ktor.server.application.*
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import ukidelly.api.v1.comment.service.CommentService
import ukidelly.api.v1.post.repository.PostRepository
import ukidelly.api.v1.post.service.PostService
import ukidelly.api.v1.user.repository.UserRepository
import ukidelly.api.v1.user.service.UserService
import ukidelly.database.DataBaseFactory
import ukidelly.database.models.comment.CommentRepository

fun Application.configureKoin() {

	val databaseModule = module {
		single { DataBaseFactory }
	}

	val repositoryModule = module {
		single { UserRepository() }
		single { PostRepository() }
		single { CommentRepository() }
	}

	val serviceModule = module {

		single { UserService() }
		single { PostService() }
		single { CommentService() }
	}

	install(Koin) {
		modules(databaseModule, repositoryModule, serviceModule)
	}
}