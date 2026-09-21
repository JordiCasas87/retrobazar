package com.retrobazar.productagent.infrastructure.adapter.out.ai;

import com.retrobazar.productagent.application.command.AnalyzeProductCommand;
import com.retrobazar.productagent.application.port.out.ProductAgentPort;
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
    public ProductAgentProposal analyze(AnalyzeProductCommand command) {
        List<Media> images = command.imageUrls().stream()
                .map(GeminiProductAgentAdapter::toImageMedia)
                .toList();

        ProductAgentProposal proposal = chatClient.prompt()
                .system(SYSTEM_PROMPT)
                .user(user -> user
                        .text(buildProductContext(command))
                        .media(images.toArray(Media[]::new)))
                .call()
                .entity(ProductAgentProposal.class, spec -> spec.validateSchema());

        return Objects.requireNonNull(proposal, "Gemini returned an empty product proposal");
    }

    private static String buildProductContext(AnalyzeProductCommand command) {
        return """
                Producto introducido por el usuario:
                - Título: %s
                - Marca: %s
                - Descripción: %s
                - Precio actual: %s EUR
                - Categoría: %s

                """.formatted(
                command.title(),
                command.brand(),
                command.description(),
                command.currentPrice(),
                command.category()
        );
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
