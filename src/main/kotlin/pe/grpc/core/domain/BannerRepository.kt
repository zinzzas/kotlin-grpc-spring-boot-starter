package pe.grpc.core.domain

import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface BannerRepository : CoroutineCrudRepository<Banner, String>