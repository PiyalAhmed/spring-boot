/*
 * Copyright 2012-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.boot.actuate.docs.beans;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.jupiter.api.Test;

import org.springframework.boot.actuate.beans.BeansEndpoint;
import org.springframework.boot.actuate.docs.MockMvcEndpointDocumentationTests;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.util.CollectionUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;

/**
 * Tests for generating documentation describing {@link BeansEndpoint}.
 *
 * @author Andy Wilkinson
 */
class BeansEndpointDocumentationTests extends MockMvcEndpointDocumentationTests {

	@Test
	void beans() {
		List<FieldDescriptor> beanFields = List.of(fieldWithPath("aliases").description("Names of any aliases."),
				fieldWithPath("scope").description("Scope of the bean."),
				fieldWithPath("type").description("Fully qualified type of the bean."),
				fieldWithPath("resource").description("Resource in which the bean was defined, if any.")
					.optional()
					.type(JsonFieldType.STRING),
				fieldWithPath("dependencies").description("Names of any dependencies."));
		ResponseFieldsSnippet responseFields = responseFields(
				fieldWithPath("contexts").description("Application contexts keyed by id."), parentIdField(),
				fieldWithPath("contexts.*.beans").description("Beans in the application context keyed by name."))
			.andWithPrefix("contexts.*.beans.*.", beanFields);
		assertThat(this.mvc.get().uri("/actuator/beans")).hasStatusOk()
			.apply(document("beans",
					preprocessResponse(
							limit(this::isIndependentBean, "contexts", getApplicationContext().getId(), "beans")),
					responseFields));
	}

	private boolean isIndependentBean(Entry<String, Map<String, Object>> bean) {
		return CollectionUtils.isEmpty((Collection<?>) bean.getValue().get("aliases"))
				&& CollectionUtils.isEmpty((Collection<?>) bean.getValue().get("dependencies"));
	}

	@Configuration(proxyBeanMethods = false)
	static class TestConfiguration {

		@Bean
		BeansEndpoint beansEndpoint(ConfigurableApplicationContext context) {
			return new BeansEndpoint(context);
		}

	}

}
