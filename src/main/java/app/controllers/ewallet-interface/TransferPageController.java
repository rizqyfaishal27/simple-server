package webapp.controllers.ewalletinterfaces;

import java.io.BufferedOutputStream;

import webapp.controllers.BaseController;

import request.Request;
import response.Response;
import response.RedirectResponse;
import response.FileResponse;

public class TransferPageController extends BaseController {

	public TransferPageController(Request request, BufferedOutputStream responseStream) {
		super(request, responseStream);
	}

	public Response createResponse() {
		return new FileResponse("/transfer.html", request, responseStream);
	}
}