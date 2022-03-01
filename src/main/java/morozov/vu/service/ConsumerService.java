package morozov.vu.service;

import morozov.vu.domain.ShopTwo;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ConsumerService {

    @KafkaListener(topics = "topic")
    public void orderListener(ConsumerRecord<Long, ShopTwo> record) {
        System.out.println(record.partition());
        System.out.println(record.key());
        System.out.println(record.value());
    }
}
