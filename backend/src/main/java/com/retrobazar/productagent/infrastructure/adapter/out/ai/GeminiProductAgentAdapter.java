package com.retrobazar.productagent.infrastructure.adapter.out.ai;

import com.retrobazar.productagent.application.command.AnalyzeProductCommand;
import com.retrobazar.productagent.application.port.out.ProductAgentPort;
import com.retrobazar.productagent.domain.CatalogProductMatch;
import com.retrobazar.productagent.domain.ProductAgentProposal;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.content.Media;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;

import java.net.URI;
import java.util.List;
import java.util.Objects;

@Component
public class GeminiProductAgentAdapter implements ProductAgentPort {

    private static final ClassPathResource SYSTEM_PROMPT =
            new ClassPathResource("prompts/analyze-product.txt");

    private final ChatClient chatClient;

    public GeminiProductAgentAdapter(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @Override
    public ProductAgentProposal analyze(
            AnalyzeProductCommand command,
            List<CatalogProductMatch> catalogMatches
    ) {
        List<Media> images = command.imageUrls().stream()
                .map(GeminiProductAgentAdapter::toImageMedia)
                .toList();

        ProductAgentProposal proposal = chatClient.prompt()
                .system(SYSTEM_PROMPT)
                .user(user -> user
                        .text(buildProductContext(command, catalogMatches))
                        .media(images.toArray(Media[]::new)))
                .call()
                .entity(ProductAgentProposal.class, spec -> spec.validateSchema());

        return Objects.requireNonNull(proposal, "Gemini returned an empty product proposal");
    }

    private static String buildProductContext(
            AnalyzeProductCommand command,
            List<CatalogProductMatch> catalogMatches
    ) {
        return """
                Producto introducido por el usuario:
                - Título: %s
                - Marca: %s
                - Descripción: %s
                - Precio actual: %s EUR
                - Categoría: %s

                Posibles coincidencias encontradas en el catálogo de Retro Bazar:
                %s
                """.formatted(
                command.title(),
                command.brand(),
                command.description(),
                command.currentPrice(),
                command.category(),
                formatCatalogMatches(catalogMatches)
        );
    }

    private static String formatCatalogMatches(List<CatalogProductMatch> catalogMatches) {
        if (catalogMatches.isEmpty()) {
            return "No se encontraron coincidencias en el catálogo.";
        }

        return catalogMatches.stream()
                .map(match -> "- %s | %s EUR | id: %s"
                        .formatted(match.title(), match.price(), match.productId()))
                .reduce((first, second) -> first + System.lineSeparator() + second)
                .orElseThrow();
    }

    private static Media toImageMedia(String imageUrl) {
        URI uri = URI.create(imageUrl);
        return new Media(imageMimeType(uri.getPath()), uri);
    }

    private static MimeType imageMimeType(String path) {
        String normalizedPath = path == null ? "" : path.toLowerCase();

        if (normalizedPath.endsWith(".png")) {
            return MimeTypeUtils.IMAGE_PNG;
        }
        if (normalizedPath.endsWith(".gif")) {
            return MimeTypeUtils.IMAGE_GIF;
        }

        return MimeTypeUtils.IMAGE_JPEG;
    }
}
