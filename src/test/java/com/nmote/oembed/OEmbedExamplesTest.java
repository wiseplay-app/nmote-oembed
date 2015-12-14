package com.nmote.oembed;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.junit.Ignore;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

@RunWith(Theories.class)
public class OEmbedExamplesTest {

	private static Map<String, List<String>> examples;
	private static Map<String, String> urlToProviderName;

	@DataPoints
	public static String[] urls() throws Exception {
		// Load examples
		try (InputStream in = OEmbedExamplesTest.class.getResourceAsStream("examples.json")) {
			ObjectMapper mapper = new ObjectMapper();
			TypeFactory tf = mapper.getTypeFactory();
			examples = mapper.readValue(in, tf.constructMapType(Map.class, tf.constructType(String.class),
					tf.constructCollectionType(List.class, String.class)));
		}

		List<String> result = new ArrayList<>();
		urlToProviderName = new HashMap<>();
		for (Map.Entry<String, List<String>> e : examples.entrySet()) {
			for (String url : e.getValue()) {
				URIBuilder b = new URIBuilder(url);
				for (NameValuePair nvp : b.getQueryParams()) {
					if ("url".equals(nvp.getName())) {
						urlToProviderName.put(nvp.getValue(), e.getKey());
						result.add(nvp.getValue());
					}
				}
			}
		}

		return result.toArray(new String[result.size()]);
	}

	@Theory
	public void resolveProviderByScheme(String url) throws IOException, URISyntaxException {
		DefaultOEmbedProvider ep = new DefaultOEmbedProvider();
		ProviderEndpoint pe = ep.getProviderEndpointFor(url);
		assertNotNull(pe);

		ProviderInfo pi = ep.getProviderByName(urlToProviderName.get(url));
		assertNotNull(pi);

		assertTrue(pi.getEndpoints().contains(pe));
	}

	@Ignore // Network access
	@Theory
	public void resolve(String url) throws IOException, URISyntaxException {
		DefaultOEmbedProvider ep = new DefaultOEmbedProvider();
		OEmbed oe = ep.resolve(url);
		System.out.println(oe);
	}
}
