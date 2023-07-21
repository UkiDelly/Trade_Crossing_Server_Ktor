package ukidelly.plugins

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.connection.ConnectionPoolSettings
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ukidelly.routing.user.userRouting

fun Application.configureRouting() {


    routing {

        val connectionUrl = ConnectionString(environment!!.config.property("mongodb.connectionUrl").getString())
        val connectionPool = ConnectionPoolSettings.builder().applyConnectionString(connectionUrl)

        // MongoDB 클라이언트 생성
        val mongoClientSetting =
            MongoClientSettings.builder().applyConnectionString(connectionUrl)
                .applyToConnectionPoolSettings { connectionPool }
                .build()

        get("/") {
            call.respondText("Hello World!")
        }

        route("/user") {


            userRouting(mongoClientSetting)
        }

    }
}
