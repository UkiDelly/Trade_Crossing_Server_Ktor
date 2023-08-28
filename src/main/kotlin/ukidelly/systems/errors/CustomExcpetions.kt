package ukidelly.systems.errors

class InvalidJwtTokenException : RuntimeException()

class UserExistException(message: String) : RuntimeException(message)
class PasswordIncorrectException : RuntimeException("비밀번호가 일치하지 않습니다.")