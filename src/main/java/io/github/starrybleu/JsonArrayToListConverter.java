package io.github.starrybleu;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Converter
public class JsonArrayToListConverter implements AttributeConverter<List<Integer>, String> {

    private static final ObjectMapper mapper = new ObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    private static final Logger logger = LoggerFactory.getLogger(JsonArrayToListConverter.class);

    @Override
    public String convertToDatabaseColumn(List<Integer> attribute) {
        if (attribute == null) {
            attribute = new ArrayList<>();
        }
        try {
            return mapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            logger.error("List<String> converting to databaseColumn failed. List: {}", attribute);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Integer> convertToEntityAttribute(String json) {
        if (StringUtils.isEmpty(json)) {
            json = "[]";
        }
        try {
            return mapper.readValue(json, new TypeReference<List<String>>(){});
        } catch (IOException e) {
            logger.error("json string(databaseColumn) converting to List<String> failed. json: {}", json);
            throw new RuntimeException(e);
        }
    }
}
