package com.pivovarit.blackbox;

import io.restassured.http.ContentType;
import org.jdbi.v3.core.Jdbi;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;

@Testcontainers
class BlackboxTest {

    private static final Logger log = LoggerFactory.getLogger(BlackboxTest.class);

    private static final Network network = Network.newNetwork();

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
    static ApplicationContainer app = new ApplicationContainer()
      .dependsOn(postgres)
      .withNetwork(network)
      .withEnv("APPLICATION_PROFILE", "prod")
      .withEnv("POSTGRES_URL", "jdbc:postgresql://postgres:5432/postgres")
      .withEnv("POSTGRES_USER", "postgres")
      .withEnv("POSTGRES_PASSWORD", "password")
      .withExposedPorts(8080)
      .withLogConsumer(new Slf4jLogConsumer(log).withPrefix("rental-store"))
      .waitingFor(Wait.forHttp("/health").forStatusCode(200));

    @Test
    void shouldRun() throws Exception {
        given()
          .port(app.getMappedPort(8080))
          .when()
          .get("/health")
          .then()
          .statusCode(200);
    }

    @Test
    void shouldCreateMovie() {
        given()
          .port(app.getMappedPort(8080))
          .contentType(ContentType.JSON)
          .body("""
            {"id": 42, "title": "The Matrix", "type": "REGULAR"}
            """)
          .when()
          .post("/movies")
          .then()
          .statusCode(200);

        assertThat(Jdbi.create(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword())
          .<List<Map<String, Object>>, RuntimeException>withHandle(handle -> handle
          .createQuery("SELECT id, title, type FROM movies WHERE id = :id")
          .bind("id", 42L)
          .mapToMap()
          .list())).containsExactly(Map.of(
          "id", 42L,
          "title", "The Matrix",
          "type", "REGULAR"));

        given()
          .port(app.getMappedPort(8080))
          .when()
          .get("/movies/42")
          .then()
          .statusCode(200)
          .body("id", equalTo(42))
          .body("title", equalTo("The Matrix"))
          .body("type", equalTo("REGULAR"))
          .body("summary", equalTo("A skilled thief who steals secrets through dream-sharing technology is given a chance to have his criminal history erased by planting an idea into a target's subconscious."));

        given()
          .port(app.getMappedPort(8080))
          .when()
          .get("/movies")
          .then()
          .statusCode(200)
          .body("title", hasItem("The Matrix"));
    }

    private static class ApplicationContainer extends GenericContainer<ApplicationContainer> {
        ApplicationContainer() {
            super(DockerImageName.parse("rental-store:snapshot"));
        }
    }
}
