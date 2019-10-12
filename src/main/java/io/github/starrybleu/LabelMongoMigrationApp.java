package io.github.starrybleu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@EnableJpaAuditing
@SpringBootApplication
public class LabelMongoMigrationApp {

    public static void main(String[] args) {
        SpringApplication.run(LabelMongoMigrationApp.class, args);
    }

    @RestController
    static class MigrationController {
        private MigrateService migrateService;

        MigrationController(MigrateService migrateService) {
            this.migrateService = migrateService;
        }

        @GetMapping("/api/labels/migrate")
        public Object startMigration() {
            return migrateService.migrateLabelDataFromCsvs();
        }


    }

}
