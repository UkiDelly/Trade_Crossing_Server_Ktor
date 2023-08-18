package ukidelly.systems.models

import kotlinx.serialization.Serializable

@Serializable
enum class LoginType {
	GOOGLE, KAKAO, APPLE
}


@Serializable
enum class DefaultProfile { NOOK, ISABELL, NO }

enum class ServerMode {
	DEV, PROD
}

@Serializable
enum class Currency {
	bell, mileage, free
}

@Serializable
enum class PostCategory { item, turnip, manjijak }