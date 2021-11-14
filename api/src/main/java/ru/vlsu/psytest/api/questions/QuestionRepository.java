package ru.vlsu.psytest.api.questions;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.vlsu.psytest.api.questions.Question;

public interface QuestionRepository extends JpaRepository <Question, Integer> {
}
