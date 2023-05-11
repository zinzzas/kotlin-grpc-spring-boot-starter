package pe.grpc.core.domain

data class Banner(
    var contents: List<Contents> = mutableListOf(),
) {
    data class Contents(
        var bannerType: String = "",
        var bannerCode: String = "",
        var poc: String = "",
        var imgUrl: String = "",
        var textval: String = "",
        var subText: String = "",
    )
}
