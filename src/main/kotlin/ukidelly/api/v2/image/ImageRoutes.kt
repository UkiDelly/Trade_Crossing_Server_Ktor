package ukidelly.api.v2.image

import io.ktor.resources.*


@Resource("/image")
class ImageRoutes {

    @Resource("upload")
    class Upload(val parent: ImageRoutes = ImageRoutes())
}