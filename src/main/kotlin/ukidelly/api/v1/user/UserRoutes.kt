package ukidelly.api.v1.user

import io.ktor.resources.*


@Resource("/user")
class UserRoutes {


    @Resource("login")
    class Login(val parent: UserRoutes = UserRoutes()) {


        @Resource("email")
        class EmailLogin(val parent: Login = Login())

        @Resource("social")
        class SocialLogin(val parent: Login = Login())

        @Resource("auto")
        class Auto(val parent: Login = Login())
    }


    @Resource("refresh-token")
    class RefreshToken(val parent: UserRoutes = UserRoutes())

    @Resource("register")
    class Register(val parent: UserRoutes = UserRoutes())


}