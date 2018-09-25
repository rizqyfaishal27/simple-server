package utils;

import java.io.IOException;
import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.nio.file.Files;
import java.util.HashMap;

import router.Router;
import response.Response;
import response.SuccessResponse;
import response.FoundResponse;
import mimetypes.MimeTypes;

public class RouterConfig {

	public static Router createStaticFileRouter(String baseDirectory) throws IOException {
		Router router = new Router();
		File baseDir = new File(baseDirectory);
		traverseRecursive(router, baseDir, baseDirectory);
		additionalConfig(router, baseDirectory);
		return router;
	}


	public static void traverseRecursive(Router router, File dir, String rootDir) throws IOException {
		File[] files = dir.listFiles();
		for(File temp: files) {
			if(temp.isDirectory()) {
				traverseRecursive(router, temp, rootDir);
			} else {
				String mimeType = Files.probeContentType(temp.toPath());
				String path = temp.getPath();
				String cleanPath = path.substring(rootDir.length(), path.length());
				router.register("GET:" + cleanPath, new SuccessResponse(path, null, null, null, false), true);
			}
		}
	}

	public static void additionalConfig(Router router, String baseDirectory) throws IOException {
		String returnedData = "";
		String redirectedLocation = "/hello-world";

		Response foundResponse = new FoundResponse("/hello-world", returnedData.getBytes(), null, null);
		router.register("GET:/", foundResponse, false);
		router.register("POST:/", foundResponse, false);

		HashMap<String, String> helloWorldTemplateStrings = new HashMap<String, String>();
		helloWorldTemplateStrings.put("__HELLO__", "World");
		Response helloWorldResponse = new SuccessResponse(baseDirectory + "/hello-world.html", 
			helloWorldTemplateStrings, null, null, true);
		router.register("GET:/hello-world", helloWorldResponse, true);

		Response styleResponse = new SuccessResponse(baseDirectory + "/style.css", null, null, null, false);
		router.register("GET:/style", styleResponse, true);

		Response backgroundResponse = new SuccessResponse(baseDirectory + "/background.jpg", null, null, null, false);
		router.register("GET:/background", backgroundResponse, true);

		Response helloWorldPostResponse = new SuccessResponse(baseDirectory + "/hello-world.html",
			null, null, null, true);
		router.register("POST:/hello-world", helloWorldPostResponse, true);

		Response infoResponse = new SuccessResponse("__DATA__", null, null, null, false);
		router.register("GET:/info", infoResponse, true);
		
	}

}