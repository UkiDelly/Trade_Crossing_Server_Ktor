package ukidelly.routing.user

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoClients
import com.mongodb.client.model.Filters.and
import com.mongodb.client.model.Filters.eq
import com.mongodb.connection.ConnectionPoolSettings
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import org.litote.kmongo.findOne
import org.slf4j.LoggerFactory
import ukidelly.domain.system.ResponseModel
import ukidelly.domain.system.Token
import ukidelly.domain.user.LoginMethod
import ukidelly.domain.user.User
import ukidelly.domain.user.UserLoginRequest
import ukidelly.domain.user.UserLoginResponse
import java.time.Instant
import java.time.LocalDateTime


fun Route.userRouting() {

    val logger = LoggerFactory.getLogger("UserRouting")

    val connectionUrl = ConnectionString(environment!!.config.property("mongodb.connectionUrl").getString())
    val database = environment!!.config.property("mongodb.database").getString()
    val connectionPool = ConnectionPoolSettings.builder().applyConnectionString(connectionUrl)
    val mongoClientSetting =
        MongoClientSettings.builder().applyConnectionString(connectionUrl)
            .applyToConnectionPoolSettings { connectionPool }
            .build()

    val jwtAudience = environment!!.config.property("jwt.audience").getString()//"jwt-audience"
    val jwtDomain = environment!!.config.property("jwt.domain").getString() //"https://jwt-provider-domain/"
    val jwtRealm = environment!!.config.property("jwt.realm").getString()
    val jwtSecret = environment!!.config.property("jwt.secret").getString()


    //
    get("login") {

        // MongoDB 클라이언트 생성
        val mongoClient = MongoClients.create(mongoClientSetting)


        // user라는 이름의 collection을 가져온다.
        val userCollection = withContext(Dispatchers.IO) {
            val mongoDatabase = mongoClient.getDatabase(database)
            mongoDatabase.getCollection("user")
        }

        // 요청의 body를 가져와서 UserLoginRequestModel로 변환
        val request = call.receive<UserLoginRequest>()

        logger.debug("request: {}", request)

        // 필터 생성
        val filter = and(
            eq("email", request.email),
            eq("snsId", request.snsId)
        )

        // DB에서 유저 찾기
        val userDoc = withContext(Dispatchers.IO) {
            userCollection.findOne(filter)
        }

        // 유저가 존재 하지 않을때
        if (userDoc == null) {
            call.respond(status = HttpStatusCode.NotFound, "존재하지 않는 유저입니다")
            return@get
        }

        val user = User.fromDocument(userDoc)

        // jwt 생성
        val now = Instant.now()


        // add 5 minutes
        var expiredAt = now.plusSeconds(60 * 5)
        val accessToken = JWT.create()
            .withIssuer(jwtDomain)
            .withAudience(jwtAudience)
            .withClaim("userId", user.id.toString())
            .withExpiresAt(expiredAt)
            .sign(Algorithm.HMAC256(jwtSecret))

        expiredAt = now.plusSeconds(60 * 60 * 24 * 30)

        val refreshToken = JWT.create()
            .withIssuer(jwtDomain)
            .withAudience(jwtAudience)
            .withExpiresAt(expiredAt)
            .sign(Algorithm.HMAC256(jwtSecret))

        val token = Token(accessToken, refreshToken)


        call.respond(
            HttpStatusCode.OK, ResponseModel(
                UserLoginResponse(
                    user,
                    token
                ), "로그인 성공"
            )
        )
        mongoClient.close()
    }



    post("register") {

        val mongoClient = MongoClients.create(mongoClientSetting)
        val userCollection = withContext(Dispatchers.IO) {
            val mongoDatabase = mongoClient.getDatabase(database)
            mongoDatabase.getCollection("user")
        }

        val testUser = User(
            email = "test2@google.com",
            snsId = "test2",
            loginMethod = LoginMethod.google,
            userName = "test2",
            islandName = "test2Island",
            introduction = "test2",
            createdAt = LocalDateTime.now().toString(),
        )

        userCollection.insertOne(testUser.toDocument())

        val r = ResponseModel(testUser, "회원가입 성공")

        val json = Json { encodeDefaults = true }

        println(json.encodeToString(ResponseModel.serializer(User.serializer()), r))

        call.respond(HttpStatusCode.OK, ResponseModel<User>(testUser, "회원가입 성공"))
        mongoClient.close()
    }

    //
    authenticate("refresh-jwt") {
        //
        post("refresh") { }

    }


}