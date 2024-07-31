//package com.kosa.kmt.controller;
//
//import com.kosa.kmt.nonController.hashtag.Hashtag;
//import com.kosa.kmt.nonController.hashtag.HashtagRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@RestController
//public class HashtagController {
//
//    @Autowired
//    private HashtagRepository hashtagRepository;
//
//    @GetMapping
//    public ResponseEntity<Map<String, Object>> getHashtagList() {
//        List<Hashtag> allHashtags = hashtagRepository.findAll();
//
//
//        Map<String, Object> response = new HashMap<>();
//        response.put("hashmaps", hashtags);
//
//        Map<String, Object> response = new HashMap<>();
//        response.put("chatContents", chatDTOs);
//
//
//        return ResponseEntity.ok(response);
//    }
//}
