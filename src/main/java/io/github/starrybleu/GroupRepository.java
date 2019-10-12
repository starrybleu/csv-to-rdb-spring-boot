package io.github.starrybleu;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<LabelGroup, Long> {
}
