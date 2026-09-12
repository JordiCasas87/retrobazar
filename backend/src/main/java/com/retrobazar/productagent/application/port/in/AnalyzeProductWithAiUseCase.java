package com.retrobazar.productagent.application.port.in;

import com.retrobazar.productagent.application.command.AnalyzeProductCommand;
import com.retrobazar.productagent.domain.ProductAgentProposal;

public interface AnalyzeProductWithAiUseCase {

    ProductAgentProposal analyze(AnalyzeProductCommand command);
}
