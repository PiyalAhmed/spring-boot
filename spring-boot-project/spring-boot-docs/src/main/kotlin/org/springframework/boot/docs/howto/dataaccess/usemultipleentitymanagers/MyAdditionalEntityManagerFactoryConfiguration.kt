/*
 * Copyright 2012-2022 the original author or authors.
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

package org.springframework.boot.docs.howto.dataaccess.usemultipleentitymanagers

import javax.sql.DataSource

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.orm.jpa.JpaVendorAdapter
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter

@Suppress("UNUSED_PARAMETER")
@Configuration(proxyBeanMethods = false)
class MyAdditionalEntityManagerFactoryConfiguration {

	@Qualifier("second")
	@Bean(defaultCandidate = false)
	@ConfigurationProperties("app.jpa")
	fun secondJpaProperties(): JpaProperties {
		return JpaProperties()
	}

	@Qualifier("second")
	@Bean(defaultCandidate = false)
	fun firstEntityManagerFactory(
		@Qualifier("second") dataSource: DataSource,
		@Qualifier("second") jpaProperties: JpaProperties
	): LocalContainerEntityManagerFactoryBean {
		val builder = createEntityManagerFactoryBuilder(jpaProperties)
		return builder.dataSource(dataSource).packages(Order::class.java).persistenceUnit("second").build()
	}

	private fun createEntityManagerFactoryBuilder(jpaProperties: JpaProperties): EntityManagerFactoryBuilder {
		val jpaVendorAdapter = createJpaVendorAdapter(jpaProperties)
		return EntityManagerFactoryBuilder(jpaVendorAdapter, jpaProperties.properties, null)
	}

	private fun createJpaVendorAdapter(jpaProperties: JpaProperties): JpaVendorAdapter {
		// ... map JPA properties as needed
		return HibernateJpaVendorAdapter()
	}

}
