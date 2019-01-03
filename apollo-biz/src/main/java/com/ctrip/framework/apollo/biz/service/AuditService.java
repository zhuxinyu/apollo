package com.ctrip.framework.apollo.biz.service;

import com.ctrip.framework.apollo.common.AuditingService;
import com.ctrip.framework.apollo.common.entity.Audit;
import com.ctrip.framework.apollo.biz.repository.AuditRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AuditService implements AuditingService {

  @Autowired
  private AuditRepository auditRepository;

  @Override
  public List<Audit> findByOwner(String owner) {
    return auditRepository.findByOwner(owner);
  }

  @Override
  public List<Audit> find(String owner, String entity, String op) {
    return auditRepository.findAudits(owner, entity, op);
  }

  @Transactional
  @Override
  public void audit(String entityName, Long entityId, Audit.OP op, String owner) {
    Audit audit = new Audit();
    audit.setEntityName(entityName);
    audit.setEntityId(entityId);
    audit.setOpName(op.name());
    audit.setDataChangeCreatedBy(owner);
    auditRepository.save(audit);
  }

  @Transactional
  @Override
  public void audit(Audit audit){
    auditRepository.save(audit);
  }
}
