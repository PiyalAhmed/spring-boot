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

package org.springframework.boot.undertow.servlet;

import io.undertow.servlet.api.DeploymentInfo;

/**
 * Callback interface that can be used to customize an Undertow {@link DeploymentInfo}.
 *
 * @author Phillip Webb
 * @since 4.0.0
 * @see UndertowServletWebServerFactory
 */
@FunctionalInterface
public interface UndertowDeploymentInfoCustomizer {

	/**
	 * Customize the deployment info.
	 * @param deploymentInfo the {@code DeploymentInfo} to customize
	 */
	void customize(DeploymentInfo deploymentInfo);

}
