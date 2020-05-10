package com.mera.shirmanov.configs;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "app")
public class ApplicationProperties {

	private Kafka kafka;

	@Data
	public static class Kafka {
		private String bootstrapServers;
		private String schemaRegistryUrl;
		private boolean autoRegisterSchemas;
		private Topic topic;

		@Data
		public static class Topic {
			private String name;
			private String customerKey;
			private int partitions;
			private int replicas;
		}
	}
}
