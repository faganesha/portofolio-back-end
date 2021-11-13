package com.example.Library.Validator;

import com.example.Library.Mapper.BodyResponse.Error.ErrorBodyResponse;
import com.example.Library.Mapper.BodyResponse.Error.ErrorMessageResponse;
import com.example.Library.Mapper.Request.MemberRequest;
import org.springframework.stereotype.Service;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.util.ArrayList;
import java.util.List;

@Service
public class MemberValidator {

    public static boolean isValidEmailAddress(String email) {
        boolean result = true;
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            result = false;
        }
        return result;
    }

    public ErrorBodyResponse addMemberValidator(MemberRequest request){
        List<ErrorMessageResponse> errorMessages = new ArrayList<>();

        if ( request.getName() == null || request.getName().trim().isEmpty()) {
            ErrorMessageResponse error = new ErrorMessageResponse();
            error.setField("Member name");
            error.setMessage("Name cannot be empty");
            errorMessages.add(error);
        }

        if (request.getEmail() == null || request.getEmail().trim().isEmpty() ) {
            ErrorMessageResponse error = new ErrorMessageResponse();
            error.setField("Member email");
            error.setMessage("Email cannot be empty");
            errorMessages.add(error);
        }

        if(!isValidEmailAddress(request.getEmail()) && request.getEmail() != null && !request.getEmail().trim().isEmpty() ){
            ErrorMessageResponse error = new ErrorMessageResponse();
            error.setField("Email");
            error.setMessage("Email is not valid");
            errorMessages.add(error);
        }

        ErrorBodyResponse errors = new ErrorBodyResponse();
        if (!errorMessages.isEmpty()) {
            errors.setMessage("Please check input parameter");
            errors.setStatusCode("400");
            errors.setTotalError(errorMessages.size());
            errors.setErrorMessages(errorMessages);
        } else {
            return null;
        }
        return errors;
    }


}
