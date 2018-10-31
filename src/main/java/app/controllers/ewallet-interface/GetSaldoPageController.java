package webapp.controllers.ewalletinterfaces;

import java.io.BufferedOutputStream;

import webapp.controllers.BaseController;


import request.Request;
import response.Response;
import response.RedirectResponse;
import response.FileResponse;

public class GetSaldoPageController extends BaseController {

	public GetSaldoPageController(Request request, BufferedOutputStream responseStream) {
		super(request, responseStream);
	}

	public Response createResponse() {
		return new FileResponse("/get-saldo.html", request, responseStream);
	}
}