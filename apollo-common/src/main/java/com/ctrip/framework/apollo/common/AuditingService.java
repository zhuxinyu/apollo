package com.ctrip.framework.apollo.common;

import com.ctrip.framework.apollo.common.entity.Audit;

import java.util.List;

/**
 * Created by kezhenxu at 2019/1/3 22:56
 *
 * @author kezhenxu (kezhenxu94 at 163 dot com)
 */
public interface AuditingService {
    List<Audit> findByOwner(String owner);

    List<Audit> find(String owner, String entity, String op);

    void audit(String entityName, Long entityId, Audit.OP op, String owner);

    void audit(Audit audit);
}
