package com.ocoelhogabriel.orders_monolith.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        PomInfo pom = readPomInfo();

        Info info = new Info()
                .title(nonEmpty(pom.name, "Orders Monolith API"))
                .version(nonEmpty(pom.version, "v0.0.1"))
                .description(nonEmpty(pom.description, "API para gerenciar pedidos e clientes"));

        if (pom.developerName != null || pom.developerEmail != null) {
            Contact c = new Contact();
            if (pom.developerName != null) c.setName(pom.developerName);
            if (pom.developerEmail != null) c.setEmail(pom.developerEmail);
            info.setContact(c);
        }

        if (pom.license != null) {
            info.setLicense(new License().name(pom.license));
        }

        if (pom.url != null && !pom.url.isBlank()) {
            // OpenAPI Info has no explicit url field aside from contact/license; leave as description addition
            info.setDescription(info.getDescription() + "\n\nProject URL: " + pom.url);
        }

        return new OpenAPI().components(new Components()).info(info);
    }

    private static String nonEmpty(String value, String fallback) {
        return (value == null || value.isBlank()) ? fallback : value;
    }

    private PomInfo readPomInfo() {
        PomInfo info = new PomInfo();
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

        try {
            // Try to read pom.properties first (contains groupId, artifactId, version)
            Resource[] props = resolver.getResources("classpath*:/META-INF/maven/*/*/pom.properties");
            if (props != null && props.length > 0) {
                try (InputStream in = props[0].getInputStream()) {
                    Properties p = new Properties();
                    p.load(in);
                    info.version = p.getProperty("version");
                    String artifactId = p.getProperty("artifactId");
                    String groupId = p.getProperty("groupId");
                    if (artifactId != null && groupId != null) {
                        info.name = artifactId;
                    }
                }
            }

            // Try to parse pom.xml for richer metadata (description, url, developers, license)
            Resource[] poms = resolver.getResources("classpath*:/META-INF/maven/*/*/pom.xml");
            if (poms != null && poms.length > 0) {
                try (InputStream in = poms[0].getInputStream()) {
                    parsePomXml(in, info);
                }
            }
        } catch (IOException e) {
            // ignore, we'll return what we have
        }

        return info;
    }

    private void parsePomXml(InputStream in, PomInfo info) {
        try {
            String xml = new String(in.readAllBytes());
            XPath xp = XPathFactory.newInstance().newXPath();

            info.name = firstNonNull(info.name, xpathText(xml, xp, "/*[local-name()='project']/*[local-name()='name']/text()"));
            info.description = firstNonNull(info.description, xpathText(xml, xp, "/*[local-name()='project']/*[local-name()='description']/text()"));
            info.url = firstNonNull(info.url, xpathText(xml, xp, "/*[local-name()='project']/*[local-name()='url']/text()"));
            info.license = firstNonNull(info.license, xpathText(xml, xp, "/*[local-name()='project']/*[local-name()='licenses']/*[local-name()='license']/*[local-name()='name']/text()"));
            info.developerName = firstNonNull(info.developerName, xpathText(xml, xp, "/*[local-name()='project']/*[local-name()='developers']/*[local-name()='developer']/*[local-name()='name']/text()"));
            info.developerEmail = firstNonNull(info.developerEmail, xpathText(xml, xp, "/*[local-name()='project']/*[local-name()='developers']/*[local-name()='developer']/*[local-name()='email']/text()"));
        } catch (Exception e) {
            // parsing failed, ignore
        }
    }

    private String xpathText(String xml, XPath xp, String expr) throws XPathExpressionException {
        javax.xml.xpath.XPathExpression xpe = xp.compile(expr);
        Object res = xpe.evaluate(new org.xml.sax.InputSource(new java.io.StringReader(xml)), XPathConstants.STRING);
        String text = (res == null) ? null : res.toString();
        return (text != null && !text.isBlank()) ? text.trim() : null;
    }

    private static String firstNonNull(String a, String b) {
        return (a != null && !a.isBlank()) ? a : b;
    }

    private static class PomInfo {
        String name;
        String version;
        String description;
        String url;
        String developerName;
        String developerEmail;
        String license;
    }

}
