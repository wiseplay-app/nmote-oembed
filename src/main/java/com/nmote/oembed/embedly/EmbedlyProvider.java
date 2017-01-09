/*
 * Copyright (c) Nmote Ltd. 2015. All rights reserved.
 * See LICENSE doc in a root of project folder for additional information.
 */

package com.nmote.oembed.embedly;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nmote.oembed.AbstractOEmbedProvider;
import com.nmote.oembed.ProviderEndpoint;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

public class EmbedlyProvider extends AbstractOEmbedProvider {

	private static final ProviderEndpoint PROVIDER_ENDPOINT = new ProviderEndpoint("http://api.embed.ly/1/oembed",
			EmbedlyEmbed.class);

	/**
	 * Makes an instance using default HTTP client and Jackson ObjectMapper.
	 *
	 * @param apiKey
	 *            your's embedly API key
	 */
	public EmbedlyProvider(String apiKey) {
		super();

		this.apiKey = apiKey;
	}

	/**
	 * Makes an instance using supplied httpClient and mapper.
	 *
	 * @param apiKey
	 *            your's embedly API key
	 * @param httpClient
	 *            OkHTTP client
	 * @param mapper
	 *            Jackson ObjectMapper instance
	 */
	public EmbedlyProvider(String apiKey, OkHttpClient httpClient, ObjectMapper mapper) {
		super(httpClient, mapper);

		this.apiKey = apiKey;
	}

	/**
	 * @see com.nmote.oembed.AbstractOEmbedProvider#getProviderEndpointFor(java.lang.String)
	 */
	@Override
	protected ProviderEndpoint getProviderEndpointFor(String url) {
		return PROVIDER_ENDPOINT;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void prepareRequestURI(HttpUrl.Builder builder, String url, Integer... maxSize) {
		super.prepareRequestURI(builder, url, maxSize);
		builder.addQueryParameter("key", apiKey);
	}

	private final String apiKey;
}
