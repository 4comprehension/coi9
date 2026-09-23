package com.pivovarit.blackbox;

import io.restassured.http.ContentType;
import org.jdbi.v3.core.Jdbi;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
class BlackboxTest {

    private static final Logger log = LoggerFactory.getLogger(BlackboxTest.class);

    private static final Network network = Network.newNetwork();

    public static final int APP_PORT = 8080;

    private static final String QUEUE_SUMMARY_UPDATED = "movie-summary.updated.queue";

    private static RabbitTemplate rabbitTemplate;

    @Container
    static final PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:18")
      .withNetwork(network)
      .withNetworkAliases("postgres")
      .withDatabaseName("postgres")
      .withUsername("postgres")
      .withPassword("password")
      .withLogConsumer(new Slf4jLogConsumer(log).withPrefix("postgres"))
      .waitingFor(Wait.forListeningPort());

    @Container
    static final RabbitMqContainer rabbitmq = new RabbitMqContainer()
      .withNetwork(network)
      .withNetworkAliases("rabbitmq")
      .withExposedPorts(5672)
      .withLogConsumer(new Slf4jLogConsumer(log).withPrefix("rabbitmq"))
      .waitingFor(Wait.forListeningPort());

    @Container
    static ApplicationContainer app = new ApplicationContainer()
      .dependsOn(postgres, rabbitmq)
      .withNetwork(network)
      .withEnv("APPLICATION_PROFILE", "prod")
      .withEnv("POSTGRES_URL", "jdbc:postgresql://postgres:5432/postgres")
      .withEnv("POSTGRES_USER", "postgres")
      .withEnv("POSTGRES_PASSWORD", "password")
      .withEnv("RABBITMQ_HOST", "rabbitmq")
      .withExposedPorts(APP_PORT)
      .withLogConsumer(new Slf4jLogConsumer(log).withPrefix("movie-summaries"))
      .waitingFor(Wait.forHttp("/health").forStatusCode(200));

    @BeforeAll
    static void setUpRabbitTemplate() {
        var connectionFactory = new CachingConnectionFactory(rabbitmq.getHost(), rabbitmq.getMappedPort(5672));
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setReceiveTimeout(5000);
    }

    @Test
    void shouldRun() {
        given()
          .port(app.getMappedPort(APP_PORT))
          .when()
          .get("/health")
          .then()
          .statusCode(200);
    }

    @Test
    void shouldCreateThenUpdateSummary() {
        given()
          .port(app.getMappedPort(APP_PORT))
          .contentType(ContentType.JSON)
          .body("""
            {"summary": "an inception summary"}
            """)
          .when()
          .post("/summaries/42")
          .then()
          .statusCode(201);

        assertThat(Jdbi.create(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword())
          .<List<Map<String, Object>>, RuntimeException>withHandle(handle -> handle
            .createQuery("SELECT movie_id, summary FROM movie_summaries WHERE movie_id = :id")
            .bind("id", 42L)
            .mapToMap()
            .list())).containsExactly(Map.of(
          "movie_id", 42L,
          "summary", "an inception summary"));

        assertPublishedSummaryUpdatedEvent(42L, "an inception summary", 1L);

        given()
          .port(app.getMappedPort(APP_PORT))
          .contentType(ContentType.JSON)
          .body("""
            {"summary": "a revised inception summary"}
            """)
          .when()
          .post("/summaries/42")
          .then()
          .statusCode(200);

        assertThat(Jdbi.create(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword())
          .<String, RuntimeException>withHandle(handle -> handle
            .createQuery("SELECT summary FROM movie_summaries WHERE movie_id = :id")
            .bind("id", 42L)
            .mapTo(String.class)
            .one()))
          .isEqualTo("a revised inception summary");

        assertPublishedSummaryUpdatedEvent(42L, "a revised inception summary", 2L);
    }

    private void assertPublishedSummaryUpdatedEvent(long expectedMovieId, String expectedSummary, long expectedVersion) {
        var message = rabbitTemplate.receive(QUEUE_SUMMARY_UPDATED, 5000);

        assertThat(message).isNotNull();

        var event = new ObjectMapper().readValue(message.getBody(), Map.class);
        assertThat(((Number) event.get("movieId")).longValue()).isEqualTo(expectedMovieId);
        assertThat(event.get("summary")).isEqualTo(expectedSummary);
        assertThat(((Number) event.get("version")).longValue()).isEqualTo(expectedVersion);
    }

    private static class ApplicationContainer extends GenericContainer<ApplicationContainer> {
        ApplicationContainer() {
            super(DockerImageName.parse("movie-summaries:snapshot"));
        }
    }

    private static class RabbitMqContainer extends GenericContainer<RabbitMqContainer> {
        RabbitMqContainer() {
            super(DockerImageName.parse("rabbitmq:3-management"));
        }
    }
}
