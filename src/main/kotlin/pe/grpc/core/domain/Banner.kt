package pe.grpc.core.domain

data class Banner(
    var bannerType: String = "",
    var bannerCode: String = "",
    var bannerContents: List<BannerContents> = mutableListOf(),
) {
    data class BannerContents(
        var poc: String = "",
        var imgUrl: String = "",
        var textval: String = "",
        var subText: String = "",
    )
}
