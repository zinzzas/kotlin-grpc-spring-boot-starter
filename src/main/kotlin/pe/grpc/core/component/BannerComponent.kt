package pe.grpc.core.component

import org.springframework.stereotype.Component
import pe.grpc.core.domain.Banner
import pe.grpc.core.domain.BannerRepository

@Component
class BannerComponent(
    private val repository: BannerRepository
) {
    suspend fun getBannerById(id: String): Banner? {
        return repository.findById(id)
    }
}