package com.example.mcpserver.config;

import com.example.mcpserver.controller.MutationController;
import com.example.mcpserver.controller.QueryController;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.McpSyncServer;
import io.modelcontextprotocol.server.transport.StdioServerTransportProvider;
import io.modelcontextprotocol.server.transport.WebFluxSseServerTransportProvider;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpServerTransportProvider;
import org.springframework.ai.mcp.McpToolUtils;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;

@Configuration
public class McpServerConfig {

	@Bean
	@ConditionalOnProperty(prefix = "transport", name = "mode", havingValue = "stdio")
	public StdioServerTransportProvider stdioServerTransportProvider() {
		return new StdioServerTransportProvider();
	}

	@Bean
	@ConditionalOnProperty(prefix = "transport", name = "mode", havingValue = "sse")
	public WebFluxSseServerTransportProvider sseServerTransportProvider() {
		return new WebFluxSseServerTransportProvider(new ObjectMapper(), "/mcp/message");
	}

	@Bean
	@ConditionalOnProperty(prefix = "transport", name = "mode", havingValue = "sse")
	public RouterFunction<?> mcpRouterFunction(WebFluxSseServerTransportProvider transportProvider) {
		return transportProvider.getRouterFunction();
	}

	@Bean
	public McpSyncServer mcpServer(McpServerTransportProvider transportProvider, MutationController mutationController, QueryController queryController) {

		var capabilities = McpSchema.ServerCapabilities.builder()
			.tools(true)
			.logging()
			.build();

		McpSyncServer server = McpServer.sync(transportProvider)
			.serverInfo("MCP", "1.0.0")
			.capabilities(capabilities)
			.tools(McpToolUtils.toSyncToolSpecifications(ToolCallbacks.from(mutationController, queryController)))
			.build();
		
		return server;
	}

}
