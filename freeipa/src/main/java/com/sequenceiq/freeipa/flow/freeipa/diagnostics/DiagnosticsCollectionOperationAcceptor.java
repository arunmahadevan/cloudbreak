package com.sequenceiq.freeipa.flow.freeipa.diagnostics;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import com.sequenceiq.freeipa.api.v1.operation.model.OperationType;
import com.sequenceiq.freeipa.entity.Operation;
import com.sequenceiq.freeipa.repository.OperationRepository;
import com.sequenceiq.freeipa.service.freeipa.user.AcceptResult;
import com.sequenceiq.freeipa.service.operation.OperationAcceptor;

@Component
public class DiagnosticsCollectionOperationAcceptor extends OperationAcceptor {

    @Inject
    public DiagnosticsCollectionOperationAcceptor(OperationRepository operationRepository) {
        super(operationRepository);
    }

    @Override
    protected OperationType selector() {
        return OperationType.DIAGNOSTICS_COLLECTION;
    }

    @Override
    public AcceptResult accept(Operation operation) {
        return AcceptResult.accept();
    }
}
