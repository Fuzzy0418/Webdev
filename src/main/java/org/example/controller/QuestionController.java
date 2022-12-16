package org.example.controller;

import org.example.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.example.repository.QuestionRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api")
public class QuestionController {

    @Autowired
    QuestionRepository questionRepository;

    @GetMapping("/questions")
    public ResponseEntity<List<Question>> getAllQuestions(){
        try {
            List<Question> questions = new ArrayList<>();

            questionRepository.findAll().forEach(questions::add);


            if (questions.isEmpty())
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);

            return new ResponseEntity<>(questions, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/questions/{id}")
    public ResponseEntity<Question> getQuestionById(@PathVariable("id") int id){
        Optional<Question> questionData = questionRepository.findById(id);

        return questionData.map(question -> new ResponseEntity<>(question, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/questions")
    public ResponseEntity<Question> createQuestion(@RequestBody Question question) {
        try {
            Question _question = questionRepository
                    .save(new Question(question.getQuestion(), question.getAnswers(), question.getCorrectAnswer()));

            return new ResponseEntity<>(_question, HttpStatus.CREATED);
        } catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/questions/{id}")
    public ResponseEntity<Question> updateQuestion(@PathVariable("id") int id, @RequestBody Question question){
        Optional<Question> questionData = questionRepository.findById(id);

        if (questionData.isPresent()){
            Question _question = questionData.get();
            _question.setQuestion(question.getQuestion());
            _question.setAnswers(question.getAnswers());
            _question.setCorrectAnswer(question.getCorrectAnswer());

            return new ResponseEntity<>(questionRepository.save(_question), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/questions/{id}")
    public ResponseEntity<HttpStatus> deleteQuestion(@PathVariable("id") int id) {
        try {
            questionRepository.deleteById(id);

            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
