package ru.vlsu.psytest.api.questions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vlsu.psytest.api.questions.Question;
import ru.vlsu.psytest.api.questions.QuestionRepository;

import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository repo;

    public List<Question> listAll(){
        return repo.findAll();
    }

    public void save (Question question){
        repo.save(question);
    }

    public Question getQuestion(Integer id){
        return repo.findById(id).get();
    }

    public void delete(Integer id){
        repo.deleteById(id);
    }

    public String getResult(Integer IE, Integer SN, Integer TF, Integer JP){

        // инициализируем перменные под черты характера
        char introvertOrExtrovert = ' ';
        char sensorOrIntuitive = ' ';
        char thinkerOrFeeler = ' ';
        char judgerOrPerceiver = ' ';

        // Main algorithm. Stores the user's Test answers as corresponding Myers-Briggs letters.
        if (IE <= 20){
            introvertOrExtrovert = 'E';
        }else{
            introvertOrExtrovert = 'I';
        }

        if( SN <= 20){
            sensorOrIntuitive = 'S';
        }else{
            sensorOrIntuitive = 'N';
        }

        if(TF <= 20){
            thinkerOrFeeler = 'T';
        }else{
            thinkerOrFeeler = 'F';
        }

        if(JP <= 20){
            judgerOrPerceiver = 'J';
        }else{
            judgerOrPerceiver = 'P';
        }

        // конкатенируем строчку результата:
        String res = "Ваш тип личности: *" + introvertOrExtrovert + sensorOrIntuitive + thinkerOrFeeler + judgerOrPerceiver + "*" ;

        return res;
    }
}
