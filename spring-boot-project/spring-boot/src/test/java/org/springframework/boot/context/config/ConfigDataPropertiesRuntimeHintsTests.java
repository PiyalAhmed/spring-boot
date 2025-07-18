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

package org.springframework.boot.context.config;

import org.junit.jupiter.api.Test;

import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.predicate.RuntimeHintsPredicates;
import org.springframework.boot.context.config.ConfigDataProperties.Activate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link ConfigDataPropertiesRuntimeHints}.
 *
 * @author Moritz Halbritter
 */
class ConfigDataPropertiesRuntimeHintsTests {

	@Test
	void shouldRegisterHints() {
		RuntimeHints hints = new RuntimeHints();
		new ConfigDataPropertiesRuntimeHints().registerHints(hints, getClass().getClassLoader());
		assertThat(RuntimeHintsPredicates.reflection().onType(ConfigDataProperties.class)).accepts(hints);
		assertThat(RuntimeHintsPredicates.reflection().onType(ConfigDataLocation.class)).accepts(hints);
		assertThat(RuntimeHintsPredicates.reflection().onType(Activate.class)).accepts(hints);
		assertThat(RuntimeHintsPredicates.reflection().onMethodInvocation(ConfigDataLocation.class, "of"))
			.accepts(hints);
	}

}
