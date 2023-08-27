package ukidelly.systems.models

import kotlinx.serialization.Serializable

@Serializable
enum class LoginType {
	google, kakao, apple, email
}


@Serializable
enum class DefaultProfile { nook, isabella, no }

enum class ServerMode {
	dev, prod
}

@Serializable
enum class Currency {
	bell, mileage, free
}

@Serializable
enum class PostCategory { item, turnip, manjijak }