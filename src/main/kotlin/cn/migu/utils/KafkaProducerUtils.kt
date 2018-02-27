package cn.migu.utils

import cn.migu.dda.vo.KafkaSink
import java.util.*
import org.apache.kafka.common.serialization.StringSerializer
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.common.KafkaException
import org.apache.kafka.common.errors.AuthorizationException
import org.apache.kafka.common.errors.OutOfOrderSequenceException
import org.apache.kafka.common.errors.ProducerFencedException
import org.apache.kafka.clients.producer.ProducerRecord
import java.io.File


class KafkaProducerUtils {

    fun dda4aKfkaProducer(kafkaSink: KafkaSink, filePath: String) {

        val sinks = kafkaSink.topic.split(",")

        for (sink in sinks) {
            val props = Properties()
            props.put("bootstrap.servers", kafkaSink.bootServr)
            props.put("transactional.id", System.currentTimeMillis())
            val producer = KafkaProducer(props, StringSerializer(), StringSerializer())
            producer.initTransactions()
            try {
                producer.beginTransaction()
                val file = File(filePath)
                file.forEachLine {
                    producer.send(ProducerRecord(sink, "", it))
                }
                producer.commitTransaction()
            } catch (e: ProducerFencedException) {
                producer.close()
            } catch (e: OutOfOrderSequenceException) {
                producer.close()
            } catch (e: AuthorizationException) {
                producer.close()
            } catch (e: KafkaException) {
                producer.abortTransaction()
            }
            producer.close()
        }
    }
}