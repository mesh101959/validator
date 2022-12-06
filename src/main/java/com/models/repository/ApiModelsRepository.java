package com.models.repository;

import com.models.model.Tuple;
import com.models.model.ApiModel;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/* in production would use
  a database which is fast for searching (e.g., elastic search). but depends on the scale of the number of models.
  for small amount, can have it all in memory,
  need to understand the full definition of the use-case and user story, is this service running online or as a scraper of historic
  data in database/log files.
  The assumption is that number of models is not large, as it is the number of REST end points in a site.
 */

@Component
public class ApiModelsRepository  {

    static final ConcurrentMap<Tuple, ApiModel> apiModels = new ConcurrentHashMap<>();


    public void save(ApiModel model) {
        apiModels.put(new Tuple(model.path(), model.method()), model);
    }

    public Collection<ApiModel> findAll() {
        return apiModels.values();
    }

    public ApiModel findById(String path, String method) {
        return apiModels.get(new Tuple(path, method));
    }
}
