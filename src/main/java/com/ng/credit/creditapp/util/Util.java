package com.ng.credit.creditapp.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Util {

    public String convertToJSon(Object customObj)
    {
        ObjectMapper mapper = new ObjectMapper();

        try {
            // convert user object to json string and return it
            log.info("returning json object");
            return mapper.writeValueAsString(customObj);
        }

        catch ( JsonProcessingException e) {
            // catch various errors
            log.error(e.getMessage());
            e.printStackTrace();
            return null;

        }
    }

}
