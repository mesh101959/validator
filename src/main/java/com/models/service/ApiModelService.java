package com.models.service;

import com.models.configuration.Messages;
import com.models.model.*;
import com.models.repository.ApiModelsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;


@Service
public class ApiModelService {
    static final AbnormalFields[] emptyFailedFields = {};
    public static final String EMAIL_REGEX_PATTERN = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
    public static final String AUTH_TOKEN = "Bearer\\s[\\d|a-f]{8}-([\\d|a-f]{4}-){3}[\\d|a-f]{12}";

    @Autowired
    ApiModelsRepository apiModelsRepository;

    @Autowired
    RequestTypeValidator requestTypeValidator;

    public ApiValidationResponse validate(ApiRequest apiRequest) {
        ApiModel apiModel = apiModelsRepository.findById(apiRequest.path(), apiRequest.method());
        ApiValidationResponse apiCheckResponse =  validateRequest(apiRequest, apiModel);
        return apiCheckResponse;
    }

    private ApiValidationResponse validateRequest(ApiRequest apiRequest, ApiModel apiModel) {
        if(null != apiModel) {
            Collection<AbnormalFields> failedFields = new LinkedList<>();
            validateRequestData(apiModel.query_params(), apiRequest.query_params(), Messages.QUERY_PARAMS_SECTION, failedFields);
            validateRequestData(apiModel.headers(), apiRequest.headers(), Messages.HEADER_SECTION, failedFields);
            validateRequestData(apiModel.body(), apiRequest.body(), Messages.BODY_SECTION, failedFields);

            if (failedFields.isEmpty()) {
                return new ApiValidationResponse("valid", emptyFailedFields);
            }
            return new ApiValidationResponse("abnormal", failedFields.toArray(emptyFailedFields));
        }else{
            return new ApiValidationResponse("Missing model", emptyFailedFields);
        }
    }



    private void validateRequestData(AttributeDescriptor[] attributesDescriptors,
                                                         QueryParamsInput[] queryParams,
                                                         String sectionName,
                                                         Collection<AbnormalFields> failedFields) {
        Map<String, QueryParamsInput> queryParamsInputMap = new HashMap<>();
        Arrays.stream(queryParams).forEach(param -> queryParamsInputMap.put(param.name(), param));

        for (AttributeDescriptor attributeDescriptor : attributesDescriptors) {
            QueryParamsInput queryParamsInput = queryParamsInputMap.get(attributeDescriptor.name());
            if(null == queryParamsInput){
                if(attributeDescriptor.required()){
                    failedFields.add(new AbnormalFields(new QueryParamsInput(attributeDescriptor.name(), null), Messages.MISSING_MANDATORY_FIELD_IN + sectionName) ) ;
                }
            }else{
                if(!requestTypeValidator.validateType(attributeDescriptor.types(), queryParamsInput.value())){
                    failedFields.add(new AbnormalFields(queryParamsInput, Messages.BAD_FIELD_TYPE_IN + sectionName +"" +
                            ", Should be one of the following types: -> " + String.join(",", attributeDescriptor.types())) ) ;
                }
            }
        }
    }

    public void registerModel(ApiModel[] apiModels) {
        Arrays.stream(apiModels).sequential().allMatch(this::addModel);
    }

    private boolean addModel(ApiModel model) {
        apiModelsRepository.save(model);
        return true;
    }

    public Collection<ApiModel> getModels() {
        return apiModelsRepository.findAll();
    }

    public ApiModel findById(String path, String method) {
        return apiModelsRepository.findById(path, method);
    }
}