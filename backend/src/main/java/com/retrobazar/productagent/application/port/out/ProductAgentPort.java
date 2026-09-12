package com.retrobazar.productagent.application.port.out;

import com.retrobazar.productagent.application.command.AnalyzeProductCommand;
import com.retrobazar.productagent.domain.ProductAgentProposal;
import com.retrobazar.productagent.domain.SimilarCatalogProduct;

import java.util.List;

public interface ProductAgentPort {

    ProductAgentProposal analyze(
            AnalyzeProductCommand command,
            List<SimilarCatalogProduct> catalogCandidates
    );
}
