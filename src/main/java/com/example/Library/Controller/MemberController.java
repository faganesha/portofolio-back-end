package com.example.Library.Controller;

import com.example.Library.Mapper.BodyResponse.Error.ErrorBodyResponse;
import com.example.Library.Mapper.Request.MemberRequest;
import com.example.Library.Mapper.Response.MemberResponse;
import com.example.Library.Service.MemberService;
import com.example.Library.Validator.MemberValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/member")

public class MemberController {
    @Autowired
    private MemberService service;

    @Autowired
    private MemberValidator validator;

    @GetMapping
    public ResponseEntity showAllMember(@RequestParam("order") Integer order) {
        List<MemberResponse> result = service.showAllMember(order);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PostMapping(path = "/registration")
    public ResponseEntity registration(@RequestBody MemberRequest request) {
        ErrorBodyResponse errorMessage = validator.addMemberValidator(request);

        if(errorMessage != null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        } else {
            MemberResponse result = service.registration(request);
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }

    }
}
