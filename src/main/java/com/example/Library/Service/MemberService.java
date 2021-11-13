package com.example.Library.Service;

import com.example.Library.Mapper.Request.MemberRequest;
import com.example.Library.Mapper.Response.MemberResponse;
import com.example.Library.Model.Borrowment;
import com.example.Library.Model.Member;
import com.example.Library.Repository.BorrowmentRepository;
import com.example.Library.Repository.MemberRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MemberService {
    @Autowired
    MemberRepository repository;

    @Autowired
    BorrowmentRepository borrowmentRepository;

    public List<MemberResponse> convert(List<Member> models) {
        List<MemberResponse> responses = new ArrayList<>();

        for (Member member : models) {
            MemberResponse response = new ObjectMapper().convertValue(member, MemberResponse.class);
            responses.add(response);
        }
        return responses;
    }

    public List<MemberResponse> showAllMember(Integer order) {
        if (order == 1) {
            return convert(repository.findByFlagOrderById(1));
        } else {
            return convert(repository.findByFlagOrderByName(1));
        }
    }

    public List<Borrowment> getEmail() {
        return borrowmentRepository.findByReturnFlag(2);
    }

    public MemberResponse registration(MemberRequest request) {
        Member member = new Member();
        member.setName(request.getName());
        member.setEmail(request.getEmail());
        //set flag default value di model
        member.setFlag(1);
        repository.save(member);

        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(member, MemberResponse.class);
    }
}
