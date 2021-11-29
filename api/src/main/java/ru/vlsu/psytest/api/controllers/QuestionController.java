package ru.vlsu.psytest.api.controllers;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.vlsu.psytest.api.questions.Question;
import ru.vlsu.psytest.api.questions.QuestionService;

import java.util.List;
import java.util.NoSuchElementException;

//здесь обрабатываются запросы для restful web сервисов
@RestController
public class QuestionController {

    @Autowired
    private QuestionService service;

    @GetMapping("/questions")
    @ApiOperation("Возвращает список всех вопросов")
    public List<Question> list(){
        return service.listAll();
    }

    @GetMapping("/question/{id}")
    @ApiOperation("Возвращает один из вопросов по Id")
    public ResponseEntity<Question> get(@PathVariable Integer id){
        try{
            Question question = service.getQuestion(id);
            return  new ResponseEntity<Question>(question, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<Question>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/questions")
    @ApiOperation("Добавляет вопрос в базу данных")
    public void add (Question question) {
        service.save(question);
    }

    @PutMapping("/questions/{id}")
    @ApiOperation("Редактирование вопроса")
    public ResponseEntity<?> update(@RequestBody Question question, @PathVariable Integer id ){

        try{
            Question existQuestion = service.getQuestion(id);
            service.save(question);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NoSuchElementException e ) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/question/{id}")
    @ApiOperation("Удаление вопроса по id")
    public void delete(@PathVariable Integer id){
        service.delete(id);
    }

    @GetMapping("/results/{IE}&{SN}&{TF}&{JP}")
    @ApiOperation("Получает данные о пройденном тесте и возвращает результат прохождения теста")
    public String results(@PathVariable Integer IE,@PathVariable Integer SN,@PathVariable Integer TF,@PathVariable Integer JP){
      return service.getResult(IE,SN,TF,JP);
    }
}
