package com.weatherable.weatherable.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.weatherable.weatherable.enums.DefaultRes;
import com.weatherable.weatherable.enums.StatusCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.util.Map;

@RestController
public class PredictController {
    //이 코드는 여러개를 동시에 처리할 때 필요함
//    @PostMapping("/sendmessage")
//    @ResponseBody
//    public String receiveMessage(@RequestBody byte[] messageBytes) throws JsonProcessingException, UnsupportedEncodingException {
//        // 받은 메시지를 UTF-8로 디코딩
//        String message = new String(messageBytes, "UTF-8");
//
//        // 받은 메시지 json 으로 파싱
//        ObjectMapper mapper = new ObjectMapper();
//        JsonNode jsonNode = mapper.readTree(message);
//        System.out.println("Received message from Python server: " + jsonNode.toString());
//
//        // 원하는 응답 또는 처리를 수행하고 반환
//        return "Message received successfully!";
//    }

    // 단일 이미지 판단 출력
    @PostMapping("/sendmessage")
    @ResponseBody
    public ResponseEntity<DefaultRes<String>> receiveMessage(@RequestBody String data) {

        System.out.println("message " + data);

        // 원하는 응답 또는 처리를 수행하고 반환

        return new ResponseEntity<>(
                DefaultRes.res(StatusCode.OK, "Message received successfully!"),
                HttpStatus.OK);
    }
}
