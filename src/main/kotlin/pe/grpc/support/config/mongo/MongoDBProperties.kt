package pe.grpc.support.config.mongo

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("mongodb")
data class MongoDBProperties (
    val uri: String,
    val info: Map<DatabaseType, Info>? = null) {

    class Info(var database: String?)

    fun getInfo(type: DatabaseType): Info? {
        return info!![type]
    }

    enum class DatabaseType {
       TEST
    }
}
