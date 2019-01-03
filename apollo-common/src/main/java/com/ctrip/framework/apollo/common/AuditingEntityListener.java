package com.ctrip.framework.apollo.common;

import com.ctrip.framework.apollo.common.entity.Audit;
import com.ctrip.framework.apollo.common.entity.BaseEntity;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.aspectj.ConfigurableObject;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

/**
 * Created by kezhenxu at 2019/1/3 22:47
 *
 * @author kezhenxu (kezhenxu94 at 163 dot com)
 */
@Configurable(preConstruction = true, autowire = Autowire.BY_TYPE)
public class AuditingEntityListener implements ConfigurableObject {
  @Autowired
  private AuditingService auditingService;

  @PostPersist
  public void postPersist(Object object) {
    if (!(object instanceof BaseEntity)) {
      throw new IllegalArgumentException(
          String.format("type %s must inherit %s", object.getClass().getName(), BaseEntity.class.getName())
      );
    }
    BaseEntity entity = (BaseEntity) object;
    Audit audit = new Audit();
    audit.setEntityName(entity.getClass().getSimpleName());
    audit.setEntityId(entity.getId());
    audit.setOpName(Audit.OP.INSERT.name());
    audit.setDataChangeCreatedBy(entity.getDataChangeLastModifiedBy());
    System.out.println("audit = " + audit);
    auditingService.audit(audit);
  }

  @PostUpdate
  public void postUpdate(Object object) {
    System.out.println("object = " + object);
  }

  @PostRemove
  public void postRemove(Object object) {
    System.out.println("object = " + object);
  }
}
