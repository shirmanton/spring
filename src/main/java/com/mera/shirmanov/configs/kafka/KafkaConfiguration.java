package com.mera.shirmanov.configs.kafka;

import com.mera.shirmanov.avro.Customer;
import com.mera.shirmanov.configs.ApplicationProperties;
import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import io.confluent.kafka.serializers.subject.RecordNameStrategy;
import lombok.AllArgsConstructor;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
@AllArgsConstructor
public class KafkaConfiguration {

	private final ApplicationProperties properties;

	@Bean
	public ProducerFactory<String, Customer> producerFactory() {
		return new DefaultKafkaProducerFactory<>(producerConfigs());
	}

	@Bean
	public Map<String, Object> producerConfigs() {
		Map<String, Object> configs = new HashMap<>();
		configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, properties.getKafka().getBootstrapServers());
		configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);
		configs.put(AbstractKafkaAvroSerDeConfig.VALUE_SUBJECT_NAME_STRATEGY, RecordNameStrategy.class);
		configs.put(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, properties.getKafka().getSchemaRegistryUrl());
		configs.put(AbstractKafkaAvroSerDeConfig.AUTO_REGISTER_SCHEMAS, properties.getKafka().isAutoRegisterSchemas());
		return configs;
	}

	@Bean
	public KafkaTemplate<String, Customer> kafkaTemplate() {
		return new KafkaTemplate<>(producerFactory());
	}

	@Bean
	public KafkaAdmin kafkaAdmin() {
		return new KafkaAdmin(adminClientConfigs());
	}

	@Bean
	public Map<String, Object> adminClientConfigs() {
		Map<String, Object> configs = new HashMap<>();
		configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, properties.getKafka().getBootstrapServers());
		return configs;
	}

	@Bean
	public NewTopic topic() {
		return TopicBuilder.name(properties.getKafka().getTopic().getName())
				.partitions(properties.getKafka().getTopic().getPartitions())
				.replicas(properties.getKafka().getTopic().getReplicas())
				.compact()
				.build();
	}
}
