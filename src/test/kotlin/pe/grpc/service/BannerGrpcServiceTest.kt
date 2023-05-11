package pe.grpc.service

import io.grpc.ManagedChannelBuilder
import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import org.springframework.boot.test.context.SpringBootTest
import pe.grpc.Banner
import pe.grpc.BannerServiceGrpcKt

@SpringBootTest
internal class BannerGrpcServiceTest: DescribeSpec() {

    init {
        describe("BannerGrpcService") {
            context("BannerGrpc 호출") {
                it("조회한 결과를 노출") {
                    val req = Banner.BannerRequest.newBuilder()
                        .setBannerType("HOME")
                        //.setBannerCode("BN0101")
                        .build()
                    val response = stub.getBanner(req)

                    assertSoftly {
                        response.contents.bannerType shouldBe "HOME"
                        response.contents.imgUrl shouldContain "https://"
                    }
                    println("===> response $response")
                }
            }
        }
    }

    companion object {
        private val channel = ManagedChannelBuilder.forTarget("localhost:9090").usePlaintext().build()
        private val stub = BannerServiceGrpcKt.BannerServiceCoroutineStub(channel)
    }
}
