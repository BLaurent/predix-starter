package com.ge.digital.simplepredixservice;

import com.ge.digital.simplepredixservice.ingester.Ingester;
import com.ge.digital.simplepredixservice.model.PostSensorRequestBody;
import com.ge.digital.simplepredixservice.resource.SensorController;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SimplePredixServiceApplicationTests {

	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(),
			Charset.forName("utf8"));

	private MockMvc mockMvc;

	private HttpMessageConverter mappingJackson2HttpMessageConverter;

	@Mock
	private Ingester mockedObject;

	@InjectMocks
	SensorController sensorControllerUnderTests;

	@Autowired
	void setConverters(HttpMessageConverter<?>[] converters) {

		this.mappingJackson2HttpMessageConverter = Arrays.stream(converters)
				.filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
				.findAny()
				.orElse(null);

		assertNotNull("the JSON message converter must not be null",
				this.mappingJackson2HttpMessageConverter);
	}

	@BeforeClass
	public static void setSystemProps() {
		Stream.of("uaa.uri", "vcap.services.timeseries-lab.credentials.ingest.uri", "vcap.services.timeseries-lab.credentials.ingest.zone-http-header-value",
				"vcap.services.timeseries-lab.credentials.query.uri", "tsClientId", "tsClientSecret",
				"predix.timeseries.query.client.secret.env.variable",
				"vcap.services.timeseries-lab.credentials.query.uri",
				"vcap.services.uaa-lab.credentials.issuerId").forEach(
				in -> System.setProperty(in, ""));
	}

	@Before
	public void setup() throws Exception {
		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(sensorControllerUnderTests).build();
	}

	@Test
	public void createSensorDataPoint() throws Exception {
		PostSensorRequestBody sensorIngestionBody = new PostSensorRequestBody();
		sensorIngestionBody.setValue(1);
		sensorIngestionBody.setDeviceId("1");
		sensorIngestionBody.setIp("1.1.1.1");
		sensorIngestionBody.setType("1");
		sensorIngestionBody.setSec(1);
		sensorIngestionBody.setUsec(1);
		String sensor = json(sensorIngestionBody);

		String prefix = "/api/v1";
		this.mockMvc.perform(post(prefix + "/sensor")
				.contentType(contentType)
				.content(sensor))
				.andExpect(status().isOk());
	}

	private String json(Object o) throws IOException {
		MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
		this.mappingJackson2HttpMessageConverter.write(
				o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
		return mockHttpOutputMessage.getBodyAsString();
	}
}
