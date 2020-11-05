/*
 * Copyright 2002-2019 the original author or authors.
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

package org.springframework.core.io.support;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.core.io.UrlResource;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ConcurrentReferenceHashMap;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

/**
 * General purpose factory loading mechanism for internal use within the framework.
 *
 * <p>{@code SpringFactoriesLoader} {@linkplain #loadFactories loads} and instantiates
 * factories of a given type from {@value #FACTORIES_RESOURCE_LOCATION} files which
 * may be present in multiple JAR files in the classpath. The {@code spring.factories}
 * file must be in {@link Properties} format, where the key is the fully qualified
 * name of the interface or abstract class, and the value is a comma-separated list of
 * implementation class names. For example:
 *
 * <pre class="code">example.MyService=example.MyServiceImpl1,example.MyServiceImpl2</pre>
 *
 * where {@code example.MyService} is the name of the interface, and {@code MyServiceImpl1}
 * and {@code MyServiceImpl2} are two implementations.
 *
 * @author Arjen Poutsma
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @since 3.2
 */
public final class SpringFactoriesLoader {

	/**
	 * The location to look for factories.
	 * <p>Can be present in multiple JAR files.
	 */
	public static final String FACTORIES_RESOURCE_LOCATION = "META-INF/spring.factories";


	private static final Log logger = LogFactory.getLog(SpringFactoriesLoader.class);

	private static final Map<ClassLoader, MultiValueMap<String, String>> cache = new ConcurrentReferenceHashMap<>();


	private SpringFactoriesLoader() {
	}


	/**
	 * Load and instantiate the factory implementations of the given type from
	 * {@value #FACTORIES_RESOURCE_LOCATION}, using the given class loader.
	 * <p>The returned factories are sorted through {@link AnnotationAwareOrderComparator}.
	 * <p>If a custom instantiation strategy is required, use {@link #loadFactoryNames}
	 * to obtain all registered factory names.
	 * @param factoryType the interface or abstract class representing the factory
	 * @param classLoader the ClassLoader to use for loading (can be {@code null} to use the default)
	 * @throws IllegalArgumentException if any factory implementation class cannot
	 * be loaded or if an error occurs while instantiating any factory
	 * @see #loadFactoryNames
	 */
	public static <T> List<T> loadFactories(Class<T> factoryType, @Nullable ClassLoader classLoader) {
		Assert.notNull(factoryType, "'factoryType' must not be null");
		ClassLoader classLoaderToUse = classLoader;
		if (classLoaderToUse == null) {
			classLoaderToUse = SpringFactoriesLoader.class.getClassLoader();
		}
		List<String> factoryImplementationNames = loadFactoryNames(factoryType, classLoaderToUse);
		if (logger.isTraceEnabled()) {
			logger.trace("Loaded [" + factoryType.getName() + "] names: " + factoryImplementationNames);
		}
		List<T> result = new ArrayList<>(factoryImplementationNames.size());
		for (String factoryImplementationName : factoryImplementationNames) {
			result.add(instantiateFactory(factoryImplementationName, factoryType, classLoaderToUse));
		}
		AnnotationAwareOrderComparator.sort(result);
		return result;
	}

	/**
	 * Step 2. loadFactoryNames
	 *
	 * factoryTypeName: 字符串，比如"org.springframework.boot.autoconfigure.AutoConfigurationImportFilter"
	 *
	 * 返回类似于
	 * org.springframework.boot.env.SpringApplicationJsonEnvironmentPostProcessor,
	 * org.springframework.boot.env.SystemEnvironmentPropertySourceEnvironmentPostProcessor,
	 * org.springframework.boot.reactor.DebugAgentEnvironmentPostProcessor
	 * 组成的List
	 */
	public static List<String> loadFactoryNames(Class<?> factoryType, @Nullable ClassLoader classLoader) {
		String factoryTypeName = factoryType.getName();
		return loadSpringFactories(classLoader).getOrDefault(factoryTypeName, Collections.emptyList());
	}

