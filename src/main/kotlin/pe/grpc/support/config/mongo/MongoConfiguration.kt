package pe.grpc.support.config.mongo

import com.mongodb.*
import com.mongodb.connection.ConnectionPoolSettings
import com.mongodb.connection.SocketSettings
import org.springframework.context.annotation.Bean
import org.springframework.data.mapping.model.SnakeCaseFieldNamingStrategy
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper
import org.springframework.data.mongodb.core.convert.MappingMongoConverter
import org.springframework.data.mongodb.core.mapping.MongoMappingContext
import java.util.concurrent.TimeUnit

interface MongoConfiguration {

    fun mongoClientSettings(properties: MongoDBProperties, poolSettings: Block<ConnectionPoolSettings.Builder>?): MongoClientSettings {
        val connectionString = ConnectionString(properties.uri!!)
        val credential = MongoCredential.createCredential(
            properties.username ?: "",
            properties.getInfo(MongoDBProperties.DatabaseType.TEST)!!.database.toString(),
            properties.password?.toCharArray() ?: charArrayOf()
        )
        val builder = MongoClientSettings.builder()
            .retryReads(false)
            .retryWrites(false)
            .writeConcern(WriteConcern.ACKNOWLEDGED)
            .readPreference(ReadPreference.secondaryPreferred())
            .applyConnectionString(connectionString)
            .applyToConnectionPoolSettings(poolSettings!!)
            .applyToSocketSettings { x: SocketSettings.Builder ->
                x.connectTimeout(3000, TimeUnit.MILLISECONDS)
                x.readTimeout(2000, TimeUnit.MILLISECONDS)
            }

        if (isCredential(properties)) {
            builder.credential(credential)
        }

        return builder.build()
    }

    fun defaultPoolSettings(): Block<ConnectionPoolSettings.Builder>? {
        return Block { x: ConnectionPoolSettings.Builder ->
            x.maxSize(100)
            x.maxWaitTime(5000, TimeUnit.MILLISECONDS)
            x.maxConnectionIdleTime(10000, TimeUnit.MILLISECONDS)
        }
    }

    fun generalPoolSettings(): Block<ConnectionPoolSettings.Builder>? {
        return Block { x: ConnectionPoolSettings.Builder ->
            x.maxSize(50)
            x.maxWaitTime(5000, TimeUnit.MILLISECONDS) // 10000 -> 5000
        }
    }

    @Bean("mongoConverter")
    fun mongoConverter(): MappingMongoConverter {
        val mongoMappingContext = MongoMappingContext()
        mongoMappingContext.setFieldNamingStrategy(SnakeCaseFieldNamingStrategy())

        val converter =  MappingMongoConverter(ReactiveMongoTemplate.NO_OP_REF_RESOLVER, mongoMappingContext)

        // collection _class 필드를 제거 하는 설정
        converter.setTypeMapper(DefaultMongoTypeMapper(null))
        return converter
    }

    private fun isCredential(properties: MongoDBProperties): Boolean {
        return !(properties.username.isNullOrEmpty() || properties.password.isNullOrEmpty())
    }
}