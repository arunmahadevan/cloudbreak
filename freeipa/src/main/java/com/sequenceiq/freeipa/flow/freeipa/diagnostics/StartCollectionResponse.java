package com.sequenceiq.freeipa.flow.freeipa.diagnostics;

public class StartCollectionResponse extends AbstractDiagnosticsCollectionEvent {
    public StartCollectionResponse(String selector, Long stackId, String accountId, String environmentCrn, String operationId) {
        super(selector, stackId, accountId, environmentCrn, operationId);
    }

    public StartCollectionResponse(DiagnosticsCollectionEvent event) {
        super(event);
    }
}
