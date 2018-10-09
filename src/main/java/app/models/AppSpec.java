package webapp.models;

import java.util.HashMap;
import java.io.IOException;
import java.io.File;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.databind.DeserializationFeature;

import utils.AppConfig;
import utils.FileUtil;


public class AppSpec {
	public static final String yamlPath = "/spesifikasi.yaml";
	private static final AppSpec INSTANCE = parseYaml();

	private String swagger;
	private HashMap<String, Object> info;
	private String host;
	private String basePath;

	private AppSpec() {}

	public static AppSpec getInstance() {
		return INSTANCE;
	}

	public static AppSpec parseYaml() {
		try {
			File yamlText = new File(AppConfig.DIRECTORY_ROOT + yamlPath);
			ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			AppSpec spec = mapper.readValue(yamlText, AppSpec.class);
			return spec;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}

	public String getSwagger() {
		return this.swagger;
	}

	public void setSwagger(String swagger) {
		this.swagger = swagger;
	}

	public HashMap<String, Object> getInfo() {
		return this.info;
	}

	public void setInfo(HashMap<String, Object> info) {
		this.info = info;
	}

	public String getHost() {
		return this.host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getBasePath() {
		return this.basePath;
	}

	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}
}