	/**
	 * Step 3. loadSpringFactories
	 *
	 * urls:
	 * 大致可以表述为所有的spring_factories文件的路径组成的集合
	 *
	 * properties:
	 * 	entry1:
	 * 		org.springframework.boot.env.EnvironmentPostProcessor=\
	 * 		org.springframework.boot.cloud.CloudFoundryVcapEnvironmentPostProcessor,\
	 * 		org.springframework.boot.env.SpringApplicationJsonEnvironmentPostProcessor,\
	 * 		org.springframework.boot.env.SystemEnvironmentPropertySourceEnvironmentPostProcessor,\
	 * 		org.springframework.boot.reactor.DebugAgentEnvironmentPostProcessor
	 * 	entry2:
	 * 		org.springframework.context.ApplicationListener=\
	 * 		org.springframework.boot.autoconfigure.BackgroundPreinitializer
	 *  entry3:
	 * 		org.springframework.context.ApplicationContextInitializer=\
	 * 		org.springframework.boot.autoconfigure.SharedMetadataReaderFactoryContextInitializer,\
	 * 		org.springframework.boot.autoconfigure.logging.ConditionEvaluationReportLoggingListener
	 * 	...
	 *
	 * cache:
	 *    classloader1: {
	 *        "org.springframework.boot.env.PropertySourceLoader1"
	 *            -> ["org.springframework.boot.env.PropertiesPropertySourceLoader1",
	 *            "org.springframework.boot.env.YamlPropertySourceLoader1"],
	 *        "org.springframework.boot.SpringApplicationRunListener1"
	 *            -> ["org.springframework.boot.context.event.EventPublishingRunListener1"],
	 *        "org.springframework.context.ApplicationContextInitializer1"
	 *            -> ["org.springframework.boot.context.ConfigurationWarningsApplicationContextInitializer1",
	 *            "org.springframework.boot.context.ContextIdApplicationContextInitializer1",
	 *            "org.springframework.boot.web.context.ServerPortInfoApplicationContextInitializer1"],
	 *    }
	 *    classloader2: {
	 *        "org.springframework.boot.env.PropertySourceLoader2"
	 *            -> ["org.springframework.boot.env.PropertiesPropertySourceLoader2",
	 *            "org.springframework.boot.env.YamlPropertySourceLoader2"],
	 *        "org.springframework.boot.SpringBootExceptionReporter2"
	 *            -> ["org.springframework.boot.diagnostics.FailureAnalyzers2"],
	 *    }
	 *    ...
	 */
	private static Map<String, List<String>> loadSpringFactories(@Nullable ClassLoader classLoader) {
		// cache中取数据，如果取出不为空，则直接返回
		MultiValueMap<String, String> result = cache.get(classLoader);
		if (result != null) {
			return result;
		}

		try {
			Enumeration<URL> urls = (classLoader != null ?
					classLoader.getResources(FACTORIES_RESOURCE_LOCATION) :
					ClassLoader.getSystemResources(FACTORIES_RESOURCE_LOCATION));
			result = new LinkedMultiValueMap<>();
			while (urls.hasMoreElements()) {
				URL url = urls.nextElement();
				UrlResource resource = new UrlResource(url);
				Properties properties = PropertiesLoaderUtils.loadProperties(resource);
				for (Map.Entry<?, ?> entry : properties.entrySet()) {
					String factoryTypeName = ((String) entry.getKey()).trim();
					for (String factoryImplementationName : StringUtils.commaDelimitedListToStringArray((String) entry.getValue())) {
						result.add(factoryTypeName, factoryImplementationName.trim());
					}
				}
			}
			// cache中存数据
			cache.put(classLoader, result);
			return result;
		}
		catch (IOException ex) {
			throw new IllegalArgumentException("Unable to load factories from location [" +
					FACTORIES_RESOURCE_LOCATION + "]", ex);
		}
	}

	@SuppressWarnings("unchecked")
	private static <T> T instantiateFactory(String factoryImplementationName, Class<T> factoryType, ClassLoader classLoader) {
		try {
			Class<?> factoryImplementationClass = ClassUtils.forName(factoryImplementationName, classLoader);
			if (!factoryType.isAssignableFrom(factoryImplementationClass)) {
				throw new IllegalArgumentException(
						"Class [" + factoryImplementationName + "] is not assignable to factory type [" + factoryType.getName() + "]");
			}
			return (T) ReflectionUtils.accessibleConstructor(factoryImplementationClass).newInstance();
		}
		catch (Throwable ex) {
			throw new IllegalArgumentException(
				"Unable to instantiate factory class [" + factoryImplementationName + "] for factory type [" + factoryType.getName() + "]",
				ex);
		}
	}

}
