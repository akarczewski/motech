package org.motechproject.dao;

import org.ektorp.CouchDbConnector;
import org.ektorp.ViewQuery;
import org.ektorp.support.CouchDbRepositorySupport;
import org.motechproject.model.MotechBaseDataObject;

import java.util.List;

public abstract class MotechBaseRepository<T extends MotechBaseDataObject> extends CouchDbRepositorySupport<T> {
    private Class<T> type;

    protected MotechBaseRepository(Class<T> type, CouchDbConnector db) {
        super(type, db);
        this.type = type;
    }

    protected void addOrReplace(T entity, String businessFieldName, String businessId) {
        List<T> entities = entities(businessFieldName, businessId);
        if (entities.size() == 0) add(entity);
        else if (entities.size() == 1) update(entity);
        else throw new BusinessIdNotUniqueException(businessFieldName, businessId);
    }

    private List<T> entities(String businessFieldName, String businessId) {
        String viewName = String.format("findBy%s", businessFieldName);
        ViewQuery q = createQuery(viewName).key(businessId).includeDocs(true);
        return db.queryView(q, type);
    }

    public void removeAll(String fieldName, String value) {
        List<T> entities = entities(fieldName, value);
        for (T entity : entities) {
            remove(entity);
        }
    }

    public void safeRemove(T entity) {
        if (contains(entity.getId()))
            remove(entity);
    }
}
