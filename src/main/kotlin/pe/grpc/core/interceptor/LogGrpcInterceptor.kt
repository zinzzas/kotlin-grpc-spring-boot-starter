package pe.grpc.core.interceptor

import io.grpc.*
import net.devh.boot.grpc.client.interceptor.GrpcGlobalClientInterceptor
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@GrpcGlobalClientInterceptor
class LogGrpcInterceptor : ClientInterceptor {
    private val log: Logger = LoggerFactory.getLogger(LogGrpcInterceptor::class.java)

    override fun <ReqT : Any?, RespT : Any?> interceptCall(method: MethodDescriptor<ReqT, RespT>?, callOptions: CallOptions?, next: Channel?): ClientCall<ReqT, RespT> {
        log.info("Received call to ${method?.fullMethodName}")
        return object : ForwardingClientCall.SimpleForwardingClientCall<ReqT, RespT>(next!!.newCall(method, callOptions)) {

            override fun sendMessage(message: ReqT) {
                log.debug("Request message: $message")
                super.sendMessage(message)
            }

            override fun start(responseListener: Listener<RespT>?, headers: Metadata?) {
                super.start( object : ForwardingClientCallListener.SimpleForwardingClientCallListener<RespT>(responseListener) {
                    override fun onMessage(message: RespT) {
                        log.debug("Response message: $message")
                        super.onMessage(message)
                    }

                    override fun onHeaders(headers: Metadata?) {
                        log.debug("gRPC headers: $headers")
                        super.onHeaders(headers)
                    }

                    override fun onClose(status: Status?, trailers: Metadata?) {
                        log.info("Interaction ends with status: $status")
                        log.info("Trailers: $trailers")
                        super.onClose(status, trailers)
                    }
                }
                , headers)
            }
        }
    }
}