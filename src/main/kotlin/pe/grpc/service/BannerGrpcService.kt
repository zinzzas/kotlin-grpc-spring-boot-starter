package pe.grpc.service

import com.google.protobuf.Descriptors
import net.devh.boot.grpc.server.service.GrpcService
import org.springframework.beans.factory.annotation.Autowired
import pe.grpc.Banner
import pe.grpc.BannerServiceGrpcKt
import pe.grpc.core.component.BannerComponent

@GrpcService
class BannerGrpcService @Autowired constructor(private val component: BannerComponent): BannerServiceGrpcKt.BannerServiceCoroutineImplBase() {

    override suspend fun findOne(request: Banner.BannerRequest): Banner.BannerResponse {

        //component.getBannerById(request.bannerType)

        return Banner.BannerResponse
            .newBuilder()
            .setBannerType("HOME")
            .setBannerCode("B001")
            .addBannerContents(
                Banner.BannerContents.newBuilder()
                    .setPoc("PC")
                    .setImgUrl("https://s.pstatic.net/static/www/mobile/edit/20230322/mobile_143613613799.gif")
                    .setText("홈 배너")
                    .setSubText("메인 배너입니다.")
            )
            .build()

    }
}