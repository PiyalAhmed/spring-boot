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

package org.springframework.boot.testsupport.classpath;

import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;

import org.springframework.util.ClassUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.isA;

/**
 * Tests for {@link ModifiedClassPathExtension} excluding entries from the class path.
 *
 * @author Christoph Dreis
 * @author Andy Wilkinson
 */
@ClassPathExclusions(files = "hibernate-validator-*.jar", packages = "java.net.http")
class ModifiedClassPathExtensionExclusionsTests {

	private static final String EXCLUDED_RESOURCE = "META-INF/services/jakarta.validation.spi.ValidationProvider";

	@Test
	void fileExclusionsAreFilteredFromTestClassClassLoader() {
		assertThat(getClass().getClassLoader().getResource(EXCLUDED_RESOURCE)).isNull();
	}

	@Test
	void fileExclusionsAreFilteredFromThreadContextClassLoader() {
		assertThat(Thread.currentThread().getContextClassLoader().getResource(EXCLUDED_RESOURCE)).isNull();
	}

	@Test
	void packageExclusionsAreFilteredFromTestClassClassLoader() {
		assertThat(ClassUtils.isPresent("java.net.http.HttpClient", getClass().getClassLoader())).isFalse();
	}

	@Test
	void packageExclusionsAreFilteredFromThreadContextClassLoader() {
		assertThat(ClassUtils.isPresent("java.net.http.HttpClient", Thread.currentThread().getContextClassLoader()))
			.isFalse();
	}

	@Test
	void testsThatUseHamcrestWorkCorrectly() {
		Matcher<IllegalStateException> matcher = isA(IllegalStateException.class);
		assertThat(matcher.matches(new IllegalStateException())).isTrue();
	}

}
