package com.retrobazar.productagent.application.port.out;

import com.retrobazar.productagent.application.command.AnalyzeProductCommand;
import com.retrobazar.productagent.domain.ProductAgentProposal;
public interface ProductAgentPort {

    ProductAgentProposal analyze(AnalyzeProductCommand command);
}
